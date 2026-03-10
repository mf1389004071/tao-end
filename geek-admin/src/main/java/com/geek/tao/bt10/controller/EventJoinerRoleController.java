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
import com.geek.tao.bt10.domain.EventJoinerRole;
import com.geek.tao.bt10.service.IEventJoinerRoleService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 报名记录与活动角色的分配关系Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "报名记录与活动角色的分配关系")
@RestController
@RequestMapping("/bt10/eventjoinerrole")
public class EventJoinerRoleController extends BaseController {

    @Autowired
    private IEventJoinerRoleService eventJoinerRoleService;

    /**
     * 查询报名记录与活动角色的分配关系列表
     */
    @Operation(summary = "查询报名记录与活动角色的分配关系列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoinerrole:list')")
    @GetMapping("/list")
    public TableDataInfo<EventJoinerRole> list(EventJoinerRole eventJoinerRole) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<EventJoinerRole> list = eventJoinerRoleService.page(eventJoinerRole, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出报名记录与活动角色的分配关系列表
     */
    @Operation(summary = "导出报名记录与活动角色的分配关系列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoinerrole:export')")
    @Log(title = "报名记录与活动角色的分配关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EventJoinerRole eventJoinerRole) {
        eventJoinerRoleService.export(eventJoinerRole, response);
    }

    /**
     * 获取报名记录与活动角色的分配关系详细信息
     */
    @Operation(summary = "获取报名记录与活动角色的分配关系详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoinerrole:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(eventJoinerRoleService.getById(id));
    }

    /**
     * 新增报名记录与活动角色的分配关系
     */
    @Operation(summary = "新增报名记录与活动角色的分配关系")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoinerrole:add')")
    @Log(title = "报名记录与活动角色的分配关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody EventJoinerRole eventJoinerRole) {
        eventJoinerRole.setCreateBy(getUsername());
        eventJoinerRole.setCreateId(getUserId());
        return toAjax(eventJoinerRoleService.save(eventJoinerRole));
    }

    /**
     * 修改报名记录与活动角色的分配关系
     */
    @Operation(summary = "修改报名记录与活动角色的分配关系")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoinerrole:edit')")
    @Log(title = "报名记录与活动角色的分配关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EventJoinerRole eventJoinerRole) {
        eventJoinerRole.setUpdateBy(getUsername());
        eventJoinerRole.setUpdateId(getUserId());
        return toAjax(eventJoinerRoleService.updateById(eventJoinerRole));
    }

    /**
     * 删除报名记录与活动角色的分配关系
     */
    @Operation(summary = "删除报名记录与活动角色的分配关系")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoinerrole:remove')")
    @Log(title = "报名记录与活动角色的分配关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(eventJoinerRoleService.removeByIds(ids));
    }
}
