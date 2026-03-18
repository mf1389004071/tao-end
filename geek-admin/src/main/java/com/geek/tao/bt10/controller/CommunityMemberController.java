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
import com.geek.tao.bt10.domain.CommunityMember;
import com.geek.tao.bt10.service.ICommunityMemberService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 社群与用户的成员关系Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "社群与用户的成员关系")
@RestController
@RequestMapping("/bt10/communitymember")
public class CommunityMemberController extends BaseController {

    @Autowired
    private ICommunityMemberService communityMemberService;

    /**
     * 查询社群与用户的成员关系列表
     */
    @Operation(summary = "查询社群与用户的成员关系列表")
    @PreAuthorize("@ss.hasPermi('bt10:communitymember:list')")
    @GetMapping("/list")
    public TableDataInfo<CommunityMember> list(CommunityMember communityMember) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<CommunityMember> list = communityMemberService.page(communityMember, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出社群与用户的成员关系列表
     */
    @Operation(summary = "导出社群与用户的成员关系列表")
    @PreAuthorize("@ss.hasPermi('bt10:communitymember:export')")
    @Log(title = "社群与用户的成员关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CommunityMember communityMember) {
        communityMemberService.export(communityMember, response);
    }

    /**
     * 获取社群与用户的成员关系详细信息
     */
    @Operation(summary = "获取社群与用户的成员关系详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:communitymember:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(communityMemberService.getById(id));
    }

    /**
     * 新增社群与用户的成员关系
     */
    @Operation(summary = "新增社群与用户的成员关系")
    @PreAuthorize("@ss.hasPermi('bt10:communitymember:add')")
    @Log(title = "社群与用户的成员关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CommunityMember communityMember) {
        return toAjax(communityMemberService.save(communityMember));
    }

    /**
     * 修改社群与用户的成员关系
     */
    @Operation(summary = "修改社群与用户的成员关系")
    @PreAuthorize("@ss.hasPermi('bt10:communitymember:edit')")
    @Log(title = "社群与用户的成员关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CommunityMember communityMember) {
        communityMember.setUpdateBy(getUsername());
        communityMember.setUpdateId(getUserId());
        return toAjax(communityMemberService.updateById(communityMember));
    }

    /**
     * 删除社群与用户的成员关系
     */
    @Operation(summary = "删除社群与用户的成员关系")
    @PreAuthorize("@ss.hasPermi('bt10:communitymember:remove')")
    @Log(title = "社群与用户的成员关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(communityMemberService.removeByIds(ids));
    }
}
