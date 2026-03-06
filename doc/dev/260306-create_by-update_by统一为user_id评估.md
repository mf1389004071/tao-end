# create_by / update_by 统一为 user_id 改动范围与影响评估

## 1. 背景与目标

- **现状**：框架中 `create_by`、`update_by` 实际存储的是 **user_name**（登录名），由 `BaseEntityListener` 通过 `SecurityUtils.getUsername()` 写入。
- **目标**：将所有此类字段统一改为存储 **sys_user.user_id**，展示处通过关联 `sys_user` 查询昵称/真实姓名等再展示。
- **收益**：用户改名不影响历史；可建外键；按人统计、权限、JOIN 稳定；与业务表设计（changelog-2 中已标注「创建人ID」）一致。

---

## 2. 改动范围总览

| 层级 | 内容 | 数量/说明 |
|------|------|-----------|
| **数据库** | changelog-1-master 系统表 | 11 张表：create_by/update_by 从 VARCHAR(64) 改为 BIGINT，并做历史数据迁移 |
| **数据库** | changelog-2-business 业务表 | 约 22 张表：列类型由 VARCHAR(64) 改为 BIGINT（备注已是「创建人ID」） |
| **Java 基类与监听器** | BaseEntity + BaseEntityListener | 1 处：类型 String→Long，写入 getUserId() |
| **Java Controller** | 显式 setCreateBy/setUpdateBy | 约 7 个 Controller、十余处调用 |
| **Java Service** | 导入等场景 | SysUserServiceImpl、FileController 等 |
| **Mapper/XML** | resultMap 等 | 仅当实体字段类型变化，列名不变则多数无需改 |
| **前端 tao-pc** | 展示/筛选 createBy、updateBy | 若干列表、表单、类型定义需改为展示「名称」或传 ID |

---

## 3. 数据库层

### 3.1 changelog-1-master.xml（系统表）

涉及表（均为 `create_by`、`update_by` 两列，当前类型 `VARCHAR(64)`，备注「创建者」「更新者」）：

- sys_config  
- sys_dept  
- sys_dict_data  
- sys_dict_type  
- sys_file_info  
- sys_menu  
- sys_notice  
- sys_post  
- sys_role  
- sys_user  

**建议变更**（新增 changeSet，不直接改原 createTable）：

1. **改类型**：`ALTER COLUMN create_by TYPE BIGINT USING ...`，`update_by` 同理。  
   - 需做 **历史数据迁移**：`UPDATE 表 SET create_by = (SELECT user_id FROM sys_user WHERE user_name = 表.create_by)`（按库语法可不同，如 PostgreSQL 需先 CAST）。  
   - 无法解析的行（用户已删、历史脏数据）：置为 `NULL` 或保留一张 name→id 映射表再批量更新。
2. **约束**：可选 `ADD CONSTRAINT fk_xxx_create_by FOREIGN KEY (create_by) REFERENCES sys_user(user_id) ON DELETE SET NULL`，同样对 `update_by`。若存在大量无法解析的历史数据，可先不加重外键，仅改类型与语义。

### 3.2 changelog-2-business.xml（业务表）

业务表已标注「创建人ID」「更新人ID」，但列类型仍为 `VARCHAR(64)`。涉及 create_by/update_by（及部分表含 delete_by）的表包括但不限于：

- user_dept  
- knowledge_category  
- knowledge_content  
- knowledge_comment  
- event_info  
- event_role  
- event_session  
- event_check_in  
- ai_tasks  
- ai_meeting_transcripts  
- xiaoe_orders  
- files  
- point_product  
- point_redemption  
- payment_info  
- community_info  
- data_change_logs  
- system_configs  
- notices  
- user_profiles  
- tags  
- 以及其它在 changelog-2 中带 create_by/update_by 的表  

**建议**：新增 changeSet 将上述表中的 `create_by`、`update_by`（及 `delete_by` 若存在）改为 `BIGINT`，并视需要加外键引用 `sys_user(user_id)`。**新建库**可直接在 createTable 中把类型写成 BIGINT，避免后续再 ALTER。

### 3.3 操作日志表 sys_oper_log

- 当前为 `oper_name`（VARCHAR）存操作人名称，无 create_by/update_by。  
- 若希望「统一为人用 user_id」，可 **可选** 增加 `oper_id BIGINT REFERENCES sys_user(user_id)`，列表/详情展示时用 oper_id 关联出昵称/姓名；`oper_name` 可保留做冗余展示或兼容旧数据。

---

## 4. Java 后端层

### 4.1 基类与监听器（必改）

| 文件 | 改动 |
|------|------|
| `geek-common/.../BaseEntity.java` | `createBy`、`updateBy` 类型由 `String` 改为 `Long` |
| `geek-framework/.../BaseEntityListener.java` | `setCreateBy(SecurityUtils.getUsername())` → `setCreateBy(SecurityUtils.getUserId())`；`setUpdateBy` 同理。匿名场景仍 `setCreateBy(null)`/`setUpdateBy(null)` |

注意：`SecurityUtils.getUserId()` 返回 `Long`，与 `Long` 类型字段一致；需处理当前用户不存在时的 NPE（与现有 getLoginUser() 异常一致）。

### 4.2 显式 setCreateBy/setUpdateBy 的调用点（必改）

以下当前传入的是 **userName**，需改为 **userId**（或删除由 Listener 统一写入）：

| 文件 | 行号/场景 | 建议 |
|------|------------|------|
| SysUserController.java | 139, 159, 188, 202 | 改为 `setCreateBy(SecurityUtils.getUserId())` / `setUpdateBy(...)`，或删除由 Listener 写入 |
| SysRoleController.java | 107, 127, 165 | 同上 |
| SysPostController.java | 88, 105 | 同上 |
| SysNoticeController.java | 78, 90 | 同上 |
| SysMenuController.java | 97, 114 | 同上 |
| SysDictTypeController.java | 83, 98 | 同上 |
| SysDictDataController.java | 99, 111 | 同上 |
| SysDeptController.java | 89, 111 | 同上 |
| SysConfigController.java | 95, 110 | 同上 |
| FileController.java | 253, 255 | 改为 `SecurityUtils.getUserId()`，注意类型 Long |
| SysUserServiceImpl.java | 556, 565（导入用户） | 入参 `operName` 改为 `operId`（Long）或方法内 `SecurityUtils.getUserId()`，setCreateBy/setUpdateBy 传 Long |

若全部依赖 BaseEntityListener 写入，则 Controller/Service 中**仅在有特殊逻辑（如导入）时**显式 set；其余可删除显式 set，避免重复与类型不一致。

### 4.3 Mapper / MyBatis

- 列名仍为 `create_by`、`update_by`，仅 Java 类型由 String 改为 Long，**resultMap 中 property 类型随实体变化即可**，一般无需改 column 名。  
- 若个别 XML 中有对 create_by/update_by 的字符串拼接或条件判断，需确认是否仍合法（如 `create_by != ''` 改为 `create_by != null`）。

### 4.4 展示层（列表/详情需要显示「创建人/更新人」名称时）

- **推荐**：在返回给前端的 DTO/VO 中增加 `createByName`、`updateByName`（或统一命名如 `createByNickName`），在 Service 或 Mapper 层通过 **JOIN sys_user** 或 **批量查 sys_user** 按 user_id 解析出昵称/真实姓名再填入。  
- 这样前端只消费「名称」字段，不关心底层存的是 user_id；后续若统一为 user_id，只需保证这些 VO 字段由关联查询填充即可。

---

## 5. 前端 tao-pc 层

- **列表/详情**：若当前直接展示 `createBy`/`updateBy` 字符串，改后将为数字（user_id）。需改为展示接口返回的 **createByName/updateByName**（或等价字段），避免直接显示 ID。  
- **筛选/查询**：如「操作人员」筛选项当前传 `createBy` 字符串，可改为传 `createBy` 为 user_id（Long），后端按 user_id 过滤；若仍按「名称」筛选，则后端需按 name 解析为 user_id 再查。  
- **类型定义**：如 `dict.ts`、`PayOrder`、`createTable.vue` 等处的 `createBy`/`updateBy` 类型可由 `string` 改为 `number | null` 或保留 string（若后端仍以字符串形式传 ID），并与后端约定一致。  
- 涉及文件：  
  - `tao-pc/src/views/system/notice/index.vue`（查询 + 表格列）  
  - `tao-pc/src/views/tool/gen/edit/index.ts`、`createTable.vue`  
  - `tao-pc/src/types/dict.ts`  
  - `tao-pc/src/entity/pay/PayOrder.ts`  
  - 以及其它直接使用 createBy/updateBy 的列表、表单、描述组件  

---

## 6. 影响与风险

| 项目 | 说明 |
|------|------|
| **历史数据** | 系统表已有数据为 user_name，需一次性迁移脚本：user_name → user_id，无法匹配的置 NULL 或单独处理。 |
| **兼容性** | 若有外部系统或报表直接读 create_by/update_by 且假定为「用户名」，需同步通知或提供视图/接口返回名称。 |
| **操作日志** | 若为 oper_name 单独增加 oper_id，需评估 LogAspect 写入逻辑与列表展示改造。 |
| **业务表未上线** | 若业务表尚未投产，可直接在 createTable 中将 create_by/update_by 定为 BIGINT，避免后续 ALTER。 |

---

## 7. 建议实施顺序

1. **数据库**  
   - 先为 **业务表**（changelog-2）在新建库或新 changeSet 中统一为 BIGINT；已建表则增加 changeSet 做 ALTER + 外键（可选）。  
   - 再对 **系统表**（changelog-1）增加 changeSet：历史数据迁移（user_name→user_id）→ 改列类型 → 可选外键。  
2. **后端**  
   - 改 BaseEntity（String→Long）、BaseEntityListener（getUsername→getUserId），再改所有显式 setCreateBy/setUpdateBy 为 Long 或删除。  
   - 列表/详情接口增加 createByName/updateByName 的关联查询或批量解析。  
3. **前端**  
   - 列表/详情改为展示 createByName/updateByName；筛选与类型定义随后端约定调整。  

建议在 **业务表尚未大量使用前** 完成统一，可减少历史数据迁移与兼容成本；系统表因已有数据，需单独做一次迁移与回归测试。

---

## 8. 小结

- **改动范围**：约 11 张系统表 + 约 22 张业务表（列类型 + 可选外键）、1 个基类 + 1 个监听器、约 7 个 Controller + 2 处 Service/文件上传、若干前端页面与类型定义。  
- **核心点**：存储统一为 **user_id（BIGINT）**，展示统一通过 **关联 sys_user 查昵称/姓名** 并在 VO 中提供 createByName/updateByName。  
- **建议**：采纳「统一为 user_id 存储、展示处关联查询」的方案；实施时按「库 → 后端 → 前端」顺序，并优先保证业务表从设计上就使用 BIGINT，再处理系统表与历史数据迁移。
