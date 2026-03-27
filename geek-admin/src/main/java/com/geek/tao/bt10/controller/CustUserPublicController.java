package com.geek.tao.bt10.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.utils.SecurityUtils;
import com.geek.tao.bt10.service.ICustUserPublicService;

/**
 * C 端用户公开主页（业务逻辑见 {@link com.geek.tao.bt10.service.ICustUserPublicService}）
 */
@RestController
@RequestMapping("/bt10/cust/user")
public class CustUserPublicController extends BaseController {

    @Autowired
    private ICustUserPublicService custUserPublicService;

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

    @GetMapping("/public")
    public AjaxResult getPublic(@RequestParam("userId") String userIdStr) {
        Long userId = parseLong(userIdStr);
        if (userId == null) {
            return error("userId 不能为空");
        }
        try {
            Long me = requireLoginUserId();
            Map<String, Object> data = custUserPublicService.getPublicProfile(userId, me);
            return success(data);
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    public static class ToggleReq {
        public String targetUserId;
        public String actionType;
        public Boolean enabled;
    }

    @PostMapping("/social/toggle")
    public AjaxResult toggle(@RequestBody ToggleReq req) {
        Long me = requireLoginUserId();
        if (me == null) {
            return error("请先登录");
        }
        Long target = parseLong(req == null ? null : req.targetUserId);
        if (target == null) {
            return error("targetUserId 不能为空");
        }
        try {
            Map<String, Object> data = custUserPublicService.toggleSocial(target, me, req.actionType, req.enabled);
            return success(data);
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }

    @PostMapping("/social/share")
    public AjaxResult share(@RequestBody Map<String, Object> body) {
        Long me = requireLoginUserId();
        if (me == null) {
            return error("请先登录");
        }
        Long target = parseLong(body == null ? null : String.valueOf(body.get("targetUserId")));
        if (target == null) {
            return error("targetUserId 不能为空");
        }
        try {
            Map<String, Object> data = custUserPublicService.recordShare(target, me);
            return success(data);
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }
    }
}
