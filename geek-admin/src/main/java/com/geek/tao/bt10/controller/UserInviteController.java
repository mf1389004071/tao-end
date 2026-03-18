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
import com.geek.tao.bt10.domain.UserInvite;
import com.geek.tao.bt10.service.IUserInviteService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 邀请关系与奖励记录Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "邀请关系与奖励记录")
@RestController
@RequestMapping("/bt10/userinvite")
public class UserInviteController extends BaseController {

    @Autowired
    private IUserInviteService userInviteService;

    /**
     * 查询邀请关系与奖励记录列表
     */
    @Operation(summary = "查询邀请关系与奖励记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:userinvite:list')")
    @GetMapping("/list")
    public TableDataInfo<UserInvite> list(UserInvite userInvite) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<UserInvite> list = userInviteService.page(userInvite, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出邀请关系与奖励记录列表
     */
    @Operation(summary = "导出邀请关系与奖励记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:userinvite:export')")
    @Log(title = "邀请关系与奖励记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserInvite userInvite) {
        userInviteService.export(userInvite, response);
    }

    /**
     * 获取邀请关系与奖励记录详细信息
     */
    @Operation(summary = "获取邀请关系与奖励记录详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:userinvite:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userInviteService.getById(id));
    }

    /**
     * 新增邀请关系与奖励记录
     */
    @Operation(summary = "新增邀请关系与奖励记录")
    @PreAuthorize("@ss.hasPermi('bt10:userinvite:add')")
    @Log(title = "邀请关系与奖励记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UserInvite userInvite) {
        return toAjax(userInviteService.save(userInvite));
    }

    /**
     * 修改邀请关系与奖励记录
     */
    @Operation(summary = "修改邀请关系与奖励记录")
    @PreAuthorize("@ss.hasPermi('bt10:userinvite:edit')")
    @Log(title = "邀请关系与奖励记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UserInvite userInvite) {
        userInvite.setUpdateBy(getUsername());
        userInvite.setUpdateId(getUserId());
        return toAjax(userInviteService.updateById(userInvite));
    }

    /**
     * 删除邀请关系与奖励记录
     */
    @Operation(summary = "删除邀请关系与奖励记录")
    @PreAuthorize("@ss.hasPermi('bt10:userinvite:remove')")
    @Log(title = "邀请关系与奖励记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(userInviteService.removeByIds(ids));
    }
}
