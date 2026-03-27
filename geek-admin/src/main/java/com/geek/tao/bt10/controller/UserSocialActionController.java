package com.geek.tao.bt10.controller;

import java.util.List;

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
import com.geek.tao.bt10.domain.UserSocialAction;
import com.geek.tao.bt10.service.IUserSocialActionService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户主页社交动作 Controller（管理端）
 */
@Tag(name = "用户主页社交动作")
@RestController
@RequestMapping("/bt10/usersocialaction")
public class UserSocialActionController extends BaseController {

    @Autowired
    private IUserSocialActionService userSocialActionService;

    @Operation(summary = "查询用户主页社交动作列表")
    @PreAuthorize("@ss.hasPermi('bt10:usersocialaction:list')")
    @GetMapping("/list")
    public TableDataInfo<UserSocialAction> list(UserSocialAction query) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<UserSocialAction> list = userSocialActionService.page(query, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    @Operation(summary = "导出用户主页社交动作")
    @PreAuthorize("@ss.hasPermi('bt10:usersocialaction:export')")
    @Log(title = "用户主页社交动作", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserSocialAction query) {
        userSocialActionService.export(query, response);
    }

    @Operation(summary = "获取用户主页社交动作详情")
    @PreAuthorize("@ss.hasPermi('bt10:usersocialaction:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userSocialActionService.getById(id));
    }

    @Operation(summary = "新增用户主页社交动作")
    @PreAuthorize("@ss.hasPermi('bt10:usersocialaction:add')")
    @Log(title = "用户主页社交动作", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UserSocialAction entity) {
        return toAjax(userSocialActionService.save(entity));
    }

    @Operation(summary = "修改用户主页社交动作")
    @PreAuthorize("@ss.hasPermi('bt10:usersocialaction:edit')")
    @Log(title = "用户主页社交动作", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UserSocialAction entity) {
        entity.setUpdateBy(getUsername());
        entity.setUpdateId(getUserId());
        return toAjax(userSocialActionService.updateById(entity));
    }

    @Operation(summary = "删除用户主页社交动作")
    @PreAuthorize("@ss.hasPermi('bt10:usersocialaction:remove')")
    @Log(title = "用户主页社交动作", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(userSocialActionService.removeByIds(ids));
    }
}
