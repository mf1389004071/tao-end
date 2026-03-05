



# Better10 数字化平台 - 业务详细设计说明书 (BDD)

**版本**: 1.0 **状态**: 正式设计稿 **架构模式**: Majestic Monolith (宏大单体) + AI-Native **技术栈**: Laravel 11 + Filament v3 + PostgreSQL 16 (All-in-One) + Inertia.js/Vue3

## 1. 设计综述 (Executive Summary)

### 1.1 核心目标

构建一套自动化运营与知识沉淀系统，解决“10倍好”组织当前面临的人力重复投入、信息孤岛与知识流失问题。通过数字化手段，支撑“1001个幸福家庭”与“1001个公益图书馆”的宏大愿景。

### 1.2 业务架构逻辑图

```
graph TD
    User[用户/流量端] -->|微信授权/手机号| OneID(统一身份中心)
    OneID -->|身份识别| RoleEngine(权益引擎)
    
    subgraph 核心业务循环
        RoleEngine -->|分发权益| Activity(活动自动化)
        Activity -->|产生数据| AI_Factory(AI 知识工厂)
        AI_Factory -->|沉淀资产| KnowledgeBase(知识库)
        KnowledgeBase -->|赋能| UserGrowth(增长与裂变)
        UserGrowth -->|晋升/付费| RoleEngine
    end

    subgraph 基础设施 (PostgreSQL)
        DB[(业务数据)]
        Vector[(向量数据)]
        Queue[(任务队列)]
        Cache[(缓存数据)]
    end
```

## 2. 模块一：OneID 与权益体系 (Identity & Rights)

**业务痛点解决**: 解决角色（联创、合伙人）对自身权益感知模糊，以及跨平台（小鹅通、微信、会议）身份不互通的问题。

### 2.1 统一身份模型 (User Schema)

- **唯一标识**: 手机号 (Mobile) + 微信 UnionID。
- **账号体系**:
  - 支持 `wechat_openid` (服务号), `miniapp_openid` (小程序), `work_userid` (企微)。
  - **设计策略**: 使用 `social_identities` 表存储多平台 ID，关联至主 `users` 表。

### 2.2 角色与权益引擎 (RBAC + Strategy)

系统不仅仅是标记角色，而是**动态计算权益**。

- **角色定义 (Roles)**:
  - `Founder` (沐诚), `CoFounder` (联创), `Partner` (合伙人), `Master` (高手), `CityHost` (城市主理人), `Member` (普通用户)。
- **权益定义 (Permissions/Entitlements)**:
  - `access_morning_call`: 准入早课。
  - `library_level_1`: 知识库L1访问权。
  - `discount_offline_20`: 线下课8折。

#### 2.2.1 业务流程：身份晋升

1. **触发**: 用户支付成功 (小鹅通回调/系统内支付) 或 管理员后台手动标记。
2. **动作 (Action)**: `UpgradeUserRoleAction`。
3. **结果**:
   - 更新用户角色标签。
   - 自动发放“权益包”（如：3张线下课兑换券 -> 写入 `user_coupons` 表）。
   - 发送微信模版消息：“恭喜您成为合伙人，您的专属权益已到账。”

### 2.3 用户端实现 (H5/Inertia)

- **个人工作台 (Dashboard)**:
  - 头部展示：头像、当前身份徽章、专属客服（绑定运营人员）。
  - 核心卡片：
    - **我的活动**: 待参加的腾讯会议链接（一键唤起）。
    - **权益资产**: 剩余兑换券、积分。
    - **知识库**: 根据身份解锁的文件夹。

## 3. 模块二：活动自动化工厂 (Activity Automation)

**业务痛点解决**: 解决“每日早课/用书会”耗时 100+ 人时的重复拉群、通知、统计痛点。

### 3.1 核心功能：周期性活动引擎

针对“1001团队齐心”和“用书会”等周期性活动。

- **数据结构 (Events Table)**:
  - `recurrence_rule`: 存储 RRule (如 `FREQ=DAILY;INTERVAL=1;BYHOUR=5`)。
  - `notification_channels`: JSON 数组 `['wechat_service', 'wechat_work_group']`。
  - `meeting_url`: 固定的腾讯会议链接。

#### 3.1.1 自动化流程 (The "5 AM" Loop)

1. **调度器 (Scheduler)**: 每分钟检查 `events` 表。
2. **触发 (Trigger)**: 检测到“1001团队齐心”将在 15 分钟后开始。
3. **生成场次 (Instance)**: 自动创建 `event_sessions` 记录 (如 2025-09-12 场次)。
4. **通知分发 (Notification Action)**:
   - **渠道 A (服务号)**: 筛选所有 `access_morning_call` 权限用户 -> 发送模版消息。
   - **渠道 B (企微群)**: 调用企微 Webhook 发送卡片消息。
5. **签到逻辑 (Check-in)**:
   - 通知消息中包含**带参数的短链接** (`/join/{session_id}/{user_hash}`)。
   - 用户点击链接 -> 系统记录 `check_in_time` -> 302 重定向唤起腾讯会议 App。
   - **价值**: 实现“无感签到”，无需人工在群里接龙。

### 3.2 线下活动支持 (Offline Support)

针对“城市主理人”发起的线下活动。

- **主理人工作台**: 提交活动申请（时间、地点、所需物料）。
- **报名系统**: 生成活动海报二维码。
- **现场签到**:
  - 方案 A: 主理人手机端扫用户的二维码。
  - 方案 B: 用户扫现场大屏二维码（根据地理位置校验防作弊）。

## 4. 模块三：AI 知识工厂 (AI Knowledge Factory)

**业务痛点解决**: 解决会议内容流失、知识无法复用、海报文案撰写难的问题。

### 4.1 原始素材采集层

- **输入源**:
  - **腾讯会议录制**: 配置腾讯会议回调或人工上传 MP4/MP3。
  - **直播回放**: 小鹅通下载链接。
- **处理流 (Pipeline)**:
  - 文件上传 -> `spatie/laravel-medialibrary` 处理。
  - 触发异步任务 `ProcessMeetingRecordingJob`。

### 4.2 AI 处理层 (Intelligence Layer)

利用 PostgreSQL `pgvector` 和外部 LLM (通义千问/Kimi) 实现。

#### 4.2.1 流程设计

1. **ASR 转写**: 调用腾讯云 ASR 接口，音频转文字 (JSON, 含时间戳)。
   - *存储*: `meeting_transcripts` (JSONB 类型，保留说话人区分)。
2. **智能总结 (Summarization)**:
   - Prompt: "你是10倍好体系的知识官，请总结本次会议的：1. 核心金句；2. 行动清单；3. 认知翻转点。"
   - *存储*: `meeting_summaries`。
3. **知识切片 (Chunking)**:
   - 将全文按语义切分为 500 字段落。
   - 调用 Embedding API (如 OpenAI/DashScope) 生成向量。
   - *存储*: `knowledge_vectors` (pgvector 插件)。

### 4.3 应用层：认知翻转与问答

- **自动生成海报文案**: 活动结束后，自动将“金句”推送到运营端，运营一键生成图片分享。
- **智能问答 (RAG)**:
  - 用户提问：“沐诚老师关于‘利他’是怎么说的？”
  - 系统检索向量库 -> 召回相关段落 -> LLM 生成回答。

## 5. 模块四：增长与生态 (Growth & Ecosystem)

**业务痛点解决**: 流量闭环，赋能城市主理人，数据驱动决策。

### 5.1 全渠道引流追踪

- **场景**: 微信文章、海报、视频号挂链。
- **机制**:
  - 生成带参数二维码 (`/ref/{source_id}/{promoter_id}`)。
  - 用户扫码关注/注册 -> 自动绑定“推荐人”和“来源渠道”。
  - **价值**: 此时可以精确计算哪个渠道带来的“合伙人”最多。

### 5.2 城市主理人赋能包

- **资源申请**: 在线申请物料（书籍、T恤），后台审批后发货。
- **数据透视**: 主理人可查看本城市的用户活跃热力图。

## 6. 系统集成与技术实现细节 (Technical Implementation)

### 6.1 外部接口集成策略 (Integration Strategy)

为了保持“外部依赖少”且“AI编码友好”，我们将所有第三方集成封装为独立的 `Service` 类。

- **WeChatService**:
  - 使用 `EasyWeChat` SDK (Laravel 版)。
  - 功能：OAuth授权、模版消息发送、微信支付。
- **TencentMeetingService**:
  - 封装腾讯会议 REST API (v1)。
  - 功能：创建会议、查询参会人员、获取录制文件。
- **XiaoETongService**:
  - 同步学员信息、订单信息 (通过 Webhook 接收数据)。

### 6.2 数据库设计关键点 (Schema Highlights)

利用 PostgreSQL 的强大特性简化架构。

- **AI 向量表**:

  ```
  CREATE TABLE knowledge_vectors (
      id bigserial PRIMARY KEY,
      content text,
      embedding vector(1536), -- 适配通义千问/OpenAI
      metadata jsonb -- 存储来源会议ID、时间戳、讲师
  );
  CREATE INDEX ON knowledge_vectors USING hnsw (embedding vector_cosine_ops);
  ```

- **活动场次表**:

  ```
  CREATE TABLE event_sessions (
      id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
      event_id uuid REFERENCES events(id),
      start_time timestamptz,
      status varchar(20), -- 'SCHEDULED', 'LIVE', 'ENDED'
      check_in_count integer DEFAULT 0,
      summary_text text -- AI生成的总结
  );
  ```

### 6.3 部署架构 (Deployment)

- **OS**: Ubuntu 22.04 LTS。
- **Docker Compose**:
  - `app`: PHP 8.3 + Nginx (业务逻辑)。
  - `db`: PostgreSQL 16 (数据 + 向量 + 队列)。
  - `scheduler`: 独立的 PHP 容器运行 `php artisan schedule:work` (处理定时任务)。
  - `worker`: 独立的 PHP 容器运行 `php artisan queue:work` (处理 AI 耗时任务)。

## 7. 实施路线图建议 (Roadmap)

**阶段一：连接 (Week 1-2)**

1. 搭建 Laravel + Postgres 基础框架。
2. 完成微信服务号/小程序授权登录 (OneID)。
3. 导入现有用户数据 (Excel/小鹅通)。
4. 上线“个人权益看板” H5 页面。

**阶段二：自动化 (Week 3-4)**

1. 开发活动管理后台 (Filament)。
2. 配置定时任务与微信模版消息接口。
3. 跑通“早课”自动通知与链接签到流程。

**阶段三：AI 知识库 (Week 5-6)**

1. 集成腾讯云 ASR 与 LLM 接口。
2. 开发“会议录音上传 -> 自动转写 -> 摘要生成”链路。
3. 上线知识库搜索功能。













