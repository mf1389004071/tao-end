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
import com.geek.tao.bt10.domain.KnowledgeContentTags;
import com.geek.tao.bt10.service.IKnowledgeContentTagsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 知识内容与标签多对多关联Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "知识内容与标签多对多关联")
@RestController
@RequestMapping("/bt10/knowledgecontenttags")
public class KnowledgeContentTagsController extends BaseController {

    @Autowired
    private IKnowledgeContentTagsService knowledgeContentTagsService;

    /**
     * 查询知识内容与标签多对多关联列表
     */
    @Operation(summary = "查询知识内容与标签多对多关联列表")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontenttags:list')")
    @GetMapping("/list")
    public TableDataInfo<KnowledgeContentTags> list(KnowledgeContentTags knowledgeContentTags) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<KnowledgeContentTags> list = knowledgeContentTagsService.page(knowledgeContentTags, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出知识内容与标签多对多关联列表
     */
    @Operation(summary = "导出知识内容与标签多对多关联列表")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontenttags:export')")
    @Log(title = "知识内容与标签多对多关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KnowledgeContentTags knowledgeContentTags) {
        knowledgeContentTagsService.export(knowledgeContentTags, response);
    }

    /**
     * 获取知识内容与标签多对多关联详细信息
     */
    @Operation(summary = "获取知识内容与标签多对多关联详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontenttags:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(knowledgeContentTagsService.getById(id));
    }

    /**
     * 新增知识内容与标签多对多关联
     */
    @Operation(summary = "新增知识内容与标签多对多关联")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontenttags:add')")
    @Log(title = "知识内容与标签多对多关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody KnowledgeContentTags knowledgeContentTags) {
        knowledgeContentTags.setCreateBy(getUsername());
        knowledgeContentTags.setCreateId(getUserId());
        return toAjax(knowledgeContentTagsService.save(knowledgeContentTags));
    }

    /**
     * 修改知识内容与标签多对多关联
     */
    @Operation(summary = "修改知识内容与标签多对多关联")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontenttags:edit')")
    @Log(title = "知识内容与标签多对多关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody KnowledgeContentTags knowledgeContentTags) {
        knowledgeContentTags.setUpdateBy(getUsername());
        knowledgeContentTags.setUpdateId(getUserId());
        return toAjax(knowledgeContentTagsService.updateById(knowledgeContentTags));
    }

    /**
     * 删除知识内容与标签多对多关联
     */
    @Operation(summary = "删除知识内容与标签多对多关联")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontenttags:remove')")
    @Log(title = "知识内容与标签多对多关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(knowledgeContentTagsService.removeByIds(ids));
    }
}
