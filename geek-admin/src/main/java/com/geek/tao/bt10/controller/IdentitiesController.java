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
import com.geek.tao.bt10.domain.Identities;
import com.geek.tao.bt10.service.IIdentitiesService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 系统身份定义表Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "系统身份定义表")
@RestController
@RequestMapping("/bt10/identities")
public class IdentitiesController extends BaseController {

    @Autowired
    private IIdentitiesService identitiesService;

    /**
     * 查询系统身份定义表列表
     */
    @Operation(summary = "查询系统身份定义表列表")
    @PreAuthorize("@ss.hasPermi('bt10:identities:list')")
    @GetMapping("/list")
    public TableDataInfo<Identities> list(Identities identities) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<Identities> list = identitiesService.page(identities, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出系统身份定义表列表
     */
    @Operation(summary = "导出系统身份定义表列表")
    @PreAuthorize("@ss.hasPermi('bt10:identities:export')")
    @Log(title = "系统身份定义表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Identities identities) {
        identitiesService.export(identities, response);
    }

    /**
     * 获取系统身份定义表详细信息
     */
    @Operation(summary = "获取系统身份定义表详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:identities:query')")
    @GetMapping("/{identityCode}")
    public AjaxResult getInfo(@PathVariable("identityCode") String identityCode) {
        return success(identitiesService.getById(identityCode));
    }

    /**
     * 新增系统身份定义表
     */
    @Operation(summary = "新增系统身份定义表")
    @PreAuthorize("@ss.hasPermi('bt10:identities:add')")
    @Log(title = "系统身份定义表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Identities identities) {
        return toAjax(identitiesService.save(identities));
    }

    /**
     * 修改系统身份定义表
     */
    @Operation(summary = "修改系统身份定义表")
    @PreAuthorize("@ss.hasPermi('bt10:identities:edit')")
    @Log(title = "系统身份定义表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Identities identities) {
        identities.setUpdateBy(getUsername());
        identities.setUpdateId(getUserId());
        return toAjax(identitiesService.updateById(identities));
    }

    /**
     * 删除系统身份定义表
     */
    @Operation(summary = "删除系统身份定义表")
    @PreAuthorize("@ss.hasPermi('bt10:identities:remove')")
    @Log(title = "系统身份定义表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{identityCodes}")
    public AjaxResult remove(@PathVariable("identityCodes") List<Long> identityCodes) {
        return toAjax(identitiesService.removeByIds(identityCodes));
    }
}
