# Cursor 规则与 Skills 建议

> 说明当前项目已配置的 Cursor Rules、设计原则与是否建议做 Skills，为 AI 辅助开发提供稳定、准确的上下文。

---

## 一、已配置的 Cursor Rules 概览

规则存放在 **`.cursor/rules/`**，均为 `.mdc` 格式，通过 frontmatter 控制生效范围。

| 规则文件 | 作用 | 生效方式 |
|:---|:---|:---|
| **project-context.mdc** | 项目与业务背景、三端结构、开发规范优先级、**文档索引**（需求/设计/数据库/编码） | `alwaysApply: true` |
| **requirements-priority.mdc** | **需求优先级 P0/P1/P2 与期数 MVP/V2/V3+**、落地路径、开发时必须遵守（先确认期数、不扩大范围、不遗漏 P0） | `alwaysApply: true` |
| **tao-end-backend.mdc** | 后端分层、统一返回、分页、权限与日志、示例与注意（含需求期数、返回格式） | 编辑 `tao-end/**/*.java` 时 |
| **business-domain.mdc** | 业务域词汇（活动/知识库/成长/积分/邀请/资源广场）、使用建议与优先级提醒 | 编辑后端或前端业务代码时 |
| **data-and-api.mdc** | **数据表设计约定、API 契约**（code/msg/data、rows/total）、常见错误避免 | 编辑后端或前端接口/表相关时 |
| **tao-pc-vue.mdc** | 管理端 Vue3 + Element Plus、目录与路由、与后端接口约定 | 编辑 `tao-pc/src` 下 Vue/TS 时 |
| **tao-app-uniapp.mdc** | 小程序端 UniApp、与后端对接、页面与业务对应、插件使用 | 编辑 `tao-app/src` 下 Vue/TS 时 |

设计原则：

- **符合当前项目架构**：包结构、BaseController、AjaxResult/TableDataInfo、@PreAuthorize/@Log 等与 tao-end（RuoYi-Geek）现有写法一致。
- **规范以当前项目为准**：Java 及行业规范、项目内约定优先；表设计与 API 以需求文档与项目内设计为准。
- **需求与期数先行**：实现前确认 P0/P1/P2 与 MVP/V2/V3+，避免做错期、扩大范围或漏做 P0。

---

## 二、是否要做成 Skills？建议

- **Rules 为主**：当前规则已覆盖项目背景、需求优先级、后端/前端/数据与 API 约定，能显著减少 AI 的理解偏差与实现错误；随项目演进在对应 `.mdc` 中微调即可。
- **Skills 可选**：若需“按步骤新增一整个业务模块（表→实体→Mapper→Service→Controller→菜单→前端 CRUD）”的固定流程，可单独做一份 `.cursor/skills/add-business-module/SKILL.md`，引用现有 Rules 并写明步骤；日常单文件修改、Bug 修复不必做 Skill。

---

## 三、文档与规则的关系

- **需求与设计**：来自 `tao-end/doc/backup/原始分析/`、`tao-end/doc/backup/需求设计/`、`tao-end/doc/dev/260126-软件开发需求详细设计说明书.md` 等，规则中已索引；开发前优先查阅对应文档，规则负责提炼优先级、期数与约定，不替代文档全文。
- **数据库**：表结构、枚举、审计字段等以 `tao-end/doc/dev` 及详细设计说明书为准；规则中「数据与 API 约定」「需求优先级与期数」约束实现时的边界与格式，避免臆造表或返回结构。

---

## 四、后续可做事项

1. **Rules**：随新模块或规范变更，在对应 `.mdc` 中补充（如新增 geek-module-ai 时在 project-context 或业务域中写一句）。
2. **Skill（可选）**：若确定需要“按步骤新增业务模块”的自动化指引，再新增 Skill 并注明遵循 tao-end-backend、business-domain、requirements-priority。
