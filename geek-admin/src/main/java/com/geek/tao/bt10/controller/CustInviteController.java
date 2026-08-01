package com.geek.tao.bt10.controller;

import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.utils.SecurityUtils;
import com.geek.tao.bt10.service.ICustInviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * C 端邀请绑定（新用户 → 邀请人身份域次数 +1）
 */
@RestController
@RequestMapping("/bt10/cust/invite")
public class CustInviteController extends BaseController {

    @Autowired
    private ICustInviteService custInviteService;

    public static class BindReq {
        public String inviterId;
        public String identityCode;
    }

    @PostMapping("/bind")
    public AjaxResult bind(@RequestBody BindReq req) {
        if (SecurityUtils.isAnonymous()) return error("请先登录");
        Long inviterId = parseLongOrNull(req == null ? null : req.inviterId);
        if (inviterId == null) return error("inviterId 不能为空");
        return success(custInviteService.bind(SecurityUtils.getUserId(), inviterId,
                req == null ? null : req.identityCode));
    }

    private static Long parseLongOrNull(String v) {
        if (v == null) return null;
        String s = v.trim();
        if (s.isEmpty()) return null;
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return null;
        }
    }
}
