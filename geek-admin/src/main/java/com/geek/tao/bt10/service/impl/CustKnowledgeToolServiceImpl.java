package com.geek.tao.bt10.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geek.ai.service.AiChatService;
import com.geek.common.core.domain.entity.SysUser;
import com.geek.common.exception.ServiceException;
import com.geek.common.utils.StringUtils;
import com.geek.system.service.ISysUserService;
import com.geek.tao.bt10.domain.AiTasks;
import com.geek.tao.bt10.domain.BizProduct;
import com.geek.tao.bt10.domain.Identities;
import com.geek.tao.bt10.domain.KnowledgeComment;
import com.geek.tao.bt10.domain.KnowledgeContent;
import com.geek.tao.bt10.domain.KnowledgeUsageRecord;
import com.geek.tao.bt10.domain.PaymentInfo;
import com.geek.tao.bt10.domain.PaymentItems;
import com.geek.tao.bt10.domain.ProductBeneficiary;
import com.geek.tao.bt10.domain.UserIdentities;
import com.geek.tao.bt10.domain.UserPointLogs;
import com.geek.tao.bt10.domain.XiaoeOrders;
import com.geek.tao.bt10.mapper.AiTasksMapper;
import com.geek.tao.bt10.mapper.BizProductMapper;
import com.geek.tao.bt10.mapper.IdentitiesMapper;
import com.geek.tao.bt10.mapper.KnowledgeCommentMapper;
import com.geek.tao.bt10.mapper.KnowledgeContentMapper;
import com.geek.tao.bt10.mapper.KnowledgeUsageRecordMapper;
import com.geek.tao.bt10.mapper.PaymentInfoMapper;
import com.geek.tao.bt10.mapper.PaymentItemsMapper;
import com.geek.tao.bt10.mapper.ProductBeneficiaryMapper;
import com.geek.tao.bt10.mapper.UserIdentitiesMapper;
import com.geek.tao.bt10.mapper.UserPointLogsMapper;
import com.geek.tao.bt10.mapper.XiaoeOrdersMapper;
import com.geek.tao.bt10.service.ICustKnowledgeToolService;
import com.geek.tao.bt10.tool.KnowledgeToolHandler;
import com.geek.tao.bt10.tool.KnowledgeToolHandlerRegistry;
import com.geek.tao.bt10.tool.ToolProTemplateService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CustKnowledgeToolServiceImpl implements ICustKnowledgeToolService {

    public static final String IDENTITY_P6 = "IDENTITY_P6";

    @Autowired
    private KnowledgeContentMapper knowledgeContentMapper;
    @Autowired
    private KnowledgeUsageRecordMapper usageRecordMapper;
    @Autowired
    private KnowledgeCommentMapper commentMapper;
    @Autowired
    private UserIdentitiesMapper userIdentitiesMapper;
    @Autowired
    private IdentitiesMapper identitiesMapper;
    @Autowired
    private KnowledgeToolHandlerRegistry handlerRegistry;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AiChatService aiChatService;
    @Autowired
    private ToolProTemplateService templateService;
    @Autowired
    private AiTasksMapper aiTasksMapper;
    @Autowired
    private UserPointLogsMapper userPointLogsMapper;
    @Autowired
    private XiaoeOrdersMapper xiaoeOrdersMapper;
    @Autowired
    private BizProductMapper bizProductMapper;
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;
    @Autowired
    private PaymentItemsMapper paymentItemsMapper;
    @Autowired
    private ProductBeneficiaryMapper productBeneficiaryMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private Environment environment;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.of("Asia/Shanghai"));

    @Override
    public Map<String, Object> entitlement(Long userId, Long contentId) {
        KnowledgeContent content = requireContent(contentId);
        String identityCode = resolveIdentityCode(content);
        UserIdentities ui = ensureUserIdentity(userId, identityCode);
        boolean memberActive = ui.getExpiredTime() != null && ui.getExpiredTime().isAfter(Instant.now());
        Map<String, Object> data = new HashMap<>();
        data.put("identityCode", identityCode);
        data.put("availablePoints", ui.getAvailablePoints() == null ? 0 : ui.getAvailablePoints());
        data.put("expiredTime", ui.getExpiredTime());
        data.put("memberActive", memberActive);
        data.put("toolCode", resolveToolCode(content));
        return data;
    }

    @Override
    @Transactional
    public Map<String, Object> submit(Long userId, Long contentId, String subjectName, Map<String, Object> answers) {
        KnowledgeContent content = requireContent(contentId);
        String toolCode = resolveToolCode(content);
        String identityCode = resolveIdentityCode(content);
        ensureUserIdentity(userId, identityCode);

        KnowledgeToolHandler handler = handlerRegistry.require(toolCode);
        Map<String, Object> scored = handler.score(answers == null ? Map.of() : answers);

        KnowledgeUsageRecord rec = new KnowledgeUsageRecord();
        rec.setContentId(contentId);
        rec.setContentType(content.getContentType());
        rec.setToolCode(toolCode);
        rec.setUserId(userId);
        rec.setIdentityCode(identityCode);
        rec.setSubjectName(StringUtils.isEmpty(subjectName) ? null : subjectName.trim());
        rec.setAnswerJson(toJson(answers));
        rec.setScoreSummary(toJson(scored.get("scoreSummary")));
        rec.setResultBasic(toJson(scored.get("resultBasic")));
        rec.setDeducted(false);
        rec.setUsedUnderMembership(false);
        usageRecordMapper.insert(rec);

        KnowledgeComment comment = new KnowledgeComment();
        comment.setContentId(contentId);
        comment.setUserId(userId);
        comment.setContent(buildCommentSummary(subjectName, scored));
        comment.setBizStatus("PUBLISHED");
        comment.setVisibility("PRIVATE");
        comment.setUsageRecordId(rec.getId());
        comment.setIdentityCode(identityCode);
        comment.setLikeCount(0);
        comment.setIsPinned(false);
        commentMapper.insert(comment);

        rec.setCommentId(comment.getId());
        usageRecordMapper.update(rec);

        Map<String, Object> data = new HashMap<>();
        data.put("usageId", String.valueOf(rec.getId()));
        data.put("commentId", String.valueOf(comment.getId()));
        data.put("scoreSummary", scored.get("scoreSummary"));
        data.put("resultBasic", scored.get("resultBasic"));
        data.put("entitlement", entitlement(userId, contentId));
        return data;
    }

    @Override
    public Map<String, Object> unlockPro(Long userId, Long usageId, String templateKey) {
        if (usageId == null) throw new ServiceException("usageId 不能为空");
        KnowledgeUsageRecord rec = usageRecordMapper.selectOneById(usageId);
        if (rec == null || !userId.equals(rec.getUserId())) {
            throw new ServiceException("记录不存在");
        }

        // 已有结果：直接回看
        if (StringUtils.isNotEmpty(rec.getResultPro()) && !"null".equals(rec.getResultPro())) {
            return buildProResponse(rec, false);
        }

        // 进行中任务幂等
        if (rec.getAiTaskId() != null) {
            AiTasks running = aiTasksMapper.selectOneById(rec.getAiTaskId());
            if (running != null && ("PENDING".equals(running.getBizStatus()) || "RUNNING".equals(running.getBizStatus()))) {
                Map<String, Object> pending = new HashMap<>();
                pending.put("status", "RUNNING");
                pending.put("taskId", String.valueOf(running.getId()));
                pending.put("usageId", String.valueOf(rec.getId()));
                return pending;
            }
        }

        UserIdentities ui = ensureUserIdentity(userId, rec.getIdentityCode());
        boolean memberActive = ui.getExpiredTime() != null && ui.getExpiredTime().isAfter(Instant.now());
        int points = ui.getAvailablePoints() == null ? 0 : ui.getAvailablePoints();
        if (!memberActive && points <= 0) {
            throw new ServiceException("高级解读次数不足，请购买或兑换权益");
        }

        String toolCode = StringUtils.isEmpty(rec.getToolCode()) ? "POWER6" : rec.getToolCode();
        String tplKey = StringUtils.isEmpty(templateKey) ? templateService.pickRandomKey(toolCode) : templateKey;

        AiTasks task = new AiTasks();
        task.setTaskType("KNOWLEDGE_INTERPRET");
        task.setBizStatus("RUNNING");
        task.setPriority(5);
        task.setRelatedType("USAGE");
        task.setRelatedId(rec.getId());
        task.setConfig(toJson(Map.of("toolCode", toolCode, "templateKey", tplKey)));
        task.setStartTime(Instant.now());
        task.setProgressPercentage(10);
        aiTasksMapper.insert(task);
        rec.setAiTaskId(task.getId());
        usageRecordMapper.update(rec);

        try {
            Map<String, Object> scoreSummary = readJsonMap(rec.getScoreSummary());
            Map<String, Object> resultBasic = readJsonMap(rec.getResultBasic());

            String system = """
                    你是「10倍好」潜能测评教练。根据用户六维得分输出 JSON（不要 Markdown 围栏），字段：
                    {"aiNarrative":"综合解读200字内","aiActions":"3条行动建议，换行分隔","focusDimension":"最需关注的维度中文名如目标力"}
                    """;
            String user = "被测称呼: " + (rec.getSubjectName() == null ? "学员" : rec.getSubjectName())
                    + "\n得分: " + toJson(scoreSummary)
                    + "\n基础卡片: " + toJson(resultBasic.get("cards"))
                    + "\n请基于得分给出解读。";

            String aiText;
            try {
                aiText = aiChatService.chat(system, user);
            } catch (Exception aiEx) {
                // 开发环境外网 AI 超时/失败时，用本地兜底解读，保证模板渲染与扣次链路可测
                if (environment.acceptsProfiles(Profiles.of("dev"))) {
                    aiText = buildDevFallbackAiJson(scoreSummary, rec.getSubjectName());
                } else {
                    throw aiEx;
                }
            }
            Map<String, Object> aiSlots = parseAiJson(aiText);

            Map<String, Object> slots = new LinkedHashMap<>();
            slots.put("subjectName", rec.getSubjectName() == null ? "学员" : rec.getSubjectName());
            slots.put("generatedAt", FMT.format(Instant.now()));
            slots.put("scoresHtml", buildScoresHtml(scoreSummary));
            slots.put("aiNarrative", String.valueOf(aiSlots.getOrDefault("aiNarrative", aiText)));
            slots.put("aiActions", String.valueOf(aiSlots.getOrDefault("aiActions", "")));
            slots.put("focusDimension", String.valueOf(aiSlots.getOrDefault("focusDimension", "综合")));

            String rendered = templateService.render(toolCode, tplKey, slots);

            Map<String, Object> pro = new LinkedHashMap<>();
            pro.put("templateKey", tplKey);
            pro.put("slots", slots);
            pro.put("html", rendered);
            pro.put("aiRaw", aiText);
            pro.put("generatedAt", Instant.now().toString());

            rec.setResultPro(toJson(pro));
            rec.setUsedUnderMembership(memberActive);
            if (!memberActive) {
                int before = points;
                int after = before - 1;
                ui.setAvailablePoints(after);
                userIdentitiesMapper.update(ui);
                UserPointLogs log = new UserPointLogs();
                log.setUserId(userId);
                log.setActionType("TOOL_PRO_UNLOCK");
                log.setPoints(-1L);
                log.setBalanceBefore((long) before);
                log.setBalanceAfter((long) after);
                log.setRelatedType("USAGE");
                log.setRelatedId(rec.getId());
                log.setIdentityCode(rec.getIdentityCode());
                userPointLogsMapper.insert(log);
                rec.setDeducted(true);
            } else {
                rec.setDeducted(false);
            }
            usageRecordMapper.update(rec);

            task.setBizStatus("DONE");
            task.setProgressPercentage(100);
            task.setCompleteTime(Instant.now());
            task.setResult(toJson(Map.of("templateKey", tplKey)));
            aiTasksMapper.update(task);

            return buildProResponse(rec, true);
        } catch (Exception e) {
            task.setBizStatus("FAILED");
            task.setErrorMessage(e.getMessage() == null ? "AI失败" : e.getMessage());
            task.setCompleteTime(Instant.now());
            aiTasksMapper.update(task);
            if (e instanceof ServiceException se) throw se;
            throw new ServiceException("高级解读失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Map<String, Object> claim(Long userId, String xiaoeOrderNo, String phone) {
        if (StringUtils.isEmpty(xiaoeOrderNo) || StringUtils.isEmpty(phone)) {
            throw new ServiceException("请填写小鹅通订单号和手机号");
        }
        String orderNoKey = xiaoeOrderNo.trim();
        String phoneNorm = normalizePhone(phone);
        if (phoneNorm.length() < 7) throw new ServiceException("手机号格式不正确");

        XiaoeOrders xo = xiaoeOrdersMapper.selectOneByQuery(QueryWrapper.create()
                .from(XiaoeOrders.class)
                .eq(XiaoeOrders::getXiaoeOrderNo, orderNoKey));
        if (xo == null) throw new ServiceException("未找到待兑订单，请确认订单号或联系客服登单");

        if ("CLAIMED".equals(xo.getProcessStatus())) {
            if (userId.equals(xo.getUserId())) {
                Map<String, Object> ok = new HashMap<>();
                ok.put("claimed", true);
                ok.put("idempotent", true);
                ok.put("xiaoeOrderId", String.valueOf(xo.getId()));
                ok.put("orderNo", xo.getOrderNo());
                return ok;
            }
            throw new ServiceException("该订单已被兑换");
        }

        String claimPhone = normalizePhone(xo.getClaimPhone());
        if (StringUtils.isEmpty(claimPhone) || !claimPhone.equals(phoneNorm)) {
            throw new ServiceException("订单号与手机号不匹配");
        }
        if (xo.getProductId() == null) {
            throw new ServiceException("订单未关联产品，请联系客服");
        }

        BizProduct product = bizProductMapper.selectOneById(xo.getProductId());
        if (product == null) throw new ServiceException("产品不存在");
        String identityCode = StringUtils.isEmpty(product.getIdentityCode()) ? IDENTITY_P6 : product.getIdentityCode();
        int validDays = product.getValidDays() == null || product.getValidDays() <= 0 ? 30 : product.getValidDays();

        // 补绑手机
        SysUser user = userService.selectUserById(userId);
        if (user != null && StringUtils.isEmpty(user.getPhonenumber())) {
            user.setPhonenumber(phoneNorm);
            userService.updateUserProfile(user);
        }

        String localOrderNo = "XO" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6);
        PaymentInfo pay = new PaymentInfo();
        pay.setOrderNo(localOrderNo);
        pay.setUserId(userId);
        pay.setOrderType("BIZ_PRODUCT");
        pay.setRelatedType("XIAOE_ORDER");
        pay.setRelatedId(xo.getId());
        pay.setTotalAmount(xo.getActualFee() != null ? xo.getActualFee() : product.getPriceAmount());
        pay.setCashAmount(pay.getTotalAmount() == null ? BigDecimal.ZERO : pay.getTotalAmount());
        pay.setPaymentStatus("PAID");
        pay.setPaymentMethod("XIAOE");
        pay.setPaymentNo(xo.getTradeNo());
        pay.setPaidTime(Instant.now());
        pay.setJsonData(toJson(Map.of(
                "xiaoeOrderNo", orderNoKey,
                "claimPhone", phoneNorm,
                "productCode", product.getCode() == null ? "" : product.getCode())));
        paymentInfoMapper.insert(pay);

        PaymentItems item = new PaymentItems();
        item.setPaymentId(pay.getId());
        item.setItemType("IDENTITY");
        item.setItemName(product.getName());
        item.setOriginalAmount(product.getPriceAmount());
        item.setAmount(product.getPriceAmount());
        item.setQuantity(1);
        item.setIsGift(false);
        item.setUsageStatus("USED");
        item.setGrantMethod("CLAIM");
        item.setRelatedType("IDENTITY");
        item.setRelatedId(null);
        item.setProductId(product.getId());
        paymentItemsMapper.insert(item);

        UserIdentities ui = ensureUserIdentity(userId, identityCode);
        Instant base = Instant.now();
        if (ui.getExpiredTime() != null && ui.getExpiredTime().isAfter(base)) {
            base = ui.getExpiredTime();
        }
        Instant newExp = base.plus(validDays, ChronoUnit.DAYS);
        ui.setExpiredTime(newExp);
        ui.setBizStatus("ACTIVE");
        ui.setSourceType("PAYMENT");
        ui.setSourceId(pay.getId());
        userIdentitiesMapper.update(ui);

        // 分润快照写入支付扩展（不发钱，仅记录配置）
        List<ProductBeneficiary> bens = productBeneficiaryMapper.selectListByQuery(QueryWrapper.create()
                .from(ProductBeneficiary.class)
                .eq(ProductBeneficiary::getProductId, product.getId()));
        if (bens != null && !bens.isEmpty()) {
            pay.setText1(toJson(bens.stream().map(b -> Map.of(
                    "userId", String.valueOf(b.getUserId()),
                    "roleCode", b.getRoleCode() == null ? "" : b.getRoleCode(),
                    "shareRatio", b.getShareRatio() == null ? "0" : b.getShareRatio().toPlainString()
            )).toList()));
            paymentInfoMapper.update(pay);
        }

        xo.setUserId(userId);
        xo.setOrderNo(localOrderNo);
        xo.setProcessStatus("CLAIMED");
        xiaoeOrdersMapper.update(xo);

        Map<String, Object> data = new HashMap<>();
        data.put("claimed", true);
        data.put("idempotent", false);
        data.put("xiaoeOrderId", String.valueOf(xo.getId()));
        data.put("paymentId", String.valueOf(pay.getId()));
        data.put("orderNo", localOrderNo);
        data.put("identityCode", identityCode);
        data.put("expiredTime", newExp.toString());
        data.put("validDays", validDays);
        data.put("availablePoints", ui.getAvailablePoints());
        return data;
    }

    private static String normalizePhone(String phone) {
        if (phone == null) return "";
        String digits = phone.trim().replaceAll("[^0-9]", "");
        if (digits.length() > 11) {
            digits = digits.substring(digits.length() - 11);
        }
        return digits;
    }

    private Map<String, Object> buildProResponse(KnowledgeUsageRecord rec, boolean newlyGenerated) {
        Map<String, Object> pro = readJsonMap(rec.getResultPro());
        Map<String, Object> data = new HashMap<>();
        data.put("status", "DONE");
        data.put("newlyGenerated", newlyGenerated);
        data.put("usageId", String.valueOf(rec.getId()));
        data.put("commentId", rec.getCommentId() == null ? null : String.valueOf(rec.getCommentId()));
        data.put("resultPro", pro);
        data.put("templateKey", pro.get("templateKey"));
        data.put("html", pro.get("html"));
        return data;
    }

    @SuppressWarnings("unchecked")
    private String buildScoresHtml(Map<String, Object> scoreSummary) {
        if (scoreSummary == null) return "";
        Object totalsObj = scoreSummary.get("totals");
        Object labelsObj = scoreSummary.get("labels");
        if (!(totalsObj instanceof Map<?, ?> totals)) return "";
        Map<String, Object> labels = labelsObj instanceof Map<?, ?> m ? (Map<String, Object>) m : Map.of();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> e : totals.entrySet()) {
            String key = String.valueOf(e.getKey());
            int score = e.getValue() instanceof Number n ? n.intValue() : 0;
            String label = String.valueOf(labels.getOrDefault(key, key));
            String cls = score > 0 ? "pos" : (score < 0 ? "neg" : "");
            String sign = score > 0 ? "+" : "";
            sb.append("<div class=\"dim\"><span>").append(label).append(" <small>")
                    .append(key).append("</small></span><span class=\"score ").append(cls).append("\">")
                    .append(sign).append(score).append("</span></div>");
        }
        return sb.toString();
    }

    private String buildDevFallbackAiJson(Map<String, Object> scoreSummary, String subjectName) {
        String name = StringUtils.isEmpty(subjectName) ? "学员" : subjectName;
        String totals = scoreSummary == null ? "{}" : String.valueOf(scoreSummary.get("totals"));
        String narrative = name + " 的潜能画像（开发环境本地兜底，因 AI 网关不可达）。得分摘要：" + totals
                + "。建议先聚焦相对偏低的维度做小步练习，再巩固优势项。";
        String actions = "1. 每天记录一个可量化小目标并完成\n2. 把优势维度用于本周一件关键事\n3. 找同伴复盘一次卡点";
        return toJson(Map.of(
                "aiNarrative", narrative,
                "aiActions", actions,
                "focusDimension", "综合"));
    }

    private Map<String, Object> parseAiJson(String aiText) {
        if (StringUtils.isEmpty(aiText)) return Map.of();
        String t = aiText.trim();
        if (t.startsWith("```")) {
            int i = t.indexOf('{');
            int j = t.lastIndexOf('}');
            if (i >= 0 && j > i) t = t.substring(i, j + 1);
        }
        try {
            return objectMapper.readValue(t, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return Map.of("aiNarrative", aiText);
        }
    }

    private Map<String, Object> readJsonMap(String json) {
        if (StringUtils.isEmpty(json)) return new HashMap<>();
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private KnowledgeContent requireContent(Long contentId) {
        if (contentId == null) throw new ServiceException("contentId 不能为空");
        KnowledgeContent c = knowledgeContentMapper.selectOneById(contentId);
        if (c == null) throw new ServiceException("工具内容不存在");
        return c;
    }

    private String resolveToolCode(KnowledgeContent content) {
        String code = readJsonField(content.getJsonData(), "toolCode");
        return StringUtils.isEmpty(code) ? "POWER6" : code;
    }

    private String resolveIdentityCode(KnowledgeContent content) {
        String code = readJsonField(content.getJsonData(), "identityCode");
        return StringUtils.isEmpty(code) ? IDENTITY_P6 : code;
    }

    private UserIdentities ensureUserIdentity(Long userId, String identityCode) {
        UserIdentities ui = userIdentitiesMapper.selectOneByQuery(QueryWrapper.create()
                .from(UserIdentities.class)
                .eq(UserIdentities::getUserId, userId)
                .eq(UserIdentities::getIdentityCode, identityCode));
        if (ui != null) {
            if (ui.getAvailablePoints() == null) {
                ui.setAvailablePoints(0);
                userIdentitiesMapper.update(ui);
            }
            return ui;
        }
        Identities def = identitiesMapper.selectOneById(identityCode);
        int defaults = def != null && def.getDefaultAvailablePoints() != null
                ? def.getDefaultAvailablePoints() : 3;
        ui = new UserIdentities();
        ui.setUserId(userId);
        ui.setIdentityCode(identityCode);
        ui.setIsPrimary(false);
        ui.setBizStatus("ACTIVE");
        ui.setAcquiredTime(Instant.now());
        ui.setAvailablePoints(defaults);
        ui.setSourceType("TOOL_FIRST_USE");
        userIdentitiesMapper.insert(ui);
        return ui;
    }

    @SuppressWarnings("unchecked")
    private String buildCommentSummary(String subjectName, Map<String, Object> scored) {
        String name = StringUtils.isEmpty(subjectName) ? "未命名" : subjectName.trim();
        Object summary = scored.get("scoreSummary");
        String totals = "";
        if (summary instanceof Map<?, ?> m && m.get("totals") != null) {
            totals = String.valueOf(m.get("totals"));
        }
        return name + " · 潜能测评 " + totals;
    }

    private String toJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o == null ? Map.of() : o);
        } catch (JsonProcessingException e) {
            throw new ServiceException("JSON 序列化失败");
        }
    }

    @SuppressWarnings("unchecked")
    private String readJsonField(String json, String field) {
        if (StringUtils.isEmpty(json)) return null;
        try {
            Map<String, Object> map = objectMapper.readValue(json, Map.class);
            Object v = map.get(field);
            return v == null ? null : String.valueOf(v);
        } catch (Exception e) {
            return null;
        }
    }
}
