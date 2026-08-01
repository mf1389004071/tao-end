# 小鹅通课程/专栏/大专栏列表 vs biz_product 评估

> 抓取时间：2026-08-02  
> 样本 JSON：`xeknow/camp_pro_list_capture.json`、`column_list_capture.json`、`big_column_list_capture.json`  
> 索引：`xeknow/product_list_capture_index.json`

## 1. 页面与接口

| UI | 路由 | resource_type | 列表 API | 样本 total |
| --- | --- | --- | --- | --- |
| 课程（系列课） | `/t/course/camp_pro/list#/list` | 50 | `POST /xe.course.b_admin_r.camp_pro.list/2.0.0` | 153 |
| 专栏 | `/t/course/column/list` | 6 | `POST /xe.course.b_admin_r.course.base.list/1.0.0` | 48 |
| 大专栏 | `/t/course/big_column/list` | 8 | `POST /xe.course.b_admin_r.course.base.list/1.0.0` | 15 |

说明：系列课页顶部「课程/专栏/大专栏/会员」是**不同路由**，不是同一接口里用 `goods_type` 区分。  
系列课返回里的 `goods_type` 表示售卖形态（免费/付费/加密等），不是「专栏/大专栏」。

## 2. 字段可映射性（相对 biz_product）

| biz_product | 小鹅通来源 | 备注 |
| --- | --- | --- |
| `code` | `resource_id` | 可做业务唯一键；需保证不与现有 `SKILL_*` 冲突 |
| `name` | `title` | OK |
| `product_type` | `resource_type`→枚举 | 建议 `XIAOE_CAMP_PRO`/`XIAOE_COLUMN`/`XIAOE_BIG_COLUMN` |
| `price_amount` | `price/100` | 接口为分 |
| `cover_image_url` | `img_url` / `img_url_compressed` | OK |
| `buy_url` | `h5_url` | **仅 base.list（专栏/大专栏）有**；系列课列表无此字段 |
| `detail_content` | `summary` | 系列课有；专栏/大专栏列表常空 |
| `biz_status` | `sale_status`/`is_display`/`is_stop_sell` | 需约定映射，不能 1:1 |
| `order_num` | `position` | OK |
| `valid_days` | `period` | 结构复杂（绝对截止/相对秒），不宜直接塞 INT |
| `identity_code` | 无 | Power6 技能包概念，小鹅通无对应 |
| `json_data` | 整行原始 JSON | 建议保留 |

系列课独有运营字段：`user_count`、`resource_cnt`、`interactive_cnt`、`sub_course_cnt`、`belong_user_info` 等——`biz_product` 无独立列，只能进 `json_data`。

## 3. 结论

**可以“勉强塞进”`biz_product`，但不建议把小鹅通全量目录直接当 biz_product 主数据。**

原因：

1. `biz_product` 定位是本平台**现金/技能包业务产品**（含 `identity_code`、受益人分账），与小鹅通资源目录是不同域。  
2. 全量约 200+ 条资源混入后，会污染运营产品列表与权限/分账逻辑。  
3. 关键字段与状态机不一致（`period`、上架/停售/审核多标志）。  
4. 订单侧已有小鹅商品信息；产品主数据更适合「同步表 + 可选关联本平台产品」。

## 4. 推荐方案

**推荐（稳）：新建 `xiaoe_products`（或 `xiaoe_resources`）同步表**

- 唯一键：`resource_id`（或 `app_id + resource_id`）  
- 独立列：`resource_type`、`title`、`price`、`img_url`、`sale_status`、`is_display`、`h5_url`、`updated_at`…  
- `json_data` 存完整列表项  
- 需要在本平台售卖/分账时，再 `biz_product.code = resource_id` **显式关联**一条业务产品（少量人工或规则映射）

**可接受折中（小改）：扩展 `biz_product`**

- 增加 `xiaoe_resource_id`、`xiaoe_resource_type`（或约定 `code` 前缀 `XE_`）  
- `product_type` 区分 `PLATFORM` vs `XIAOE_*`  
- 管理端列表默认过滤掉同步资源  
- 仍建议订单关联走 `resource_id`，不要强迫每条小鹅资源都有受益人配置

**不推荐：** 无强制关联地全量 upsert 进现有 `biz_product`，与 `SKILL_199_P6` 混用同一套运营语义。

## 5. 落地结果（已实施）

| 表 | source | 覆盖 |
| --- | --- | --- |
| `xiaoe_products` | `product_list` | 系列课50 / 专栏6 / 大专栏8 |
| `xiaoe_contents` | `content_list` | 图文1 / 音频2 / 视频3 |

- 独立列尽量铺开；`json_data` 存原始行  
- 列表翻页字段必须用 **`page_index`**（`page` 会被忽略导致重复）  
- 油猴脚本 v0.4；工具脚本：`xeknow/tools/browser-build-sync-queue.js`、`report-envelope.mjs`  
- 首次全量入库（验算）：products **216**，contents **1950**  

## 6. 采集上报建议

- 与订单/用户相同：浏览器会话 + `GM_xmlhttpRequest` 上报  
- MVP：列表字段够用；详情页暂不做  

