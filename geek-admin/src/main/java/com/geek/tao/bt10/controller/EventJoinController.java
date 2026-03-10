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
import com.geek.tao.bt10.domain.EventJoin;
import com.geek.tao.bt10.service.IEventJoinService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户活动报名记录Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "用户活动报名记录")
@RestController
@RequestMapping("/bt10/eventjoin")
public class EventJoinController extends BaseController {

    @Autowired
    private IEventJoinService eventJoinService;

    /**
     * 查询用户活动报名记录列表
     */
    @Operation(summary = "查询用户活动报名记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoin:list')")
    @GetMapping("/list")
    public TableDataInfo<EventJoin> list(EventJoin eventJoin) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<EventJoin> list = eventJoinService.page(eventJoin, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出用户活动报名记录列表
     */
    @Operation(summary = "导出用户活动报名记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoin:export')")
    @Log(title = "用户活动报名记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EventJoin eventJoin) {
        eventJoinService.export(eventJoin, response);
    }

    /**
     * 获取用户活动报名记录详细信息
     */
    @Operation(summary = "获取用户活动报名记录详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoin:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(eventJoinService.getById(id));
    }

    /**
     * 新增用户活动报名记录
     */
    @Operation(summary = "新增用户活动报名记录")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoin:add')")
    @Log(title = "用户活动报名记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody EventJoin eventJoin) {
        eventJoin.setCreateBy(getUsername());
        eventJoin.setCreateId(getUserId());
        return toAjax(eventJoinService.save(eventJoin));
    }

    /**
     * 修改用户活动报名记录
     */
    @Operation(summary = "修改用户活动报名记录")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoin:edit')")
    @Log(title = "用户活动报名记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EventJoin eventJoin) {
        eventJoin.setUpdateBy(getUsername());
        eventJoin.setUpdateId(getUserId());
        return toAjax(eventJoinService.updateById(eventJoin));
    }

    /**
     * 删除用户活动报名记录
     */
    @Operation(summary = "删除用户活动报名记录")
    @PreAuthorize("@ss.hasPermi('bt10:eventjoin:remove')")
    @Log(title = "用户活动报名记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(eventJoinService.removeByIds(ids));
    }
}
