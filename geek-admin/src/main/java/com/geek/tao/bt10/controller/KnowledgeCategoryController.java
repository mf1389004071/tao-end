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
import com.geek.tao.bt10.domain.KnowledgeCategory;
import com.geek.tao.bt10.service.IKnowledgeCategoryService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 知识库分类Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "知识库分类")
@RestController
@RequestMapping("/bt10/knowledgecategory")
public class KnowledgeCategoryController extends BaseController {

    @Autowired
    private IKnowledgeCategoryService knowledgeCategoryService;

    /**
     * 查询知识库分类列表
     */
    @Operation(summary = "查询知识库分类列表")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecategory:list')")
    @GetMapping("/list")
    public TableDataInfo<KnowledgeCategory> list(KnowledgeCategory knowledgeCategory) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<KnowledgeCategory> list = knowledgeCategoryService.page(knowledgeCategory, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出知识库分类列表
     */
    @Operation(summary = "导出知识库分类列表")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecategory:export')")
    @Log(title = "知识库分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KnowledgeCategory knowledgeCategory) {
        knowledgeCategoryService.export(knowledgeCategory, response);
    }

    /**
     * 获取知识库分类详细信息
     */
    @Operation(summary = "获取知识库分类详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecategory:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(knowledgeCategoryService.getById(id));
    }

    /**
     * 新增知识库分类
     */
    @Operation(summary = "新增知识库分类")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecategory:add')")
    @Log(title = "知识库分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody KnowledgeCategory knowledgeCategory) {
        knowledgeCategory.setCreateBy(getUsername());
        knowledgeCategory.setCreateId(getUserId());
        return toAjax(knowledgeCategoryService.save(knowledgeCategory));
    }

    /**
     * 修改知识库分类
     */
    @Operation(summary = "修改知识库分类")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecategory:edit')")
    @Log(title = "知识库分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody KnowledgeCategory knowledgeCategory) {
        knowledgeCategory.setUpdateBy(getUsername());
        knowledgeCategory.setUpdateId(getUserId());
        return toAjax(knowledgeCategoryService.updateById(knowledgeCategory));
    }

    /**
     * 删除知识库分类
     */
    @Operation(summary = "删除知识库分类")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecategory:remove')")
    @Log(title = "知识库分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(knowledgeCategoryService.removeByIds(ids));
    }
}
