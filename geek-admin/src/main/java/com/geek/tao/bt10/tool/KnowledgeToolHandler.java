package com.geek.tao.bt10.tool;

import java.util.Map;

/**
 * 知识工具处理器：按 toolCode 注册，负责校验/计分/基础结果。
 */
public interface KnowledgeToolHandler {

    String toolCode();

    /** 校验答案，失败抛 ServiceException */
    void validate(Map<String, Object> answers);

    /**
     * @return scoreSummary / resultBasic 等结构化 Map，由服务层序列化为 JSON 落库
     */
    Map<String, Object> score(Map<String, Object> answers);

    /** 组装喂给 AI 提示词的变量 */
    Map<String, Object> buildAiContext(Map<String, Object> answers, Map<String, Object> scoreResult);
}
