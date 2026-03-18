package com.geek.tao.bt10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geek.common.annotation.Log;
import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.core.page.PageDomain;
import com.geek.common.core.page.TableDataInfo;
import com.geek.common.core.page.TableSupport;
import com.geek.common.enums.BusinessType;
import com.geek.tao.bt10.domain.PaymentInfo;
import com.geek.tao.bt10.service.IPaymentInfoService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 统一支付订单Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "统一支付订单")
@RestController
@RequestMapping("/bt10/paymentinfo")
public class PaymentInfoController extends BaseController {

    @Autowired
    private IPaymentInfoService paymentInfoService;

    /**
     * 查询统一支付订单列表
     */
    @Operation(summary = "查询统一支付订单列表")
    @PreAuthorize("@ss.hasPermi('bt10:paymentinfo:list')")
    @GetMapping("/list")
    public TableDataInfo<PaymentInfo> list(PaymentInfo paymentInfo) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<PaymentInfo> list = paymentInfoService.page(paymentInfo, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出统一支付订单列表
     */
    @Operation(summary = "导出统一支付订单列表")
    @PreAuthorize("@ss.hasPermi('bt10:paymentinfo:export')")
    @Log(title = "统一支付订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PaymentInfo paymentInfo) {
        paymentInfoService.export(paymentInfo, response);
    }

    /**
     * 获取统一支付订单详细信息
     */
    @Operation(summary = "获取统一支付订单详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:paymentinfo:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(paymentInfoService.getById(id));
    }

    /**
     * 新增统一支付订单
     */
    @Operation(summary = "新增统一支付订单")
    @PreAuthorize("@ss.hasPermi('bt10:paymentinfo:add')")
    @Log(title = "统一支付订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PaymentInfo paymentInfo) {
        return toAjax(paymentInfoService.save(paymentInfo));
    }

    /**
     * 修改统一支付订单
     */
    @Operation(summary = "修改统一支付订单")
    @PreAuthorize("@ss.hasPermi('bt10:paymentinfo:edit')")
    @Log(title = "统一支付订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PaymentInfo paymentInfo) {
        paymentInfo.setUpdateBy(getUsername());
        paymentInfo.setUpdateId(getUserId());
        return toAjax(paymentInfoService.updateById(paymentInfo));
    }

    /**
     * 删除统一支付订单
     */
    @Operation(summary = "删除统一支付订单")
    @PreAuthorize("@ss.hasPermi('bt10:paymentinfo:remove')")
    @Log(title = "统一支付订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(paymentInfoService.removeByIds(ids));
    }
}
