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
import com.geek.tao.bt10.domain.EventRole;
import com.geek.tao.bt10.service.IEventRoleService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 活动所需角色Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "活动所需角色")
@RestController
@RequestMapping("/bt10/eventrole")
public class EventRoleController extends BaseController {

    @Autowired
    private IEventRoleService eventRoleService;

    /**
     * 查询活动所需角色列表
     */
    @Operation(summary = "查询活动所需角色列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventrole:list')")
    @GetMapping("/list")
    public TableDataInfo<EventRole> list(EventRole eventRole) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<EventRole> list = eventRoleService.page(eventRole, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出活动所需角色列表
     */
    @Operation(summary = "导出活动所需角色列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventrole:export')")
    @Log(title = "活动所需角色", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EventRole eventRole) {
        eventRoleService.export(eventRole, response);
    }

    /**
     * 获取活动所需角色详细信息
     */
    @Operation(summary = "获取活动所需角色详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:eventrole:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(eventRoleService.getById(id));
    }

    /**
     * 新增活动所需角色
     */
    @Operation(summary = "新增活动所需角色")
    @PreAuthorize("@ss.hasPermi('bt10:eventrole:add')")
    @Log(title = "活动所需角色", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody EventRole eventRole) {
        return toAjax(eventRoleService.save(eventRole));
    }

    /**
     * 修改活动所需角色
     */
    @Operation(summary = "修改活动所需角色")
    @PreAuthorize("@ss.hasPermi('bt10:eventrole:edit')")
    @Log(title = "活动所需角色", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EventRole eventRole) {
        eventRole.setUpdateBy(getUsername());
        eventRole.setUpdateId(getUserId());
        return toAjax(eventRoleService.updateById(eventRole));
    }

    /**
     * 删除活动所需角色
     */
    @Operation(summary = "删除活动所需角色")
    @PreAuthorize("@ss.hasPermi('bt10:eventrole:remove')")
    @Log(title = "活动所需角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(eventRoleService.removeByIds(ids));
    }
}
