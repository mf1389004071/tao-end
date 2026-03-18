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
import com.geek.tao.bt10.domain.Tags;
import com.geek.tao.bt10.service.ITagsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 通用标签定义表Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "通用标签定义表")
@RestController
@RequestMapping("/bt10/tags")
public class TagsController extends BaseController {

    @Autowired
    private ITagsService tagsService;

    /**
     * 查询通用标签定义表列表
     */
    @Operation(summary = "查询通用标签定义表列表")
    @PreAuthorize("@ss.hasPermi('bt10:tags:list')")
    @GetMapping("/list")
    public TableDataInfo<Tags> list(Tags tags) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<Tags> list = tagsService.page(tags, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出通用标签定义表列表
     */
    @Operation(summary = "导出通用标签定义表列表")
    @PreAuthorize("@ss.hasPermi('bt10:tags:export')")
    @Log(title = "通用标签定义表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Tags tags) {
        tagsService.export(tags, response);
    }

    /**
     * 获取通用标签定义表详细信息
     */
    @Operation(summary = "获取通用标签定义表详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:tags:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tagsService.getById(id));
    }

    /**
     * 新增通用标签定义表
     */
    @Operation(summary = "新增通用标签定义表")
    @PreAuthorize("@ss.hasPermi('bt10:tags:add')")
    @Log(title = "通用标签定义表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Tags tags) {
        return toAjax(tagsService.save(tags));
    }

    /**
     * 修改通用标签定义表
     */
    @Operation(summary = "修改通用标签定义表")
    @PreAuthorize("@ss.hasPermi('bt10:tags:edit')")
    @Log(title = "通用标签定义表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Tags tags) {
        tags.setUpdateBy(getUsername());
        tags.setUpdateId(getUserId());
        return toAjax(tagsService.updateById(tags));
    }

    /**
     * 删除通用标签定义表
     */
    @Operation(summary = "删除通用标签定义表")
    @PreAuthorize("@ss.hasPermi('bt10:tags:remove')")
    @Log(title = "通用标签定义表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(tagsService.removeByIds(ids));
    }
}
