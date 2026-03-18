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
import com.geek.tao.bt10.domain.UserIdentities;
import com.geek.tao.bt10.service.IUserIdentitiesService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户身份关系表Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "用户身份关系表")
@RestController
@RequestMapping("/bt10/useridentities")
public class UserIdentitiesController extends BaseController {

    @Autowired
    private IUserIdentitiesService userIdentitiesService;

    /**
     * 查询用户身份关系表列表
     */
    @Operation(summary = "查询用户身份关系表列表")
    @PreAuthorize("@ss.hasPermi('bt10:useridentities:list')")
    @GetMapping("/list")
    public TableDataInfo<UserIdentities> list(UserIdentities userIdentities) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<UserIdentities> list = userIdentitiesService.page(userIdentities, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出用户身份关系表列表
     */
    @Operation(summary = "导出用户身份关系表列表")
    @PreAuthorize("@ss.hasPermi('bt10:useridentities:export')")
    @Log(title = "用户身份关系表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserIdentities userIdentities) {
        userIdentitiesService.export(userIdentities, response);
    }

    /**
     * 获取用户身份关系表详细信息
     */
    @Operation(summary = "获取用户身份关系表详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:useridentities:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userIdentitiesService.getById(id));
    }

    /**
     * 新增用户身份关系表
     */
    @Operation(summary = "新增用户身份关系表")
    @PreAuthorize("@ss.hasPermi('bt10:useridentities:add')")
    @Log(title = "用户身份关系表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UserIdentities userIdentities) {
        return toAjax(userIdentitiesService.save(userIdentities));
    }

    /**
     * 修改用户身份关系表
     */
    @Operation(summary = "修改用户身份关系表")
    @PreAuthorize("@ss.hasPermi('bt10:useridentities:edit')")
    @Log(title = "用户身份关系表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UserIdentities userIdentities) {
        userIdentities.setUpdateBy(getUsername());
        userIdentities.setUpdateId(getUserId());
        return toAjax(userIdentitiesService.updateById(userIdentities));
    }

    /**
     * 删除用户身份关系表
     */
    @Operation(summary = "删除用户身份关系表")
    @PreAuthorize("@ss.hasPermi('bt10:useridentities:remove')")
    @Log(title = "用户身份关系表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(userIdentitiesService.removeByIds(ids));
    }
}
