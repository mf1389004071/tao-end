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
import com.geek.tao.bt10.domain.UserProfiles;
import com.geek.tao.bt10.service.IUserProfilesService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户信息画像扩展表Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "用户信息画像扩展表")
@RestController
@RequestMapping("/bt10/userprofiles")
public class UserProfilesController extends BaseController {

    @Autowired
    private IUserProfilesService userProfilesService;

    /**
     * 查询用户信息画像扩展表列表
     */
    @Operation(summary = "查询用户信息画像扩展表列表")
    @PreAuthorize("@ss.hasPermi('bt10:userprofiles:list')")
    @GetMapping("/list")
    public TableDataInfo<UserProfiles> list(UserProfiles userProfiles) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<UserProfiles> list = userProfilesService.page(userProfiles, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出用户信息画像扩展表列表
     */
    @Operation(summary = "导出用户信息画像扩展表列表")
    @PreAuthorize("@ss.hasPermi('bt10:userprofiles:export')")
    @Log(title = "用户信息画像扩展表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserProfiles userProfiles) {
        userProfilesService.export(userProfiles, response);
    }

    /**
     * 获取用户信息画像扩展表详细信息
     */
    @Operation(summary = "获取用户信息画像扩展表详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:userprofiles:query')")
    @GetMapping("/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId) {
        return success(userProfilesService.getById(userId));
    }

    /**
     * 新增用户信息画像扩展表
     */
    @Operation(summary = "新增用户信息画像扩展表")
    @PreAuthorize("@ss.hasPermi('bt10:userprofiles:add')")
    @Log(title = "用户信息画像扩展表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UserProfiles userProfiles) {
        return toAjax(userProfilesService.save(userProfiles));
    }

    /**
     * 修改用户信息画像扩展表
     */
    @Operation(summary = "修改用户信息画像扩展表")
    @PreAuthorize("@ss.hasPermi('bt10:userprofiles:edit')")
    @Log(title = "用户信息画像扩展表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UserProfiles userProfiles) {
        userProfiles.setUpdateBy(getUsername());
        userProfiles.setUpdateId(getUserId());
        return toAjax(userProfilesService.updateById(userProfiles));
    }

    /**
     * 删除用户信息画像扩展表
     */
    @Operation(summary = "删除用户信息画像扩展表")
    @PreAuthorize("@ss.hasPermi('bt10:userprofiles:remove')")
    @Log(title = "用户信息画像扩展表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable("userIds") List<Long> userIds) {
        return toAjax(userProfilesService.removeByIds(userIds));
    }
}
