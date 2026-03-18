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
import com.geek.tao.bt10.domain.EventChangeLogs;
import com.geek.tao.bt10.service.IEventChangeLogsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 活动关键信息变更记录Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "活动关键信息变更记录")
@RestController
@RequestMapping("/bt10/eventchangelogs")
public class EventChangeLogsController extends BaseController {

    @Autowired
    private IEventChangeLogsService eventChangeLogsService;

    /**
     * 查询活动关键信息变更记录列表
     */
    @Operation(summary = "查询活动关键信息变更记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventchangelogs:list')")
    @GetMapping("/list")
    public TableDataInfo<EventChangeLogs> list(EventChangeLogs eventChangeLogs) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<EventChangeLogs> list = eventChangeLogsService.page(eventChangeLogs, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出活动关键信息变更记录列表
     */
    @Operation(summary = "导出活动关键信息变更记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventchangelogs:export')")
    @Log(title = "活动关键信息变更记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EventChangeLogs eventChangeLogs) {
        eventChangeLogsService.export(eventChangeLogs, response);
    }

    /**
     * 获取活动关键信息变更记录详细信息
     */
    @Operation(summary = "获取活动关键信息变更记录详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:eventchangelogs:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(eventChangeLogsService.getById(id));
    }

    /**
     * 新增活动关键信息变更记录
     */
    @Operation(summary = "新增活动关键信息变更记录")
    @PreAuthorize("@ss.hasPermi('bt10:eventchangelogs:add')")
    @Log(title = "活动关键信息变更记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody EventChangeLogs eventChangeLogs) {
        return toAjax(eventChangeLogsService.save(eventChangeLogs));
    }

    /**
     * 修改活动关键信息变更记录
     */
    @Operation(summary = "修改活动关键信息变更记录")
    @PreAuthorize("@ss.hasPermi('bt10:eventchangelogs:edit')")
    @Log(title = "活动关键信息变更记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EventChangeLogs eventChangeLogs) {
        eventChangeLogs.setUpdateBy(getUsername());
        eventChangeLogs.setUpdateId(getUserId());
        return toAjax(eventChangeLogsService.updateById(eventChangeLogs));
    }

    /**
     * 删除活动关键信息变更记录
     */
    @Operation(summary = "删除活动关键信息变更记录")
    @PreAuthorize("@ss.hasPermi('bt10:eventchangelogs:remove')")
    @Log(title = "活动关键信息变更记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(eventChangeLogsService.removeByIds(ids));
    }
}
