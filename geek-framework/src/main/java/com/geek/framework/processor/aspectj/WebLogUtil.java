package com.geek.framework.processor.aspectj;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geek.common.utils.JSON;
import com.geek.common.utils.ServletUtils;
import com.geek.common.utils.StringUtils;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

/**
 * Web 接口请求/响应日志工具
 *
 * 只用于调试与排查问题，默认按 info 级别输出到控制台。
 */
public class WebLogUtil {

    private static final Logger log = LoggerFactory.getLogger(WebLogUtil.class);

    /**
     * 方法描述缓存，避免重复反射
     */
    private static final ConcurrentHashMap<String, String> METHOD_DESC_CACHE = new ConcurrentHashMap<>();

    /**
     * 记录请求日志（URL、入参等）
     */
    public static void requestLog(org.aspectj.lang.JoinPoint joinPoint) {
        HttpServletRequest request;
        try {
            request = ServletUtils.getRequest();
        } catch (IllegalStateException ex) {
            // 非 Web 请求线程，直接返回
            return;
        }

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        List<Object> argList = new ArrayList<>();
        if (args != null) {
            for (Object item : args) {
                if (item instanceof ServletRequest || item instanceof ServletResponse || item instanceof MultipartFile) {
                    continue;
                }
                argList.add(item);
            }
        }

        String methodDesc = getMethodDesc(className, methodName, args != null ? args.length : 0);
        String paramJson;
        try {
            paramJson = JSON.toJSONString(argList);
        } catch (Exception e) {
            paramJson = "[入参序列化异常:" + e.getMessage() + "]";
        }

        String httpMethod = request.getMethod();
        String requestUri = StringUtils.substring(request.getRequestURI(), 0, 255);
        String queryString = request.getQueryString();

        String params = paramJson;
        if ("GET".equalsIgnoreCase(httpMethod) && StringUtils.isNotEmpty(queryString)) {
            try {
                String decoded = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
                params = formatQueryString(decoded);
            } catch (Exception e) {
                params = "[解码查询参数异常:" + e.getMessage() + "]";
            }
        }

        StringBuilder sb = new StringBuilder(256);
        sb.append('\n')
          .append("**************** Http Request ******************\n")
          .append("Description    : ").append(StringUtils.defaultString(methodDesc)).append('\n')
          .append("RequestUri     : [").append(httpMethod).append("] ").append(requestUri).append('\n')
          .append("RequestParams  : ").append(params).append('\n');

        if (log.isDebugEnabled()) {
            sb.append("ClassName      : ").append(className).append('\n')
              .append("RequestMethod  : ").append(methodName).append('\n')
              .append("ContentType    : ").append(Objects.toString(request.getContentType(), "")).append('\n')
              .append("UserAgent      : ").append(Objects.toString(request.getHeader("User-Agent"), "")).append('\n');
        }

        log.info(sb.toString());
    }

    /**
     * 记录响应日志（返回值、耗时等）
     */
    public static void responseLog(Object ret, long startTimeMillis) {
        HttpServletRequest request;
        try {
            request = ServletUtils.getRequest();
        } catch (IllegalStateException ex) {
            return;
        }

        String responseStr;
        try {
            responseStr = JSON.toJSONString(ret);
        } catch (Exception e) {
            responseStr = "[响应序列化异常:" + e.getMessage() + "]";
        }

        // 简单做一下长度保护，避免日志过长
        final int maxLength = 2000;
        if (responseStr != null && responseStr.length() > maxLength) {
            responseStr = responseStr.substring(0, maxLength) + "...";
        }

        StringBuilder sb = new StringBuilder(256);
        sb.append('\n')
          .append("**************** Http Response *****************\n")
          .append("RequestUri     : ").append(StringUtils.substring(request.getRequestURI(), 0, 255)).append('\n')
          .append("Response       : ").append(responseStr).append('\n')
          .append("CostTime       : ").append(System.currentTimeMillis() - startTimeMillis).append(" ms\n");

        log.info(sb.toString());
    }

    /**
     * 记录异常日志
     */
    public static void throwableLog(Throwable ex, long startTimeMillis) {
        HttpServletRequest request = null;
        try {
            request = ServletUtils.getRequest();
        } catch (IllegalStateException ignored) {
        }

        StringBuilder sb = new StringBuilder(256);
        sb.append('\n')
          .append("**************** Http Exception ****************\n");

        if (request != null) {
            sb.append("RequestUri     : ").append(StringUtils.substring(request.getRequestURI(), 0, 255)).append('\n');
        }

        sb.append("CostTime       : ").append(System.currentTimeMillis() - startTimeMillis).append(" ms\n")
          .append("Exception      : ").append(ex.getClass().getName()).append('\n')
          .append("ExceptionMsg   : ").append(Objects.toString(ex.getMessage(), "[No Message]")).append('\n');

        log.error(sb.toString(), ex);
    }

    /**
     * 从 @Operation 注解里取接口中文描述
     */
    private static String getMethodDesc(String className, String methodName, int argLen) {
        String key = className + "." + methodName + "." + argLen;
        return METHOD_DESC_CACHE.computeIfAbsent(key, k -> {
            try {
                Class<?> targetClass = Class.forName(className);
                for (Method method : targetClass.getMethods()) {
                    if (method.getName().equals(methodName) && method.getParameterCount() == argLen) {
                        Operation apiOperation = method.getAnnotation(Operation.class);
                        return apiOperation != null ? apiOperation.summary() : "";
                    }
                }
            } catch (ClassNotFoundException e) {
                log.warn("解析方法描述失败, className={}", className, e);
            }
            return "";
        });
    }

    /**
     * 把 GET 查询串转成 JSON 形式字符串，便于阅读
     */
    private static String formatQueryString(String queryString) {
        if (StringUtils.isEmpty(queryString)) {
            return "{}";
        }
        String[] pairs = queryString.split("&");
        Map<String, String> paramMap = new java.util.HashMap<>(pairs.length);
        for (String pair : pairs) {
            if (StringUtils.isEmpty(pair)) {
                continue;
            }
            int idx = pair.indexOf('=');
            if (idx <= 0 || idx == pair.length() - 1) {
                continue;
            }
            String key = pair.substring(0, idx);
            String value = pair.substring(idx + 1);
            paramMap.put(key, value);
        }
        try {
            return JSON.toJSONString(paramMap);
        } catch (Exception e) {
            return "[查询参数序列化异常:" + e.getMessage() + "]";
        }
    }

    private WebLogUtil() {
    }
}

