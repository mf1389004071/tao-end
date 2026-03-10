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
import com.geek.tao.bt10.domain.UserPointLogs;
import com.geek.tao.bt10.service.IUserPointLogsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户积分收支流水Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "用户积分收支流水")
@RestController
@RequestMapping("/bt10/userpointlogs")
public class UserPointLogsController extends BaseController {

    @Autowired
    private IUserPointLogsService userPointLogsService;

    /**
     * 查询用户积分收支流水列表
     */
    @Operation(summary = "查询用户积分收支流水列表")
    @PreAuthorize("@ss.hasPermi('bt10:userpointlogs:list')")
    @GetMapping("/list")
    public TableDataInfo<UserPointLogs> list(UserPointLogs userPointLogs) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<UserPointLogs> list = userPointLogsService.page(userPointLogs, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出用户积分收支流水列表
     */
    @Operation(summary = "导出用户积分收支流水列表")
    @PreAuthorize("@ss.hasPermi('bt10:userpointlogs:export')")
    @Log(title = "用户积分收支流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserPointLogs userPointLogs) {
        userPointLogsService.export(userPointLogs, response);
    }

    /**
     * 获取用户积分收支流水详细信息
     */
    @Operation(summary = "获取用户积分收支流水详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:userpointlogs:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userPointLogsService.getById(id));
    }

    /**
     * 新增用户积分收支流水
     */
    @Operation(summary = "新增用户积分收支流水")
    @PreAuthorize("@ss.hasPermi('bt10:userpointlogs:add')")
    @Log(title = "用户积分收支流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UserPointLogs userPointLogs) {
        userPointLogs.setCreateBy(getUsername());
        userPointLogs.setCreateId(getUserId());
        return toAjax(userPointLogsService.save(userPointLogs));
    }

    /**
     * 修改用户积分收支流水
     */
    @Operation(summary = "修改用户积分收支流水")
    @PreAuthorize("@ss.hasPermi('bt10:userpointlogs:edit')")
    @Log(title = "用户积分收支流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UserPointLogs userPointLogs) {
        userPointLogs.setUpdateBy(getUsername());
        userPointLogs.setUpdateId(getUserId());
        return toAjax(userPointLogsService.updateById(userPointLogs));
    }

    /**
     * 删除用户积分收支流水
     */
    @Operation(summary = "删除用户积分收支流水")
    @PreAuthorize("@ss.hasPermi('bt10:userpointlogs:remove')")
    @Log(title = "用户积分收支流水", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(userPointLogsService.removeByIds(ids));
    }
}
