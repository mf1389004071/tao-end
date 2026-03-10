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
import com.geek.tao.bt10.domain.PointChangeLogs;
import com.geek.tao.bt10.service.IPointChangeLogsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 积分变动审计Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "积分变动审计")
@RestController
@RequestMapping("/bt10/pointchangelogs")
public class PointChangeLogsController extends BaseController {

    @Autowired
    private IPointChangeLogsService pointChangeLogsService;

    /**
     * 查询积分变动审计列表
     */
    @Operation(summary = "查询积分变动审计列表")
    @PreAuthorize("@ss.hasPermi('bt10:pointchangelogs:list')")
    @GetMapping("/list")
    public TableDataInfo<PointChangeLogs> list(PointChangeLogs pointChangeLogs) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<PointChangeLogs> list = pointChangeLogsService.page(pointChangeLogs, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出积分变动审计列表
     */
    @Operation(summary = "导出积分变动审计列表")
    @PreAuthorize("@ss.hasPermi('bt10:pointchangelogs:export')")
    @Log(title = "积分变动审计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PointChangeLogs pointChangeLogs) {
        pointChangeLogsService.export(pointChangeLogs, response);
    }

    /**
     * 获取积分变动审计详细信息
     */
    @Operation(summary = "获取积分变动审计详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:pointchangelogs:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(pointChangeLogsService.getById(id));
    }

    /**
     * 新增积分变动审计
     */
    @Operation(summary = "新增积分变动审计")
    @PreAuthorize("@ss.hasPermi('bt10:pointchangelogs:add')")
    @Log(title = "积分变动审计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PointChangeLogs pointChangeLogs) {
        pointChangeLogs.setCreateBy(getUsername());
        pointChangeLogs.setCreateId(getUserId());
        return toAjax(pointChangeLogsService.save(pointChangeLogs));
    }

    /**
     * 修改积分变动审计
     */
    @Operation(summary = "修改积分变动审计")
    @PreAuthorize("@ss.hasPermi('bt10:pointchangelogs:edit')")
    @Log(title = "积分变动审计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PointChangeLogs pointChangeLogs) {
        pointChangeLogs.setUpdateBy(getUsername());
        pointChangeLogs.setUpdateId(getUserId());
        return toAjax(pointChangeLogsService.updateById(pointChangeLogs));
    }

    /**
     * 删除积分变动审计
     */
    @Operation(summary = "删除积分变动审计")
    @PreAuthorize("@ss.hasPermi('bt10:pointchangelogs:remove')")
    @Log(title = "积分变动审计", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(pointChangeLogsService.removeByIds(ids));
    }
}
