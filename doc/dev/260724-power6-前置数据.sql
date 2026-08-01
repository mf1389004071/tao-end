-- =============================================================================
-- 六大能力测评（POWER6 / P6）前置：结构补齐 + 业务种子（打开即用）
-- 日期：2026-07-24
-- 库：PostgreSQL（bt10）
--
-- 用法示例：
--   docker exec -i bt10-db psql -U postgres -d bt10 < tao-end/scripts/sql/260724-power6-前置数据.sql
--
-- 本脚本幂等：可重复执行。
-- 含：P6 缺表缺列 DDL + 身份/产品/工具内容/菜单/演示登单/admin 演示权益
--
-- 需你本地配置（不在 SQL）：
--   微信：tao-end/geek-admin/src/main/resources/application-auth.yml
--     oauth.wx.miniapp.appId / appSecret
--     oauth.wx.pub.appId / appSecret
--   AI：application-dev.yml geek.ai 或环境变量 YUNWU_API_KEY
-- =============================================================================

BEGIN;

-- ###########################################################################
-- A. DDL（对齐 changelog-2-power6-tool.xml，仅补缺）
-- ###########################################################################

CREATE TABLE IF NOT EXISTS biz_product (
    id              BIGSERIAL PRIMARY KEY,
    code            VARCHAR(64) NOT NULL UNIQUE,
    name            VARCHAR(255) NOT NULL,
    product_type    VARCHAR(50),
    price_amount    NUMERIC(24,4),
    valid_days      INT,
    identity_code   VARCHAR(50),
    buy_url         TEXT,
    cover_image_url TEXT,
    detail_content  TEXT,
    biz_status      VARCHAR(20) DEFAULT 'ACTIVE',
    order_num       INT DEFAULT 0,
    json_data       JSONB,
    create_id       BIGINT,
    create_by       VARCHAR(64) DEFAULT '',
    create_time     TIMESTAMPTZ,
    update_id       BIGINT,
    update_by       VARCHAR(64) DEFAULT '',
    update_time     TIMESTAMPTZ,
    delete_id       BIGINT,
    delete_time     TIMESTAMPTZ,
    status          CHAR(1) DEFAULT '0',
    del_flag        SMALLINT DEFAULT 0,
    remark          TEXT
);
CREATE INDEX IF NOT EXISTS idx_biz_product_identity ON biz_product(identity_code);
CREATE INDEX IF NOT EXISTS idx_biz_product_biz_status ON biz_product(biz_status);

CREATE TABLE IF NOT EXISTS product_beneficiary (
    id           BIGSERIAL PRIMARY KEY,
    product_id   BIGINT NOT NULL REFERENCES biz_product(id) ON DELETE CASCADE,
    user_id      BIGINT,
    role_code    VARCHAR(50) NOT NULL,
    share_ratio  NUMERIC(10,6) NOT NULL,
    currency     VARCHAR(20) DEFAULT 'CONTRIB',
    create_id    BIGINT,
    create_by    VARCHAR(64) DEFAULT '',
    create_time  TIMESTAMPTZ,
    update_id    BIGINT,
    update_by    VARCHAR(64) DEFAULT '',
    update_time  TIMESTAMPTZ,
    status       CHAR(1) DEFAULT '0',
    del_flag     SMALLINT DEFAULT 0,
    remark       TEXT
);
CREATE INDEX IF NOT EXISTS idx_product_beneficiary_product ON product_beneficiary(product_id);

CREATE TABLE IF NOT EXISTS knowledge_usage_record (
    id                    BIGSERIAL PRIMARY KEY,
    content_id            BIGINT NOT NULL REFERENCES knowledge_content(id) ON DELETE CASCADE,
    content_type          VARCHAR(50),
    tool_code             VARCHAR(64),
    user_id               BIGINT NOT NULL REFERENCES sys_user(user_id) ON DELETE CASCADE,
    identity_code         VARCHAR(50),
    subject_name          VARCHAR(100),
    answer_json           JSONB,
    score_summary         JSONB,
    result_basic          JSONB,
    result_pro            JSONB,
    result_human          JSONB,
    comment_id            BIGINT,
    ai_task_id            BIGINT,
    deducted              BOOLEAN DEFAULT FALSE,
    used_under_membership BOOLEAN DEFAULT FALSE,
    create_id             BIGINT,
    create_by             VARCHAR(64) DEFAULT '',
    create_time           TIMESTAMPTZ,
    update_id             BIGINT,
    update_by             VARCHAR(64) DEFAULT '',
    update_time           TIMESTAMPTZ,
    delete_id             BIGINT,
    delete_time           TIMESTAMPTZ,
    status                CHAR(1) DEFAULT '0',
    del_flag              SMALLINT DEFAULT 0,
    remark                TEXT
);
CREATE INDEX IF NOT EXISTS idx_knowledge_usage_user ON knowledge_usage_record(user_id);
CREATE INDEX IF NOT EXISTS idx_knowledge_usage_content ON knowledge_usage_record(content_id);
CREATE INDEX IF NOT EXISTS idx_knowledge_usage_tool ON knowledge_usage_record(tool_code);

ALTER TABLE identities ADD COLUMN IF NOT EXISTS default_available_points INT DEFAULT 0;
ALTER TABLE user_identities ADD COLUMN IF NOT EXISTS available_points INT DEFAULT 0;
ALTER TABLE user_point_logs ADD COLUMN IF NOT EXISTS identity_code VARCHAR(50);
ALTER TABLE user_invite ADD COLUMN IF NOT EXISTS identity_code VARCHAR(50);
ALTER TABLE knowledge_comment ADD COLUMN IF NOT EXISTS visibility VARCHAR(20) DEFAULT 'PUBLIC';
ALTER TABLE knowledge_comment ADD COLUMN IF NOT EXISTS usage_record_id BIGINT;
ALTER TABLE knowledge_comment ADD COLUMN IF NOT EXISTS identity_code VARCHAR(50);
ALTER TABLE knowledge_action ADD COLUMN IF NOT EXISTS target_type VARCHAR(20) DEFAULT 'CONTENT';
ALTER TABLE knowledge_action ADD COLUMN IF NOT EXISTS target_id BIGINT;
ALTER TABLE payment_items ADD COLUMN IF NOT EXISTS product_id BIGINT;
ALTER TABLE xiaoe_orders ADD COLUMN IF NOT EXISTS claim_phone VARCHAR(20);
ALTER TABLE xiaoe_orders ADD COLUMN IF NOT EXISTS product_id BIGINT;

CREATE INDEX IF NOT EXISTS idx_user_point_logs_identity ON user_point_logs(identity_code);
CREATE INDEX IF NOT EXISTS idx_user_invite_identity ON user_invite(identity_code);
CREATE INDEX IF NOT EXISTS idx_knowledge_comment_usage ON knowledge_comment(usage_record_id);
CREATE INDEX IF NOT EXISTS idx_payment_items_product ON payment_items(product_id);
CREATE INDEX IF NOT EXISTS idx_xiaoe_orders_claim_phone ON xiaoe_orders(claim_phone);
CREATE INDEX IF NOT EXISTS idx_xiaoe_orders_product ON xiaoe_orders(product_id);

-- 双靶/身份唯一索引：已存在则忽略
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'uk_user_identities_user_code') THEN
        CREATE UNIQUE INDEX uk_user_identities_user_code
            ON user_identities(user_id, identity_code, del_flag);
    END IF;
EXCEPTION WHEN OTHERS THEN
    RAISE NOTICE 'skip uk_user_identities_user_code: %', SQLERRM;
END $$;

UPDATE knowledge_action
SET target_type = COALESCE(NULLIF(target_type, ''), 'CONTENT'),
    target_id = COALESCE(target_id, content_id)
WHERE target_id IS NULL OR target_type IS NULL;

-- ###########################################################################
-- B. 种子数据
-- ###########################################################################

INSERT INTO knowledge_category (name, slug, parent_id, order_num, status, del_flag, create_time)
SELECT '工具', 'kb-tool', NULL, 2, '0', 0, NOW()
WHERE NOT EXISTS (SELECT 1 FROM knowledge_category WHERE slug = 'kb-tool');

INSERT INTO identities (
    identity_code, identity_level, name, intro, rights_text,
    default_available_points, create_by, create_time, status, del_flag, remark
)
SELECT
    'IDENTITY_P6', 10, '六大能力测评权益',
    '潜能测评专业解读与有效期权益',
    '有效期内高级解读不扣次；过期可用身份域次数解锁高级',
    3, 'system', NOW(), '0', 0, 'P6前置数据'
WHERE NOT EXISTS (SELECT 1 FROM identities WHERE identity_code = 'IDENTITY_P6');

UPDATE identities
SET default_available_points = 3,
    name = '六大能力测评权益',
    intro = '潜能测评专业解读与有效期权益',
    rights_text = '有效期内高级解读不扣次；过期可用身份域次数解锁高级',
    update_by = 'system',
    update_time = NOW()
WHERE identity_code = 'IDENTITY_P6';

INSERT INTO biz_product (
    id, code, name, product_type, price_amount, valid_days, identity_code,
    buy_url, cover_image_url, detail_content, biz_status, order_num, json_data,
    create_id, create_by, create_time, status, del_flag, remark
)
SELECT
    940001,
    'SKILL_199_P6',
    '识别好199·六大能力技能包',
    'SKILL_PACK',
    199.0000,
    30,
    'IDENTITY_P6',
    '',
    '',
    '购买后授期 30 天：有效期内高级解读免扣次；过期可用身份域次数解锁。',
    'ACTIVE',
    10,
    '{"scene":"POWER6","sku":"199"}'::jsonb,
    1, 'admin', NOW(), '0', 0, 'P6前置数据'
WHERE NOT EXISTS (SELECT 1 FROM biz_product WHERE code = 'SKILL_199_P6');

-- 固定序列，避免后续 BIGSERIAL 撞 940001
SELECT setval(pg_get_serial_sequence('biz_product', 'id'), GREATEST(940001, (SELECT COALESCE(MAX(id), 1) FROM biz_product)));

UPDATE biz_product
SET name = '识别好199·六大能力技能包',
    product_type = 'SKILL_PACK',
    price_amount = 199.0000,
    valid_days = 30,
    identity_code = 'IDENTITY_P6',
    biz_status = 'ACTIVE',
    order_num = COALESCE(order_num, 10),
    detail_content = COALESCE(NULLIF(detail_content, ''), '购买后授期 30 天：有效期内高级解读免扣次。'),
    update_by = 'system',
    update_time = NOW()
WHERE code = 'SKILL_199_P6';

INSERT INTO product_beneficiary (
    product_id, user_id, role_code, share_ratio, currency,
    create_id, create_by, create_time, status, del_flag, remark
)
SELECT p.id, NULL, 'PLATFORM', 1.000000, 'CONTRIB',
       1, 'admin', NOW(), '0', 0, 'P6前置数据-平台分润占位'
FROM biz_product p
WHERE p.code = 'SKILL_199_P6'
  AND NOT EXISTS (
      SELECT 1 FROM product_beneficiary b
      WHERE b.product_id = p.id AND b.role_code = 'PLATFORM' AND COALESCE(b.del_flag, 0) = 0
  );

INSERT INTO knowledge_content (
    id, title, subtitle, content, content_type, category_id, author_id,
    tags, biz_status, publish_time,
    view_count, like_count, comment_count, share_count, collect_count,
    promotional_text, ai_summary, json_data,
    create_id, create_by, create_time, status, del_flag, remark
)
SELECT
    920006,
    '「10倍好」潜能测评',
    '六大能力测评工具',
    '真实计分：三框各 4 个番号(1–24 不重复)，权重 5/3/1，六维 M/Z/X/S/K/Q。基础免费；高级 AI 按权益。',
    'TOOL',
    (SELECT id FROM knowledge_category WHERE slug = 'kb-tool' LIMIT 1),
    1,
    '潜能,测评,POWER6,六大能力',
    'PUBLISHED',
    NOW(),
    0, 0, 0, 0, 0,
    '3 分钟看清自己的六维潜能画像',
    '服务端 Power6ToolHandler 计分；高级解读走 yunwu + 本地 HTML 模板。',
    jsonb_build_object(
        'toolCode', 'POWER6',
        'identityCode', 'IDENTITY_P6',
        'productCode', 'SKILL_199_P6',
        'productId', (SELECT id::text FROM biz_product WHERE code = 'SKILL_199_P6' LIMIT 1),
        'entry_url', '/#/tools/power6?contentId=920006',
        'claim_url', '/#/tools/claim',
        'ai_prompt', '你是「10倍好」潜能测评教练。根据六维得分输出 JSON：aiNarrative / aiActions / focusDimension。',
        'pro_templates', jsonb_build_object(
            'default', 'classpath:tool-templates/POWER6/default.html',
            'focus', 'classpath:tool-templates/POWER6/focus.html'
        )
    ),
    1, 'admin', NOW(), '0', 0, 'P6前置数据'
WHERE NOT EXISTS (SELECT 1 FROM knowledge_content WHERE id = 920006);

SELECT setval(pg_get_serial_sequence('knowledge_content', 'id'), GREATEST(920006, (SELECT COALESCE(MAX(id), 1) FROM knowledge_content)));

UPDATE knowledge_content
SET title = '「10倍好」潜能测评',
    subtitle = '六大能力测评工具',
    content_type = 'TOOL',
    category_id = COALESCE(category_id, (SELECT id FROM knowledge_category WHERE slug = 'kb-tool' LIMIT 1)),
    biz_status = 'PUBLISHED',
    tags = '潜能,测评,POWER6,六大能力',
    json_data = jsonb_build_object(
        'toolCode', 'POWER6',
        'identityCode', 'IDENTITY_P6',
        'productCode', 'SKILL_199_P6',
        'productId', (SELECT id::text FROM biz_product WHERE code = 'SKILL_199_P6' LIMIT 1),
        'entry_url', '/#/tools/power6?contentId=920006',
        'claim_url', '/#/tools/claim',
        'ai_prompt', '你是「10倍好」潜能测评教练。根据六维得分输出 JSON：aiNarrative / aiActions / focusDimension。',
        'pro_templates', jsonb_build_object(
            'default', 'classpath:tool-templates/POWER6/default.html',
            'focus', 'classpath:tool-templates/POWER6/focus.html'
        )
    ),
    update_by = 'system',
    update_time = NOW()
WHERE id = 920006;

-- PC 菜单：业务产品
INSERT INTO sys_menu (
    menu_id, menu_name, parent_id, order_num, path, component, query, route_name,
    is_frame, is_cache, menu_type, visible, status, perms, icon,
    create_by, create_time, remark
)
SELECT 12980, '业务产品', 3, 2, 'bizproduct', 'bt10/bizproduct/index', NULL, '',
       1, 0, 'C', '0', '0', 'bt10:bizproduct:list', 'shopping',
       'admin', NOW(), '业务产品(现金/技能包)'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 12980);

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
SELECT 12981, '业务产品查询', 12980, 1, '#', '', 1, 0, 'F', '0', '0', 'bt10:bizproduct:query', '#', 'admin', NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 12981);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
SELECT 12982, '业务产品新增', 12980, 2, '#', '', 1, 0, 'F', '0', '0', 'bt10:bizproduct:add', '#', 'admin', NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 12982);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
SELECT 12983, '业务产品修改', 12980, 3, '#', '', 1, 0, 'F', '0', '0', 'bt10:bizproduct:edit', '#', 'admin', NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 12983);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
SELECT 12984, '业务产品删除', 12980, 4, '#', '', 1, 0, 'F', '0', '0', 'bt10:bizproduct:remove', '#', 'admin', NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 12984);
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time)
SELECT 12985, '业务产品导出', 12980, 5, '#', '', 1, 0, 'F', '0', '0', 'bt10:bizproduct:export', '#', 'admin', NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_id = 12985);

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM (VALUES (1), (2)) AS r(role_id)
CROSS JOIN (VALUES (12980),(12981),(12982),(12983),(12984),(12985)) AS m(menu_id)
WHERE NOT EXISTS (
    SELECT 1 FROM sys_role_menu x WHERE x.role_id = r.role_id AND x.menu_id = m.menu_id
);

-- 演示待兑登单
INSERT INTO xiaoe_orders (
    id, xiaoe_order_no, order_state, actual_fee, goods_name, goods_type,
    pay_state, pay_type, trade_no, xiaoe_create_time,
    sync_status, process_status, claim_phone, product_id,
    create_id, create_by, create_time, status, del_flag, remark
)
SELECT
    950001,
    'P6DEMO20260724001',
    'paid',
    199.0000,
    '识别好199·六大能力技能包',
    'SKILL_PACK',
    'SUCCESS',
    'XIAOE',
    'P6DEMO_TRADE_001',
    NOW(),
    'DONE',
    'WAIT_CLAIM',
    '13800138000',
    (SELECT id FROM biz_product WHERE code = 'SKILL_199_P6' LIMIT 1),
    1, 'admin', NOW(), '0', 0, 'P6演示待兑登单'
WHERE NOT EXISTS (SELECT 1 FROM xiaoe_orders WHERE xiaoe_order_no = 'P6DEMO20260724001');

SELECT setval(pg_get_serial_sequence('xiaoe_orders', 'id'), GREATEST(950002, (SELECT COALESCE(MAX(id), 1) FROM xiaoe_orders)));

UPDATE xiaoe_orders
SET process_status = CASE WHEN process_status = 'CLAIMED' THEN process_status ELSE 'WAIT_CLAIM' END,
    claim_phone = '13800138000',
    product_id = (SELECT id FROM biz_product WHERE code = 'SKILL_199_P6' LIMIT 1),
    actual_fee = 199.0000,
    goods_name = '识别好199·六大能力技能包',
    update_by = 'system',
    update_time = NOW()
WHERE xiaoe_order_no = 'P6DEMO20260724001'
  AND COALESCE(process_status, '') <> 'CLAIMED';

INSERT INTO xiaoe_orders (
    id, xiaoe_order_no, order_state, actual_fee, goods_name, goods_type,
    pay_state, pay_type, trade_no, xiaoe_create_time,
    sync_status, process_status, claim_phone, product_id,
    create_id, create_by, create_time, status, del_flag, remark
)
SELECT
    950002,
    'P6DEMO20260724002',
    'paid',
    199.0000,
    '识别好199·六大能力技能包',
    'SKILL_PACK',
    'SUCCESS',
    'XIAOE',
    'P6DEMO_TRADE_002',
    NOW(),
    'DONE',
    'WAIT_CLAIM',
    '13900139000',
    (SELECT id FROM biz_product WHERE code = 'SKILL_199_P6' LIMIT 1),
    1, 'admin', NOW(), '0', 0, 'P6演示待兑登单#2'
WHERE NOT EXISTS (SELECT 1 FROM xiaoe_orders WHERE xiaoe_order_no = 'P6DEMO20260724002');

-- admin 演示权益
INSERT INTO user_identities (
    user_id, identity_code, is_primary, biz_status, acquired_time, expired_time,
    available_points, source_type, create_id, create_by, create_time, status, del_flag, remark
)
SELECT
    1, 'IDENTITY_P6', false, 'ACTIVE', NOW(), NOW() + INTERVAL '30 day',
    3, 'SEED', 1, 'admin', NOW(), '0', 0, 'P6前置数据-admin演示权益'
WHERE EXISTS (SELECT 1 FROM sys_user WHERE user_id = 1)
  AND NOT EXISTS (
      SELECT 1 FROM user_identities
      WHERE user_id = 1 AND identity_code = 'IDENTITY_P6' AND COALESCE(del_flag, 0) = 0
  );

UPDATE user_identities
SET available_points = GREATEST(COALESCE(available_points, 0), 3),
    expired_time = COALESCE(expired_time, NOW() + INTERVAL '30 day'),
    biz_status = 'ACTIVE',
    update_by = 'system',
    update_time = NOW()
WHERE user_id = 1
  AND identity_code = 'IDENTITY_P6'
  AND COALESCE(del_flag, 0) = 0;

COMMIT;

-- 自检
SELECT 'IDENTITY_P6' AS k, default_available_points::text AS v FROM identities WHERE identity_code='IDENTITY_P6'
UNION ALL
SELECT 'product', id::text || ' / ' || code FROM biz_product WHERE code='SKILL_199_P6'
UNION ALL
SELECT 'content', id::text || ' / ' || content_type FROM knowledge_content WHERE id=920006
UNION ALL
SELECT 'xiaoe', xiaoe_order_no || ' / ' || claim_phone || ' / ' || process_status
FROM xiaoe_orders WHERE xiaoe_order_no LIKE 'P6DEMO%'
UNION ALL
SELECT 'admin_ui', COALESCE(available_points::text,'') || ' pts / exp=' || COALESCE(expired_time::text,'')
FROM user_identities WHERE user_id=1 AND identity_code='IDENTITY_P6';
