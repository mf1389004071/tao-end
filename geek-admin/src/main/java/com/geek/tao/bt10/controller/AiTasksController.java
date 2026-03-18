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
import com.geek.tao.bt10.domain.AiTasks;
import com.geek.tao.bt10.service.IAiTasksService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * AI异步任务Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "AI异步任务")
@RestController
@RequestMapping("/bt10/aitasks")
public class AiTasksController extends BaseController {

    @Autowired
    private IAiTasksService aiTasksService;

    /**
     * 查询AI异步任务列表
     */
    @Operation(summary = "查询AI异步任务列表")
    @PreAuthorize("@ss.hasPermi('bt10:aitasks:list')")
    @GetMapping("/list")
    public TableDataInfo<AiTasks> list(AiTasks aiTasks) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<AiTasks> list = aiTasksService.page(aiTasks, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出AI异步任务列表
     */
    @Operation(summary = "导出AI异步任务列表")
    @PreAuthorize("@ss.hasPermi('bt10:aitasks:export')")
    @Log(title = "AI异步任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiTasks aiTasks) {
        aiTasksService.export(aiTasks, response);
    }

    /**
     * 获取AI异步任务详细信息
     */
    @Operation(summary = "获取AI异步任务详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:aitasks:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(aiTasksService.getById(id));
    }

    /**
     * 新增AI异步任务
     */
    @Operation(summary = "新增AI异步任务")
    @PreAuthorize("@ss.hasPermi('bt10:aitasks:add')")
    @Log(title = "AI异步任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AiTasks aiTasks) {
        return toAjax(aiTasksService.save(aiTasks));
    }

    /**
     * 修改AI异步任务
     */
    @Operation(summary = "修改AI异步任务")
    @PreAuthorize("@ss.hasPermi('bt10:aitasks:edit')")
    @Log(title = "AI异步任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AiTasks aiTasks) {
        aiTasks.setUpdateBy(getUsername());
        aiTasks.setUpdateId(getUserId());
        return toAjax(aiTasksService.updateById(aiTasks));
    }

    /**
     * 删除AI异步任务
     */
    @Operation(summary = "删除AI异步任务")
    @PreAuthorize("@ss.hasPermi('bt10:aitasks:remove')")
    @Log(title = "AI异步任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(aiTasksService.removeByIds(ids));
    }
}
