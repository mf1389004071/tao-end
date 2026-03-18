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
import com.geek.tao.bt10.domain.UserChangeLog;
import com.geek.tao.bt10.service.IUserChangeLogService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户关键字段变更记录Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "用户关键字段变更记录")
@RestController
@RequestMapping("/bt10/userchangelog")
public class UserChangeLogController extends BaseController {

    @Autowired
    private IUserChangeLogService userChangeLogService;

    /**
     * 查询用户关键字段变更记录列表
     */
    @Operation(summary = "查询用户关键字段变更记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:userchangelog:list')")
    @GetMapping("/list")
    public TableDataInfo<UserChangeLog> list(UserChangeLog userChangeLog) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<UserChangeLog> list = userChangeLogService.page(userChangeLog, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出用户关键字段变更记录列表
     */
    @Operation(summary = "导出用户关键字段变更记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:userchangelog:export')")
    @Log(title = "用户关键字段变更记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserChangeLog userChangeLog) {
        userChangeLogService.export(userChangeLog, response);
    }

    /**
     * 获取用户关键字段变更记录详细信息
     */
    @Operation(summary = "获取用户关键字段变更记录详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:userchangelog:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userChangeLogService.getById(id));
    }

    /**
     * 新增用户关键字段变更记录
     */
    @Operation(summary = "新增用户关键字段变更记录")
    @PreAuthorize("@ss.hasPermi('bt10:userchangelog:add')")
    @Log(title = "用户关键字段变更记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UserChangeLog userChangeLog) {
        return toAjax(userChangeLogService.save(userChangeLog));
    }

    /**
     * 修改用户关键字段变更记录
     */
    @Operation(summary = "修改用户关键字段变更记录")
    @PreAuthorize("@ss.hasPermi('bt10:userchangelog:edit')")
    @Log(title = "用户关键字段变更记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UserChangeLog userChangeLog) {
        userChangeLog.setUpdateBy(getUsername());
        userChangeLog.setUpdateId(getUserId());
        return toAjax(userChangeLogService.updateById(userChangeLog));
    }

    /**
     * 删除用户关键字段变更记录
     */
    @Operation(summary = "删除用户关键字段变更记录")
    @PreAuthorize("@ss.hasPermi('bt10:userchangelog:remove')")
    @Log(title = "用户关键字段变更记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(userChangeLogService.removeByIds(ids));
    }
}
