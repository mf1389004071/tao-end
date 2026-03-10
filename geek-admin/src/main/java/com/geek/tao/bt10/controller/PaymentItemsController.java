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
import com.geek.tao.bt10.domain.PaymentItems;
import com.geek.tao.bt10.service.IPaymentItemsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 统一支付订单明细表Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "统一支付订单明细表")
@RestController
@RequestMapping("/bt10/paymentitems")
public class PaymentItemsController extends BaseController {

    @Autowired
    private IPaymentItemsService paymentItemsService;

    /**
     * 查询统一支付订单明细表列表
     */
    @Operation(summary = "查询统一支付订单明细表列表")
    @PreAuthorize("@ss.hasPermi('bt10:paymentitems:list')")
    @GetMapping("/list")
    public TableDataInfo<PaymentItems> list(PaymentItems paymentItems) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<PaymentItems> list = paymentItemsService.page(paymentItems, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出统一支付订单明细表列表
     */
    @Operation(summary = "导出统一支付订单明细表列表")
    @PreAuthorize("@ss.hasPermi('bt10:paymentitems:export')")
    @Log(title = "统一支付订单明细表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PaymentItems paymentItems) {
        paymentItemsService.export(paymentItems, response);
    }

    /**
     * 获取统一支付订单明细表详细信息
     */
    @Operation(summary = "获取统一支付订单明细表详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:paymentitems:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(paymentItemsService.getById(id));
    }

    /**
     * 新增统一支付订单明细表
     */
    @Operation(summary = "新增统一支付订单明细表")
    @PreAuthorize("@ss.hasPermi('bt10:paymentitems:add')")
    @Log(title = "统一支付订单明细表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PaymentItems paymentItems) {
        paymentItems.setCreateBy(getUsername());
        paymentItems.setCreateId(getUserId());
        return toAjax(paymentItemsService.save(paymentItems));
    }

    /**
     * 修改统一支付订单明细表
     */
    @Operation(summary = "修改统一支付订单明细表")
    @PreAuthorize("@ss.hasPermi('bt10:paymentitems:edit')")
    @Log(title = "统一支付订单明细表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PaymentItems paymentItems) {
        paymentItems.setUpdateBy(getUsername());
        paymentItems.setUpdateId(getUserId());
        return toAjax(paymentItemsService.updateById(paymentItems));
    }

    /**
     * 删除统一支付订单明细表
     */
    @Operation(summary = "删除统一支付订单明细表")
    @PreAuthorize("@ss.hasPermi('bt10:paymentitems:remove')")
    @Log(title = "统一支付订单明细表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(paymentItemsService.removeByIds(ids));
    }
}
