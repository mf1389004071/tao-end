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
import com.geek.tao.bt10.domain.XiaoeOrders;
import com.geek.tao.bt10.service.IXiaoeOrdersService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 小鹅通订单同步表Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "小鹅通订单同步表")
@RestController
@RequestMapping("/bt10/xiaoeorders")
public class XiaoeOrdersController extends BaseController {

    @Autowired
    private IXiaoeOrdersService xiaoeOrdersService;

    /**
     * 查询小鹅通订单同步表列表
     */
    @Operation(summary = "查询小鹅通订单同步表列表")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeorders:list')")
    @GetMapping("/list")
    public TableDataInfo<XiaoeOrders> list(XiaoeOrders xiaoeOrders) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<XiaoeOrders> list = xiaoeOrdersService.page(xiaoeOrders, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出小鹅通订单同步表列表
     */
    @Operation(summary = "导出小鹅通订单同步表列表")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeorders:export')")
    @Log(title = "小鹅通订单同步表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XiaoeOrders xiaoeOrders) {
        xiaoeOrdersService.export(xiaoeOrders, response);
    }

    /**
     * 获取小鹅通订单同步表详细信息
     */
    @Operation(summary = "获取小鹅通订单同步表详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeorders:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(xiaoeOrdersService.getById(id));
    }

    /**
     * 新增小鹅通订单同步表
     */
    @Operation(summary = "新增小鹅通订单同步表")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeorders:add')")
    @Log(title = "小鹅通订单同步表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody XiaoeOrders xiaoeOrders) {
        xiaoeOrders.setCreateBy(getUsername());
        xiaoeOrders.setCreateId(getUserId());
        return toAjax(xiaoeOrdersService.save(xiaoeOrders));
    }

    /**
     * 修改小鹅通订单同步表
     */
    @Operation(summary = "修改小鹅通订单同步表")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeorders:edit')")
    @Log(title = "小鹅通订单同步表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody XiaoeOrders xiaoeOrders) {
        xiaoeOrders.setUpdateBy(getUsername());
        xiaoeOrders.setUpdateId(getUserId());
        return toAjax(xiaoeOrdersService.updateById(xiaoeOrders));
    }

    /**
     * 删除小鹅通订单同步表
     */
    @Operation(summary = "删除小鹅通订单同步表")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeorders:remove')")
    @Log(title = "小鹅通订单同步表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(xiaoeOrdersService.removeByIds(ids));
    }
}
