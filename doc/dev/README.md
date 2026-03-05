# Better10 数字化平台 - 开发文档目录

本目录包含完整的数据库设计和软件开发需求详细设计说明书,可直接用于指导AI编码实现。

## 文件说明

- **数据库表结构**：当前由 tao-end 的 **Liquibase** 管理，启动应用时自动执行 `geek-admin/src/main/resources/db/changelog` 下的脚本，无需单独执行 SQL 文件。新增表或变更请通过 changelog 维护。

### 1. `软件开发需求详细设计说明书.md`
详细的软件开发设计文档,包含:
- 项目概述与架构设计
- 数据库设计说明
- 各功能模块的详细实现方案
- 前后端逻辑说明
- UI/UX设计要点
- 成熟套件推荐

**适用对象**:
- AI编码助手(Cursor、GitHub Copilot等)
- 全栈开发工程师
- 产品经理和技术负责人

## 核心设计原则

1. **AI-Native设计**: 代码结构清晰,便于AI理解
2. **变更日志完整**: 所有重要业务数据都有变更记录
3. **业务逻辑自洽**: 符合公司战略规划和业务需求
4. **用户体验优先**: 功能交互通俗简单好理解
5. **数据关系充分**: 业务场景覆盖充分,数据存留完整

## 快速开始

### 1. 阅读设计文档

打开 `软件开发需求详细设计说明书.md`,按照模块顺序阅读:
1. 项目概述与架构设计
2. OneID统一身份系统
3. 积分与贡献点双币种体系
4. 知识库系统
5. 活动自动化系统
6. 线下课程运营系统
7. AI知识工厂
8. 小程序端设计
9. 管理端设计

### 2. 开始编码

根据设计文档中的代码示例和实现方案,使用AI编码助手开始开发:
- 后端: tao-end（Spring Boot 3 + RuoYi-Geek + MyBatis-Flex），对接 PostgreSQL/MySQL
- 管理端: tao-pc（Vue 3 + Element Plus + Vite），对接 tao-end 的 REST API
- 小程序端: tao-app（UniApp + Vue 3），对接同一后端

**本地运行**：参见各端目录下的 README 或 tao-end/docker 的编排说明；管理端与小程序需配置后端 API 地址后启动。

## 数据库表清单

### 核心业务表
- `users` - 用户主表
- `events` - 活动表
- `knowledge_contents` - 知识内容表
- `payments` - 支付订单表
- `communities` - 社群表

### 关联表
- `user_point_logs` - 积分流水
- `user_contribution_logs` - 贡献点流水
- `event_registrations` - 活动报名
- `event_roles` - 活动角色
- `knowledge_comments` - 内容评论

### 变更日志表
- `data_change_logs` - 通用变更日志
- `user_change_logs` - 用户变更日志
- `event_change_logs` - 活动变更日志
- `point_change_logs` - 积分变更日志

### AI相关表
- `ai_tasks` - AI任务表
- `ai_vectors` - 向量知识库
- `meeting_transcripts` - 会议转写

### 小鹅通相关表
- `xiaoe_orders` - 小鹅通订单
- `xiaoe_user_mappings` - 用户映射

## 注意事项

1. **数据库扩展**: 确保PostgreSQL已安装`pgvector`和`pg_trgm`扩展
2. **UUID主键**: 所有表使用UUID v7作为主键
3. **软删除**: 所有业务表支持软删除,保留审计字段
4. **变更日志**: 重要操作必须记录变更日志
5. **索引优化**: 根据实际查询场景调整索引

## 更新日志

- **2026-01-26**: 初始版本发布
  - 完成数据库表结构设计
  - 完成详细设计说明书编写
  - 包含所有核心业务模块的实现方案

## 联系方式

如有问题或建议,请联系开发团队。
