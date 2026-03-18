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
import com.geek.tao.bt10.domain.CommunityInfo;
import com.geek.tao.bt10.service.ICommunityInfoService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 合伙人创建的社群Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "合伙人创建的社群")
@RestController
@RequestMapping("/bt10/communityinfo")
public class CommunityInfoController extends BaseController {

    @Autowired
    private ICommunityInfoService communityInfoService;

    /**
     * 查询合伙人创建的社群列表
     */
    @Operation(summary = "查询合伙人创建的社群列表")
    @PreAuthorize("@ss.hasPermi('bt10:communityinfo:list')")
    @GetMapping("/list")
    public TableDataInfo<CommunityInfo> list(CommunityInfo communityInfo) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<CommunityInfo> list = communityInfoService.page(communityInfo, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出合伙人创建的社群列表
     */
    @Operation(summary = "导出合伙人创建的社群列表")
    @PreAuthorize("@ss.hasPermi('bt10:communityinfo:export')")
    @Log(title = "合伙人创建的社群", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CommunityInfo communityInfo) {
        communityInfoService.export(communityInfo, response);
    }

    /**
     * 获取合伙人创建的社群详细信息
     */
    @Operation(summary = "获取合伙人创建的社群详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:communityinfo:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(communityInfoService.getById(id));
    }

    /**
     * 新增合伙人创建的社群
     */
    @Operation(summary = "新增合伙人创建的社群")
    @PreAuthorize("@ss.hasPermi('bt10:communityinfo:add')")
    @Log(title = "合伙人创建的社群", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CommunityInfo communityInfo) {
        return toAjax(communityInfoService.save(communityInfo));
    }

    /**
     * 修改合伙人创建的社群
     */
    @Operation(summary = "修改合伙人创建的社群")
    @PreAuthorize("@ss.hasPermi('bt10:communityinfo:edit')")
    @Log(title = "合伙人创建的社群", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CommunityInfo communityInfo) {
        communityInfo.setUpdateBy(getUsername());
        communityInfo.setUpdateId(getUserId());
        return toAjax(communityInfoService.updateById(communityInfo));
    }

    /**
     * 删除合伙人创建的社群
     */
    @Operation(summary = "删除合伙人创建的社群")
    @PreAuthorize("@ss.hasPermi('bt10:communityinfo:remove')")
    @Log(title = "合伙人创建的社群", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(communityInfoService.removeByIds(ids));
    }
}
