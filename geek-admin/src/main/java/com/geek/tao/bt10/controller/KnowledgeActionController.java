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
import com.geek.tao.bt10.domain.KnowledgeAction;
import com.geek.tao.bt10.service.IKnowledgeActionService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户对知识内容的行为记录Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "用户对知识内容的行为记录")
@RestController
@RequestMapping("/bt10/knowledgeaction")
public class KnowledgeActionController extends BaseController {

    @Autowired
    private IKnowledgeActionService knowledgeActionService;

    /**
     * 查询用户对知识内容的行为记录列表
     */
    @Operation(summary = "查询用户对知识内容的行为记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgeaction:list')")
    @GetMapping("/list")
    public TableDataInfo<KnowledgeAction> list(KnowledgeAction knowledgeAction) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<KnowledgeAction> list = knowledgeActionService.page(knowledgeAction, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出用户对知识内容的行为记录列表
     */
    @Operation(summary = "导出用户对知识内容的行为记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgeaction:export')")
    @Log(title = "用户对知识内容的行为记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KnowledgeAction knowledgeAction) {
        knowledgeActionService.export(knowledgeAction, response);
    }

    /**
     * 获取用户对知识内容的行为记录详细信息
     */
    @Operation(summary = "获取用户对知识内容的行为记录详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgeaction:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(knowledgeActionService.getById(id));
    }

    /**
     * 新增用户对知识内容的行为记录
     */
    @Operation(summary = "新增用户对知识内容的行为记录")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgeaction:add')")
    @Log(title = "用户对知识内容的行为记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody KnowledgeAction knowledgeAction) {
        knowledgeAction.setCreateBy(getUsername());
        knowledgeAction.setCreateId(getUserId());
        return toAjax(knowledgeActionService.save(knowledgeAction));
    }

    /**
     * 修改用户对知识内容的行为记录
     */
    @Operation(summary = "修改用户对知识内容的行为记录")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgeaction:edit')")
    @Log(title = "用户对知识内容的行为记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody KnowledgeAction knowledgeAction) {
        knowledgeAction.setUpdateBy(getUsername());
        knowledgeAction.setUpdateId(getUserId());
        return toAjax(knowledgeActionService.updateById(knowledgeAction));
    }

    /**
     * 删除用户对知识内容的行为记录
     */
    @Operation(summary = "删除用户对知识内容的行为记录")
    @PreAuthorize("@ss.hasPermi('bt10:knowledgeaction:remove')")
    @Log(title = "用户对知识内容的行为记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(knowledgeActionService.removeByIds(ids));
    }
}
