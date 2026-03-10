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
import com.geek.tao.bt10.domain.AiVectors;
import com.geek.tao.bt10.service.IAiVectorsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * AI向量Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "AI向量")
@RestController
@RequestMapping("/bt10/aivectors")
public class AiVectorsController extends BaseController {

    @Autowired
    private IAiVectorsService aiVectorsService;

    /**
     * 查询AI向量列表
     */
    @Operation(summary = "查询AI向量列表")
    @PreAuthorize("@ss.hasPermi('bt10:aivectors:list')")
    @GetMapping("/list")
    public TableDataInfo<AiVectors> list(AiVectors aiVectors) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<AiVectors> list = aiVectorsService.page(aiVectors, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出AI向量列表
     */
    @Operation(summary = "导出AI向量列表")
    @PreAuthorize("@ss.hasPermi('bt10:aivectors:export')")
    @Log(title = "AI向量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiVectors aiVectors) {
        aiVectorsService.export(aiVectors, response);
    }

    /**
     * 获取AI向量详细信息
     */
    @Operation(summary = "获取AI向量详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:aivectors:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(aiVectorsService.getById(id));
    }

    /**
     * 新增AI向量
     */
    @Operation(summary = "新增AI向量")
    @PreAuthorize("@ss.hasPermi('bt10:aivectors:add')")
    @Log(title = "AI向量", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AiVectors aiVectors) {
        aiVectors.setCreateBy(getUsername());
        aiVectors.setCreateId(getUserId());
        return toAjax(aiVectorsService.save(aiVectors));
    }

    /**
     * 修改AI向量
     */
    @Operation(summary = "修改AI向量")
    @PreAuthorize("@ss.hasPermi('bt10:aivectors:edit')")
    @Log(title = "AI向量", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AiVectors aiVectors) {
        aiVectors.setUpdateBy(getUsername());
        aiVectors.setUpdateId(getUserId());
        return toAjax(aiVectorsService.updateById(aiVectors));
    }

    /**
     * 删除AI向量
     */
    @Operation(summary = "删除AI向量")
    @PreAuthorize("@ss.hasPermi('bt10:aivectors:remove')")
    @Log(title = "AI向量", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(aiVectorsService.removeByIds(ids));
    }
}
