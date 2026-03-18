package com.geek.tao.bt10.controller;

import com.geek.common.annotation.Anonymous;
import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.tao.bt10.common.Enums;
import com.geek.tao.bt10.common.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * bt10 常规 Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "bt10常规请求")
@RestController
@RequestMapping("/bt10/common")
public class Bt10Controller extends BaseController {

    /**
     * 查询下拉框枚举数据列表（匿名可访问，供前端下拉等使用）
     * 仅返回 Enums 中的业务枚举，使用 BT10_EM_KEY 统一缓存。
     */
    @Operation(summary = "查询枚举缓存数据列表")
    @Anonymous
    @GetMapping("/enums")
    public AjaxResult getEnumsCache() {
        return success(Enums.getEnumsCache());
    }

    /**
     * 查询业务状态枚举数据列表（匿名可访问，供前端下拉等使用）
     * 仅返回 Status 中的业务状态枚举，使用 BT10_STATUS_KEY 独立缓存。
     */
    @Operation(summary = "查询状态缓存数据列表")
    @Anonymous
    @GetMapping("/status")
    public AjaxResult getStatusCache() {
        return success(Status.getStatusCache());
    }

}
