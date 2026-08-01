package com.geek.tao.bt10.controller;

import com.geek.common.annotation.Log;
import com.geek.common.core.controller.BaseController;
import com.geek.common.core.domain.AjaxResult;
import com.geek.common.core.page.PageDomain;
import com.geek.common.core.page.TableDataInfo;
import com.geek.common.core.page.TableSupport;
import com.geek.common.enums.BusinessType;
import com.geek.tao.bt10.domain.BizProduct;
import com.geek.tao.bt10.service.IBizProductService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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

import java.util.List;

@Tag(name = "业务产品")
@RestController
@RequestMapping("/bt10/bizproduct")
public class BizProductController extends BaseController {

    @Autowired
    private IBizProductService bizProductService;

    @Operation(summary = "查询业务产品列表")
    @PreAuthorize("@ss.hasPermi('bt10:bizproduct:list')")
    @GetMapping("/list")
    public TableDataInfo<BizProduct> list(BizProduct query) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<BizProduct> list = bizProductService.page(query, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    @Operation(summary = "导出业务产品")
    @PreAuthorize("@ss.hasPermi('bt10:bizproduct:export')")
    @Log(title = "业务产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizProduct query) {
        bizProductService.export(query, response);
    }

    @Operation(summary = "获取业务产品详细")
    @PreAuthorize("@ss.hasPermi('bt10:bizproduct:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(bizProductService.getById(id));
    }

    @Operation(summary = "新增业务产品")
    @PreAuthorize("@ss.hasPermi('bt10:bizproduct:add')")
    @Log(title = "业务产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BizProduct body) {
        return toAjax(bizProductService.save(body));
    }

    @Operation(summary = "修改业务产品")
    @PreAuthorize("@ss.hasPermi('bt10:bizproduct:edit')")
    @Log(title = "业务产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BizProduct body) {
        body.setUpdateBy(getUsername());
        body.setUpdateId(getUserId());
        return toAjax(bizProductService.updateById(body));
    }

    @Operation(summary = "删除业务产品")
    @PreAuthorize("@ss.hasPermi('bt10:bizproduct:remove')")
    @Log(title = "业务产品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(bizProductService.removeByIds(ids));
    }
}
