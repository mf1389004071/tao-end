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
import com.geek.tao.bt10.domain.EventInfo;
import com.geek.tao.bt10.service.IEventInfoService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Instant;
import java.util.List;

/**
 * 活动或线下课程主表Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "活动或线下课程主表")
@RestController
@RequestMapping("/bt10/eventinfo")
public class EventInfoController extends BaseController {

    @Autowired
    private IEventInfoService eventInfoService;

    /**
     * 查询活动或线下课程主表列表
     */
    @Operation(summary = "查询活动或线下课程主表列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfo:list')")
    @GetMapping("/list")
    public TableDataInfo<EventInfo> list(EventInfo eventInfo) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<EventInfo> list = eventInfoService.page(eventInfo, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出活动或线下课程主表列表
     */
    @Operation(summary = "导出活动或线下课程主表列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfo:export')")
    @Log(title = "活动或线下课程主表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EventInfo eventInfo) {
        eventInfoService.export(eventInfo, response);
    }

    /**
     * 获取活动或线下课程主表详细信息
     */
    @Operation(summary = "获取活动或线下课程主表详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfo:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(eventInfoService.getInfo(id));
    }

    /**
     * 新增活动或线下课程主表
     */
    @Operation(summary = "新增活动或线下课程主表")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfo:add')")
    @Log(title = "活动或线下课程主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody EventInfo eventInfo) {
        return toAjax(eventInfoService.save(eventInfo));
    }

    /**
     * 修改活动或线下课程主表
     */
    @Operation(summary = "修改活动或线下课程主表")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfo:edit')")
    @Log(title = "活动或线下课程主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EventInfo eventInfo) {
        return toAjax(eventInfoService.updateById(eventInfo));
    }

    /**
     * 删除活动或线下课程主表
     */
    @Operation(summary = "删除活动或线下课程主表")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfo:remove')")
    @Log(title = "活动或线下课程主表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(eventInfoService.removeByIds(ids));
    }

    /**
     * 更新活动或线下课程主表业务状态：DRAFT -> PUBLISHED
     */
    @Operation(summary = "获取活动或线下课程主表详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfo:edit')")
    @Log(title = "活动或线下课程主表", businessType = BusinessType.UPDATE)
    @GetMapping("/{ids}/published")
    public AjaxResult updateBizStatusPublished(@PathVariable("ids") List<Long> ids) {
        //TODO 
        List<EventInfo> eventInfos = eventInfoService.listByIds(ids);
        if(eventInfos==null || ids.size() != eventInfos.size()){
            return error("出现无效的活动ID列表");
        }
        long eventInfosSize = eventInfos.stream().filter(m -> "DRAFT".equals(m.getBizStatus())).toList().size();
        if(ids.size() != eventInfosSize){
            return error("出现无效的活动状态列表");
        }
        eventInfos.forEach(m -> {m.setBizStatus("PUBLISHED");});
        return toAjax(eventInfoService.updateBatch(eventInfos));
    }

}
