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
import com.geek.tao.bt10.domain.UserGrowth;
import com.geek.tao.bt10.service.IUserGrowthService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户成长阶段变更历史Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "用户成长阶段变更历史")
@RestController
@RequestMapping("/bt10/usergrowth")
public class UserGrowthController extends BaseController {

    @Autowired
    private IUserGrowthService userGrowthService;

    /**
     * 查询用户成长阶段变更历史列表
     */
    @Operation(summary = "查询用户成长阶段变更历史列表")
    @PreAuthorize("@ss.hasPermi('bt10:usergrowth:list')")
    @GetMapping("/list")
    public TableDataInfo<UserGrowth> list(UserGrowth userGrowth) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<UserGrowth> list = userGrowthService.page(userGrowth, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出用户成长阶段变更历史列表
     */
    @Operation(summary = "导出用户成长阶段变更历史列表")
    @PreAuthorize("@ss.hasPermi('bt10:usergrowth:export')")
    @Log(title = "用户成长阶段变更历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserGrowth userGrowth) {
        userGrowthService.export(userGrowth, response);
    }

    /**
     * 获取用户成长阶段变更历史详细信息
     */
    @Operation(summary = "获取用户成长阶段变更历史详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:usergrowth:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userGrowthService.getById(id));
    }

    /**
     * 新增用户成长阶段变更历史
     */
    @Operation(summary = "新增用户成长阶段变更历史")
    @PreAuthorize("@ss.hasPermi('bt10:usergrowth:add')")
    @Log(title = "用户成长阶段变更历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UserGrowth userGrowth) {
        userGrowth.setCreateBy(getUsername());
        userGrowth.setCreateId(getUserId());
        return toAjax(userGrowthService.save(userGrowth));
    }

    /**
     * 修改用户成长阶段变更历史
     */
    @Operation(summary = "修改用户成长阶段变更历史")
    @PreAuthorize("@ss.hasPermi('bt10:usergrowth:edit')")
    @Log(title = "用户成长阶段变更历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody UserGrowth userGrowth) {
        userGrowth.setUpdateBy(getUsername());
        userGrowth.setUpdateId(getUserId());
        return toAjax(userGrowthService.updateById(userGrowth));
    }

    /**
     * 删除用户成长阶段变更历史
     */
    @Operation(summary = "删除用户成长阶段变更历史")
    @PreAuthorize("@ss.hasPermi('bt10:usergrowth:remove')")
    @Log(title = "用户成长阶段变更历史", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(userGrowthService.removeByIds(ids));
    }
}
