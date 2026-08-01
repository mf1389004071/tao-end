package com.geek.tao.bt10.tool;

import com.geek.common.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KnowledgeToolHandlerRegistry {

    private final Map<String, KnowledgeToolHandler> byCode = new HashMap<>();

    public KnowledgeToolHandlerRegistry(List<KnowledgeToolHandler> handlers) {
        for (KnowledgeToolHandler h : handlers) {
            byCode.put(h.toolCode(), h);
        }
    }

    public KnowledgeToolHandler require(String toolCode) {
        KnowledgeToolHandler h = byCode.get(toolCode);
        if (h == null) {
            throw new ServiceException("未知工具码: " + toolCode);
        }
        return h;
    }
}
