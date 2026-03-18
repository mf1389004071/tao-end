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
import com.geek.tao.bt10.domain.Notices;
import com.geek.tao.bt10.service.INoticesService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 系统级通知与公告Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "系统级通知与公告")
@RestController
@RequestMapping("/bt10/notices")
public class NoticesController extends BaseController {

    @Autowired
    private INoticesService noticesService;

    /**
     * 查询系统级通知与公告列表
     */
    @Operation(summary = "查询系统级通知与公告列表")
    @PreAuthorize("@ss.hasPermi('bt10:notices:list')")
    @GetMapping("/list")
    public TableDataInfo<Notices> list(Notices notices) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<Notices> list = noticesService.page(notices, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出系统级通知与公告列表
     */
    @Operation(summary = "导出系统级通知与公告列表")
    @PreAuthorize("@ss.hasPermi('bt10:notices:export')")
    @Log(title = "系统级通知与公告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Notices notices) {
        noticesService.export(notices, response);
    }

    /**
     * 获取系统级通知与公告详细信息
     */
    @Operation(summary = "获取系统级通知与公告详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:notices:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(noticesService.getById(id));
    }

    /**
     * 新增系统级通知与公告
     */
    @Operation(summary = "新增系统级通知与公告")
    @PreAuthorize("@ss.hasPermi('bt10:notices:add')")
    @Log(title = "系统级通知与公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Notices notices) {
        return toAjax(noticesService.save(notices));
    }

    /**
     * 修改系统级通知与公告
     */
    @Operation(summary = "修改系统级通知与公告")
    @PreAuthorize("@ss.hasPermi('bt10:notices:edit')")
    @Log(title = "系统级通知与公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Notices notices) {
        notices.setUpdateBy(getUsername());
        notices.setUpdateId(getUserId());
        return toAjax(noticesService.updateById(notices));
    }

    /**
     * 删除系统级通知与公告
     */
    @Operation(summary = "删除系统级通知与公告")
    @PreAuthorize("@ss.hasPermi('bt10:notices:remove')")
    @Log(title = "系统级通知与公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(noticesService.removeByIds(ids));
    }
}
