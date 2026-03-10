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
import com.geek.tao.bt10.domain.UserActivityLogs;
import com.geek.tao.bt10.service.IUserActivityLogsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户行为轨迹日志Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "用户行为轨迹日志")
@RestController
@RequestMapping("/bt10/useractivitylogs")
public class UserActivityLogsController extends BaseController {

    @Autowired
    private IUserActivityLogsService userActivityLogsService;

    /**
     * 查询用户行为轨迹日志列表
     */
    @Operation(summary = "查询用户行为轨迹日志列表")
    @PreAuthorize("@ss.hasPermi('bt10:useractivitylogs:list')")
    @GetMapping("/list")
    public TableDataInfo<UserActivityLogs> list(UserActivityLogs userActivityLogs) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<UserActivityLogs> list = userActivityLogsService.page(userActivityLogs, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出用户行为轨迹日志列表
     */
    @Operation(summary = "导出用户行为轨迹日志列表")
    @PreAuthorize("@ss.hasPermi('bt10:useractivitylogs:export')")
    @Log(title = "用户行为轨迹日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserActivityLogs userActivityLogs) {
        userActivityLogsService.export(userActivityLogs, response);
    }

    /**
     * 获取用户行为轨迹日志详细信息
     */
    @Operation(summary = "获取用户行为轨迹日志详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:useractivitylogs:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userActivityLogsService.getById(id));
    }

    /**
     * 新增用户行为轨迹日志
     */
    @Operation(summary = "新增用户行为轨迹日志")
    @PreAuthorize("@ss.hasPermi('bt10:useractivitylogs:add')")
    @Log(title = "用户行为轨迹日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UserActivityLogs userActivityLogs) {
        userActivityLogs.setCreateBy(getUsername());
        userActivityLogs.setCreateId(getUserId());
        return toAjax(userActivityLogsService.save(userActivityLogs));
    }

    /**
     * 修改用户行为轨迹日志
     */
    @Operation(summary = "修改用户行为轨迹日志")
    @PreAuthorize("@ss.hasPermi('bt10:useractivitylogs:edit')")
    @Log(title = "用户行为轨迹日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UserActivityLogs userActivityLogs) {
        userActivityLogs.setUpdateBy(getUsername());
        userActivityLogs.setUpdateId(getUserId());
        return toAjax(userActivityLogsService.updateById(userActivityLogs));
    }

    /**
     * 删除用户行为轨迹日志
     */
    @Operation(summary = "删除用户行为轨迹日志")
    @PreAuthorize("@ss.hasPermi('bt10:useractivitylogs:remove')")
    @Log(title = "用户行为轨迹日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(userActivityLogsService.removeByIds(ids));
    }
}
