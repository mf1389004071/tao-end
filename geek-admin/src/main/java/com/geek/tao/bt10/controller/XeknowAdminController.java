package com.geek.tao.bt10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geek.common.annotation.Anonymous;
import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.tao.bt10.domain.XeknowReportEncryptedReq;
import com.geek.tao.bt10.service.IXeknowAdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * xeknow 采集上报入口（油猴 → 本平台）
 */
@Tag(name = "xeknow采集上报")
@RestController
@RequestMapping("/bt10/xeknow")
public class XeknowAdminController extends BaseController {

    @Autowired
    private IXeknowAdminService xeknowAdminService;

    @Anonymous
    @Operation(summary = "接收 xeknow 加密上报")
    @PostMapping("/report")
    public AjaxResult report(@RequestBody XeknowReportEncryptedReq req) {
        return success(xeknowAdminService.ingestReport(req));
    }

    @Anonymous
    @Operation(summary = "下发缺手机号的小鹅通用户ID列表")
    @GetMapping("/umissing")
    public AjaxResult umissing(@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        return success(xeknowAdminService.listPhoneMissingUserIds(limit == null ? 10 : limit));
    }
}
