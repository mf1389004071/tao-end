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
import com.geek.tao.bt10.domain.UserTags;
import com.geek.tao.bt10.service.IUserTagsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户与多维标签关联表Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "用户与多维标签关联表")
@RestController
@RequestMapping("/bt10/usertags")
public class UserTagsController extends BaseController {

    @Autowired
    private IUserTagsService userTagsService;

    /**
     * 查询用户与多维标签关联表列表
     */
    @Operation(summary = "查询用户与多维标签关联表列表")
    @PreAuthorize("@ss.hasPermi('bt10:usertags:list')")
    @GetMapping("/list")
    public TableDataInfo<UserTags> list(UserTags userTags) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<UserTags> list = userTagsService.page(userTags, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出用户与多维标签关联表列表
     */
    @Operation(summary = "导出用户与多维标签关联表列表")
    @PreAuthorize("@ss.hasPermi('bt10:usertags:export')")
    @Log(title = "用户与多维标签关联表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserTags userTags) {
        userTagsService.export(userTags, response);
    }

    /**
     * 获取用户与多维标签关联表详细信息
     */
    @Operation(summary = "获取用户与多维标签关联表详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:usertags:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userTagsService.getById(id));
    }

    /**
     * 新增用户与多维标签关联表
     */
    @Operation(summary = "新增用户与多维标签关联表")
    @PreAuthorize("@ss.hasPermi('bt10:usertags:add')")
    @Log(title = "用户与多维标签关联表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UserTags userTags) {
        return toAjax(userTagsService.save(userTags));
    }

    /**
     * 修改用户与多维标签关联表
     */
    @Operation(summary = "修改用户与多维标签关联表")
    @PreAuthorize("@ss.hasPermi('bt10:usertags:edit')")
    @Log(title = "用户与多维标签关联表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UserTags userTags) {
        userTags.setUpdateBy(getUsername());
        userTags.setUpdateId(getUserId());
        return toAjax(userTagsService.updateById(userTags));
    }

    /**
     * 删除用户与多维标签关联表
     */
    @Operation(summary = "删除用户与多维标签关联表")
    @PreAuthorize("@ss.hasPermi('bt10:usertags:remove')")
    @Log(title = "用户与多维标签关联表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(userTagsService.removeByIds(ids));
    }
}
