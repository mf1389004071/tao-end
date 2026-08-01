package com.geek.tao.bt10.controller;

import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.utils.SecurityUtils;
import com.geek.tao.bt10.service.ICustKnowledgeToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * C 端知识工具（P6 等）
 */
@RestController
@RequestMapping("/bt10/cust/tool")
public class CustKnowledgeToolController extends BaseController {

    @Autowired
    private ICustKnowledgeToolService custKnowledgeToolService;

    public static class SubmitReq {
        public String contentId;
        public String subjectName;
        public Map<String, Object> answers;
    }

    public static class UnlockProReq {
        public String usageId;
        /** 可选；空则随机本地模板 */
        public String templateKey;
    }

    public static class ClaimReq {
        public String xiaoeOrderNo;
        public String phone;
    }

    private Long requireLoginUserId() {
        if (SecurityUtils.isAnonymous()) {
            return null;
        }
        return SecurityUtils.getUserId();
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

    @GetMapping("/entitlement")
    public AjaxResult entitlement(@RequestParam("contentId") String contentId) {
        Long uid = requireLoginUserId();
        if (uid == null) return error("请先登录");
        Long cid = parseLongOrNull(contentId);
        if (cid == null) return error("contentId 不能为空");
        return success(custKnowledgeToolService.entitlement(uid, cid));
    }

    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody SubmitReq req) {
        Long uid = requireLoginUserId();
        if (uid == null) return error("请先登录");
        Long cid = parseLongOrNull(req == null ? null : req.contentId);
        if (cid == null) return error("contentId 不能为空");
        return success(custKnowledgeToolService.submit(uid, cid,
                req == null ? null : req.subjectName,
                req == null ? null : req.answers));
    }

    @PostMapping("/unlock-pro")
    public AjaxResult unlockPro(@RequestBody UnlockProReq req) {
        Long uid = requireLoginUserId();
        if (uid == null) return error("请先登录");
        Long usageId = parseLongOrNull(req == null ? null : req.usageId);
        if (usageId == null) return error("usageId 不能为空");
        return success(custKnowledgeToolService.unlockPro(uid, usageId,
                req == null ? null : req.templateKey));
    }

    @PostMapping("/claim")
    public AjaxResult claim(@RequestBody ClaimReq req) {
        Long uid = requireLoginUserId();
        if (uid == null) return error("请先登录");
        return success(custKnowledgeToolService.claim(uid,
                req == null ? null : req.xiaoeOrderNo,
                req == null ? null : req.phone));
    }
}
