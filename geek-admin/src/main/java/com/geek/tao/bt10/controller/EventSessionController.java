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
import com.geek.tao.bt10.domain.EventSession;
import com.geek.tao.bt10.service.IEventSessionService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 周期活动的单场次Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "周期活动的单场次")
@RestController
@RequestMapping("/bt10/eventsession")
public class EventSessionController extends BaseController {

    @Autowired
    private IEventSessionService eventSessionService;

    /**
     * 查询周期活动的单场次列表
     */
    @Operation(summary = "查询周期活动的单场次列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventsession:list')")
    @GetMapping("/list")
    public TableDataInfo<EventSession> list(EventSession eventSession) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<EventSession> list = eventSessionService.page(eventSession, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出周期活动的单场次列表
     */
    @Operation(summary = "导出周期活动的单场次列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventsession:export')")
    @Log(title = "周期活动的单场次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EventSession eventSession) {
        eventSessionService.export(eventSession, response);
    }

    /**
     * 获取周期活动的单场次详细信息
     */
    @Operation(summary = "获取周期活动的单场次详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:eventsession:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(eventSessionService.getById(id));
    }

    /**
     * 新增周期活动的单场次
     */
    @Operation(summary = "新增周期活动的单场次")
    @PreAuthorize("@ss.hasPermi('bt10:eventsession:add')")
    @Log(title = "周期活动的单场次", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody EventSession eventSession) {
        return toAjax(eventSessionService.save(eventSession));
    }

    /**
     * 修改周期活动的单场次
     */
    @Operation(summary = "修改周期活动的单场次")
    @PreAuthorize("@ss.hasPermi('bt10:eventsession:edit')")
    @Log(title = "周期活动的单场次", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EventSession eventSession) {
        eventSession.setUpdateBy(getUsername());
        eventSession.setUpdateId(getUserId());
        return toAjax(eventSessionService.updateById(eventSession));
    }

    /**
     * 删除周期活动的单场次
     */
    @Operation(summary = "删除周期活动的单场次")
    @PreAuthorize("@ss.hasPermi('bt10:eventsession:remove')")
    @Log(title = "周期活动的单场次", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(eventSessionService.removeByIds(ids));
    }
}
