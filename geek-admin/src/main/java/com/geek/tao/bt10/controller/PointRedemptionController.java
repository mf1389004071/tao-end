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
import com.geek.tao.bt10.domain.PointRedemption;
import com.geek.tao.bt10.service.IPointRedemptionService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户积分兑换记录Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "用户积分兑换记录")
@RestController
@RequestMapping("/bt10/pointredemption")
public class PointRedemptionController extends BaseController {

    @Autowired
    private IPointRedemptionService pointRedemptionService;

    /**
     * 查询用户积分兑换记录列表
     */
    @Operation(summary = "查询用户积分兑换记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:pointredemption:list')")
    @GetMapping("/list")
    public TableDataInfo<PointRedemption> list(PointRedemption pointRedemption) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<PointRedemption> list = pointRedemptionService.page(pointRedemption, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出用户积分兑换记录列表
     */
    @Operation(summary = "导出用户积分兑换记录列表")
    @PreAuthorize("@ss.hasPermi('bt10:pointredemption:export')")
    @Log(title = "用户积分兑换记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PointRedemption pointRedemption) {
        pointRedemptionService.export(pointRedemption, response);
    }

    /**
     * 获取用户积分兑换记录详细信息
     */
    @Operation(summary = "获取用户积分兑换记录详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:pointredemption:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(pointRedemptionService.getById(id));
    }

    /**
     * 新增用户积分兑换记录
     */
    @Operation(summary = "新增用户积分兑换记录")
    @PreAuthorize("@ss.hasPermi('bt10:pointredemption:add')")
    @Log(title = "用户积分兑换记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PointRedemption pointRedemption) {
        pointRedemption.setCreateBy(getUsername());
        pointRedemption.setCreateId(getUserId());
        return toAjax(pointRedemptionService.save(pointRedemption));
    }

    /**
     * 修改用户积分兑换记录
     */
    @Operation(summary = "修改用户积分兑换记录")
    @PreAuthorize("@ss.hasPermi('bt10:pointredemption:edit')")
    @Log(title = "用户积分兑换记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PointRedemption pointRedemption) {
        pointRedemption.setUpdateBy(getUsername());
        pointRedemption.setUpdateId(getUserId());
        return toAjax(pointRedemptionService.updateById(pointRedemption));
    }

    /**
     * 删除用户积分兑换记录
     */
    @Operation(summary = "删除用户积分兑换记录")
    @PreAuthorize("@ss.hasPermi('bt10:pointredemption:remove')")
    @Log(title = "用户积分兑换记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(pointRedemptionService.removeByIds(ids));
    }
}
