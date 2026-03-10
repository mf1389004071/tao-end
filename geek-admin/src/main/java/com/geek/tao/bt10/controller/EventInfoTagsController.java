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
import com.geek.tao.bt10.domain.EventInfoTags;
import com.geek.tao.bt10.service.IEventInfoTagsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 活动与标签多对多关联Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "活动与标签多对多关联")
@RestController
@RequestMapping("/bt10/eventinfotags")
public class EventInfoTagsController extends BaseController {

    @Autowired
    private IEventInfoTagsService eventInfoTagsService;

    /**
     * 查询活动与标签多对多关联列表
     */
    @Operation(summary = "查询活动与标签多对多关联列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfotags:list')")
    @GetMapping("/list")
    public TableDataInfo<EventInfoTags> list(EventInfoTags eventInfoTags) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<EventInfoTags> list = eventInfoTagsService.page(eventInfoTags, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出活动与标签多对多关联列表
     */
    @Operation(summary = "导出活动与标签多对多关联列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfotags:export')")
    @Log(title = "活动与标签多对多关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EventInfoTags eventInfoTags) {
        eventInfoTagsService.export(eventInfoTags, response);
    }

    /**
     * 获取活动与标签多对多关联详细信息
     */
    @Operation(summary = "获取活动与标签多对多关联详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfotags:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(eventInfoTagsService.getById(id));
    }

    /**
     * 新增活动与标签多对多关联
     */
    @Operation(summary = "新增活动与标签多对多关联")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfotags:add')")
    @Log(title = "活动与标签多对多关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody EventInfoTags eventInfoTags) {
        eventInfoTags.setCreateBy(getUsername());
        eventInfoTags.setCreateId(getUserId());
        return toAjax(eventInfoTagsService.save(eventInfoTags));
    }

    /**
     * 修改活动与标签多对多关联
     */
    @Operation(summary = "修改活动与标签多对多关联")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfotags:edit')")
    @Log(title = "活动与标签多对多关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EventInfoTags eventInfoTags) {
        eventInfoTags.setUpdateBy(getUsername());
        eventInfoTags.setUpdateId(getUserId());
        return toAjax(eventInfoTagsService.updateById(eventInfoTags));
    }

    /**
     * 删除活动与标签多对多关联
     */
    @Operation(summary = "删除活动与标签多对多关联")
    @PreAuthorize("@ss.hasPermi('bt10:eventinfotags:remove')")
    @Log(title = "活动与标签多对多关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(eventInfoTagsService.removeByIds(ids));
    }
}
