package com.geek.tao.bt10.tool;

import com.geek.common.exception.ServiceException;
import com.geek.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从 classpath:tool-templates/{toolCode}/*.html 加载多套高级结果模板，支持随机选取与占位符渲染。
 */
@Service
public class ToolProTemplateService {

    private static final Logger log = LoggerFactory.getLogger(ToolProTemplateService.class);
    private static final Pattern SLOT = Pattern.compile("\\{\\{\\s*([a-zA-Z0-9_.-]+)\\s*\\}\\}");

    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private final ConcurrentHashMap<String, Map<String, String>> cache = new ConcurrentHashMap<>();

    public List<String> listKeys(String toolCode) {
        return new ArrayList<>(loadAll(toolCode).keySet());
    }

    public String pickRandomKey(String toolCode) {
        List<String> keys = listKeys(toolCode);
        if (keys.isEmpty()) {
            throw new ServiceException("未找到工具模板: " + toolCode);
        }
        return keys.get(ThreadLocalRandom.current().nextInt(keys.size()));
    }

    public String loadRaw(String toolCode, String templateKey) {
        Map<String, String> all = loadAll(toolCode);
        String html = all.get(templateKey);
        if (html == null) {
            html = all.get("default");
        }
        if (html == null && !all.isEmpty()) {
            html = all.values().iterator().next();
        }
        if (html == null) {
            throw new ServiceException("模板不存在: " + toolCode + "/" + templateKey);
        }
        return html;
    }

    public String render(String toolCode, String templateKey, Map<String, ?> slots) {
        String raw = loadRaw(toolCode, templateKey);
        if (slots == null || slots.isEmpty()) {
            return raw;
        }
        Matcher m = SLOT.matcher(raw);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String key = m.group(1);
            Object val = slots.get(key);
            String rep = val == null ? "" : Matcher.quoteReplacement(String.valueOf(val));
            m.appendReplacement(sb, rep);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private Map<String, String> loadAll(String toolCode) {
        String code = StringUtils.isEmpty(toolCode) ? "POWER6" : toolCode;
        return cache.computeIfAbsent(code, this::scanClasspath);
    }

    private Map<String, String> scanClasspath(String toolCode) {
        Map<String, String> map = new LinkedHashMap<>();
        try {
            Resource[] resources = resolver.getResources("classpath*:tool-templates/" + toolCode + "/*.html");
            List<Resource> list = new ArrayList<>(List.of(resources));
            list.sort(Comparator.comparing(r -> {
                try {
                    return r.getFilename() == null ? "" : r.getFilename();
                } catch (Exception e) {
                    return "";
                }
            }));
            for (Resource r : list) {
                String name = r.getFilename();
                if (name == null || !name.endsWith(".html")) continue;
                String key = name.substring(0, name.length() - 5);
                String html = new String(r.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                map.put(key, html);
            }
        } catch (IOException e) {
            log.error("加载工具模板失败 toolCode={}", toolCode, e);
            throw new ServiceException("加载工具模板失败");
        }
        if (map.isEmpty()) {
            log.warn("classpath 无模板 tool-templates/{}/", toolCode);
        }
        return map;
    }

    /** 测试/热更新时可清缓存 */
    public void evict(String toolCode) {
        if (toolCode == null) cache.clear();
        else cache.remove(toolCode);
    }
}
