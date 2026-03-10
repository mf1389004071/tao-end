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
import com.geek.tao.bt10.domain.PointProduct;
import com.geek.tao.bt10.service.IPointProductService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 积分商城商品Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "积分商城商品")
@RestController
@RequestMapping("/bt10/pointproduct")
public class PointProductController extends BaseController {

    @Autowired
    private IPointProductService pointProductService;

    /**
     * 查询积分商城商品列表
     */
    @Operation(summary = "查询积分商城商品列表")
    @PreAuthorize("@ss.hasPermi('bt10:pointproduct:list')")
    @GetMapping("/list")
    public TableDataInfo<PointProduct> list(PointProduct pointProduct) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<PointProduct> list = pointProductService.page(pointProduct, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出积分商城商品列表
     */
    @Operation(summary = "导出积分商城商品列表")
    @PreAuthorize("@ss.hasPermi('bt10:pointproduct:export')")
    @Log(title = "积分商城商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PointProduct pointProduct) {
        pointProductService.export(pointProduct, response);
    }

    /**
     * 获取积分商城商品详细信息
     */
    @Operation(summary = "获取积分商城商品详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:pointproduct:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(pointProductService.getById(id));
    }

    /**
     * 新增积分商城商品
     */
    @Operation(summary = "新增积分商城商品")
    @PreAuthorize("@ss.hasPermi('bt10:pointproduct:add')")
    @Log(title = "积分商城商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PointProduct pointProduct) {
        pointProduct.setCreateBy(getUsername());
        pointProduct.setCreateId(getUserId());
        return toAjax(pointProductService.save(pointProduct));
    }

    /**
     * 修改积分商城商品
     */
    @Operation(summary = "修改积分商城商品")
    @PreAuthorize("@ss.hasPermi('bt10:pointproduct:edit')")
    @Log(title = "积分商城商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PointProduct pointProduct) {
        pointProduct.setUpdateBy(getUsername());
        pointProduct.setUpdateId(getUserId());
        return toAjax(pointProductService.updateById(pointProduct));
    }

    /**
     * 删除积分商城商品
     */
    @Operation(summary = "删除积分商城商品")
    @PreAuthorize("@ss.hasPermi('bt10:pointproduct:remove')")
    @Log(title = "积分商城商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(pointProductService.removeByIds(ids));
    }
}
