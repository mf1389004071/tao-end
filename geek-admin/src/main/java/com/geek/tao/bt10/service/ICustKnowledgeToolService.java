package com.geek.tao.bt10.service;

import java.util.Map;

/**
 * C 端知识工具：提交测评、权益、高级解读、兑换
 */
public interface ICustKnowledgeToolService {

    Map<String, Object> entitlement(Long userId, Long contentId);

    Map<String, Object> submit(Long userId, Long contentId, String subjectName, Map<String, Object> answers);

    /**
     * 高级解读：已有 result_pro 直接返回；否则调 AI，成功后写库并按规则扣次。
     * @param templateKey 可选，指定模板；空则随机选一套本地 HTML 模板
     */
    Map<String, Object> unlockPro(Long userId, Long usageId, String templateKey);

    /**
     * 小鹅通单号 + 手机兑换：匹配待兑登单 → 写 payment → 按产品 validDays 授/延期身份。
     */
    Map<String, Object> claim(Long userId, String xiaoeOrderNo, String phone);
}
