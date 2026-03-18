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
import com.geek.tao.bt10.domain.Notifications;
import com.geek.tao.bt10.service.INotificationsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户站内通知Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "用户站内通知")
@RestController
@RequestMapping("/bt10/notifications")
public class NotificationsController extends BaseController {

    @Autowired
    private INotificationsService notificationsService;

    /**
     * 查询用户站内通知列表
     */
    @Operation(summary = "查询用户站内通知列表")
    @PreAuthorize("@ss.hasPermi('bt10:notifications:list')")
    @GetMapping("/list")
    public TableDataInfo<Notifications> list(Notifications notifications) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<Notifications> list = notificationsService.page(notifications, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出用户站内通知列表
     */
    @Operation(summary = "导出用户站内通知列表")
    @PreAuthorize("@ss.hasPermi('bt10:notifications:export')")
    @Log(title = "用户站内通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Notifications notifications) {
        notificationsService.export(notifications, response);
    }

    /**
     * 获取用户站内通知详细信息
     */
    @Operation(summary = "获取用户站内通知详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:notifications:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(notificationsService.getById(id));
    }

    /**
     * 新增用户站内通知
     */
    @Operation(summary = "新增用户站内通知")
    @PreAuthorize("@ss.hasPermi('bt10:notifications:add')")
    @Log(title = "用户站内通知", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Notifications notifications) {
        return toAjax(notificationsService.save(notifications));
    }

    /**
     * 修改用户站内通知
     */
    @Operation(summary = "修改用户站内通知")
    @PreAuthorize("@ss.hasPermi('bt10:notifications:edit')")
    @Log(title = "用户站内通知", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Notifications notifications) {
        notifications.setUpdateBy(getUsername());
        notifications.setUpdateId(getUserId());
        return toAjax(notificationsService.updateById(notifications));
    }

    /**
     * 删除用户站内通知
     */
    @Operation(summary = "删除用户站内通知")
    @PreAuthorize("@ss.hasPermi('bt10:notifications:remove')")
    @Log(title = "用户站内通知", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(notificationsService.removeByIds(ids));
    }
}
