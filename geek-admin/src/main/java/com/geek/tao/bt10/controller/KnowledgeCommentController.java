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
import com.geek.tao.bt10.domain.KnowledgeComment;
import com.geek.tao.bt10.service.IKnowledgeCommentService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 知识内容评论与回复，支持楼中楼与置顶Controller
 *
 * @author qm.wu
 * @date 2026-03-08
 */
@Tag(name = "知识内容评论与回复，支持楼中楼与置顶")
@RestController
@RequestMapping("/bt10/knowledgecomment")
public class KnowledgeCommentController extends BaseController {

    @Autowired
    private IKnowledgeCommentService knowledgeCommentService;

    /**
     * 查询知识内容评论与回复，支持楼中楼与置顶列表
     */
    @Operation(summary = "查询知识内容评论与回复，支持楼中楼与置顶列表")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecomment:list')")
    @GetMapping("/list")
    public TableDataInfo<KnowledgeComment> list(KnowledgeComment knowledgeComment) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<KnowledgeComment> list = knowledgeCommentService.page(knowledgeComment, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出知识内容评论与回复，支持楼中楼与置顶列表
     */
    @Operation(summary = "导出知识内容评论与回复，支持楼中楼与置顶列表")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecomment:export')")
    @Log(title = "知识内容评论与回复，支持楼中楼与置顶", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KnowledgeComment knowledgeComment) {
        knowledgeCommentService.export(knowledgeComment, response);
    }

    /**
     * 获取知识内容评论与回复，支持楼中楼与置顶详细信息
     */
    @Operation(summary = "获取知识内容评论与回复，支持楼中楼与置顶详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecomment:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(knowledgeCommentService.getById(id));
    }

    /**
     * 新增知识内容评论与回复，支持楼中楼与置顶
     */
    @Operation(summary = "新增知识内容评论与回复，支持楼中楼与置顶")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecomment:add')")
    @Log(title = "知识内容评论与回复，支持楼中楼与置顶", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody KnowledgeComment knowledgeComment) {
        knowledgeComment.setCreateBy(getUsername());
        knowledgeComment.setCreateId(getUserId());
        return toAjax(knowledgeCommentService.save(knowledgeComment));
    }

    /**
     * 修改知识内容评论与回复，支持楼中楼与置顶
     */
    @Operation(summary = "修改知识内容评论与回复，支持楼中楼与置顶")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecomment:edit')")
    @Log(title = "知识内容评论与回复，支持楼中楼与置顶", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody KnowledgeComment knowledgeComment) {
        knowledgeComment.setUpdateBy(getUsername());
        knowledgeComment.setUpdateId(getUserId());
        return toAjax(knowledgeCommentService.updateById(knowledgeComment));
    }

    /**
     * 删除知识内容评论与回复，支持楼中楼与置顶
     */
    @Operation(summary = "删除知识内容评论与回复，支持楼中楼与置顶")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgecomment:remove')")
    @Log(title = "知识内容评论与回复，支持楼中楼与置顶", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(knowledgeCommentService.removeByIds(ids));
    }
}
