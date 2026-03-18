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
import com.geek.tao.bt10.domain.EventCheckIn;
import com.geek.tao.bt10.service.IEventCheckInService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 单次签到记录Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "单次签到记录")
@RestController
@RequestMapping("/bt10/eventcheckin")
public class EventCheckInController extends BaseController {

    @Autowired
    private IEventCheckInService eventCheckInService;

    /**
     * 查询单次签到记录列表
     */
    @Operation(summary = "查询单次签到记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventcheckin:list')")
    @GetMapping("/list")
    public TableDataInfo<EventCheckIn> list(EventCheckIn eventCheckIn) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<EventCheckIn> list = eventCheckInService.page(eventCheckIn, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出单次签到记录列表
     */
    @Operation(summary = "导出单次签到记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventcheckin:export')")
    @Log(title = "单次签到记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EventCheckIn eventCheckIn) {
        eventCheckInService.export(eventCheckIn, response);
    }

    /**
     * 获取单次签到记录详细信息
     */
    @Operation(summary = "获取单次签到记录详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:eventcheckin:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(eventCheckInService.getById(id));
    }

    /**
     * 新增单次签到记录
     */
    @Operation(summary = "新增单次签到记录")
    @PreAuthorize("@ss.hasPermi('bt10:eventcheckin:add')")
    @Log(title = "单次签到记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody EventCheckIn eventCheckIn) {
        return toAjax(eventCheckInService.save(eventCheckIn));
    }

    /**
     * 修改单次签到记录
     */
    @Operation(summary = "修改单次签到记录")
    @PreAuthorize("@ss.hasPermi('bt10:eventcheckin:edit')")
    @Log(title = "单次签到记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EventCheckIn eventCheckIn) {
        eventCheckIn.setUpdateBy(getUsername());
        eventCheckIn.setUpdateId(getUserId());
        return toAjax(eventCheckInService.updateById(eventCheckIn));
    }

    /**
     * 删除单次签到记录
     */
    @Operation(summary = "删除单次签到记录")
    @PreAuthorize("@ss.hasPermi('bt10:eventcheckin:remove')")
    @Log(title = "单次签到记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(eventCheckInService.removeByIds(ids));
    }
}
