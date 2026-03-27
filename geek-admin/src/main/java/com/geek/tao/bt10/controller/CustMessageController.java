package com.geek.tao.bt10.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.core.page.PageDomain;
import com.geek.common.core.page.TableDataInfo;
import com.geek.common.core.page.TableSupport;
import com.geek.common.utils.SecurityUtils;
import com.geek.tao.bt10.service.ICustMessageService;
import com.mybatisflex.core.paginate.Page;

/**
 * C 端私信：会话与消息（业务逻辑见 {@link com.geek.tao.bt10.service.ICustMessageService}）
 */
@RestController
@RequestMapping("/bt10/cust/message")
public class CustMessageController extends BaseController {

    @Autowired
    private ICustMessageService custMessageService;

    private Long requireLoginUserId() {
        if (SecurityUtils.isAnonymous()) {
            return null;
        }
        return SecurityUtils.getUserId();
    }

    private static Long parseLong(String v) {
        if (v == null || v.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(v.trim());
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/thread/ensure")
    public AjaxResult ensureThread(@RequestBody Map<String, Object> body) {
        Long me = requireLoginUserId();
        if (me == null) {
            return error("请先登录");
        }
        Long target = parseLong(body == null ? null : String.valueOf(body.get("targetUserId")));
        if (target == null) {
            return error("targetUserId 不能为空");
        }
        try {
            Long threadId = custMessageService.ensureThread(me, target);
            Map<String, Object> data = new HashMap<>();
            data.put("threadId", String.valueOf(threadId));
            return success(data);
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @GetMapping("/thread/list")
    public TableDataInfo<Map<String, Object>> listThreads() {
        Long me = requireLoginUserId();
        if (me == null) {
            return getDataTable(Collections.emptyList());
        }
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<Map<String, Object>> page = custMessageService.pageMyThreads(me, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(page);
    }

    @GetMapping("/list")
    public TableDataInfo<Map<String, Object>> listMessages(Long threadId) {
        Long me = requireLoginUserId();
        if (me == null) {
            return getDataTable(Collections.emptyList());
        }
        if (threadId == null) {
            return getDataTable(Collections.emptyList());
        }
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<Map<String, Object>> page = custMessageService.pageMessages(me, threadId, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(page);
    }

    @PostMapping("/send")
    public AjaxResult send(@RequestBody Map<String, Object> body) {
        Long me = requireLoginUserId();
        if (me == null) {
            return error("请先登录");
        }
        Long threadId = parseLong(body == null ? null : String.valueOf(body.get("threadId")));
        String content = body == null || body.get("content") == null ? "" : String.valueOf(body.get("content")).trim();
        if (threadId == null) {
            return error("threadId 不能为空");
        }
        if (content.isEmpty()) {
            return error("消息内容不能为空");
        }
        try {
            custMessageService.sendText(me, threadId, content);
            return success();
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @PostMapping("/read")
    public AjaxResult markRead(@RequestBody Map<String, Object> body) {
        Long me = requireLoginUserId();
        if (me == null) {
            return error("请先登录");
        }
        Long threadId = parseLong(body == null ? null : String.valueOf(body.get("threadId")));
        if (threadId == null) {
            return error("threadId 不能为空");
        }
        try {
            custMessageService.markRead(me, threadId);
            return success();
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }
}
