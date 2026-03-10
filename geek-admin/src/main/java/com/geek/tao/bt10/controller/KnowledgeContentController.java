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
import com.geek.tao.bt10.domain.KnowledgeContent;
import com.geek.tao.bt10.service.IKnowledgeContentService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 知识库内容Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "知识库内容")
@RestController
@RequestMapping("/bt10/knowledgecontent")
public class KnowledgeContentController extends BaseController {

    @Autowired
    private IKnowledgeContentService knowledgeContentService;

    /**
     * 查询知识库内容列表
     */
    @Operation(summary = "查询知识库内容列表")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontent:list')")
    @GetMapping("/list")
    public TableDataInfo<KnowledgeContent> list(KnowledgeContent knowledgeContent) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<KnowledgeContent> list = knowledgeContentService.page(knowledgeContent, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出知识库内容列表
     */
    @Operation(summary = "导出知识库内容列表")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontent:export')")
    @Log(title = "知识库内容", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KnowledgeContent knowledgeContent) {
        knowledgeContentService.export(knowledgeContent, response);
    }

    /**
     * 获取知识库内容详细信息
     */
    @Operation(summary = "获取知识库内容详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontent:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(knowledgeContentService.getById(id));
    }

    /**
     * 新增知识库内容
     */
    @Operation(summary = "新增知识库内容")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontent:add')")
    @Log(title = "知识库内容", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody KnowledgeContent knowledgeContent) {
        knowledgeContent.setCreateBy(getUsername());
        knowledgeContent.setCreateId(getUserId());
        return toAjax(knowledgeContentService.save(knowledgeContent));
    }

    /**
     * 修改知识库内容
     */
    @Operation(summary = "修改知识库内容")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontent:edit')")
    @Log(title = "知识库内容", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody KnowledgeContent knowledgeContent) {
        knowledgeContent.setUpdateBy(getUsername());
        knowledgeContent.setUpdateId(getUserId());
        return toAjax(knowledgeContentService.updateById(knowledgeContent));
    }

    /**
     * 删除知识库内容
     */
    @Operation(summary = "删除知识库内容")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecontent:remove')")
    @Log(title = "知识库内容", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(knowledgeContentService.removeByIds(ids));
    }
}
