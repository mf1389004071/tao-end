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
import com.geek.tao.bt10.domain.XiaoeUserMapping;
import com.geek.tao.bt10.service.IXiaoeUserMappingService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 本平台用户与小鹅通用户ID映射Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "本平台用户与小鹅通用户ID映射")
@RestController
@RequestMapping("/bt10/xiaoeusermapping")
public class XiaoeUserMappingController extends BaseController {

    @Autowired
    private IXiaoeUserMappingService xiaoeUserMappingService;

    /**
     * 查询本平台用户与小鹅通用户ID映射列表
     */
    @Operation(summary = "查询本平台用户与小鹅通用户ID映射列表")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeusermapping:list')")
    @GetMapping("/list")
    public TableDataInfo<XiaoeUserMapping> list(XiaoeUserMapping xiaoeUserMapping) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<XiaoeUserMapping> list = xiaoeUserMappingService.page(xiaoeUserMapping, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出本平台用户与小鹅通用户ID映射列表
     */
    @Operation(summary = "导出本平台用户与小鹅通用户ID映射列表")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeusermapping:export')")
    @Log(title = "本平台用户与小鹅通用户ID映射", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XiaoeUserMapping xiaoeUserMapping) {
        xiaoeUserMappingService.export(xiaoeUserMapping, response);
    }

    /**
     * 获取本平台用户与小鹅通用户ID映射详细信息
     */
    @Operation(summary = "获取本平台用户与小鹅通用户ID映射详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeusermapping:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(xiaoeUserMappingService.getById(id));
    }

    /**
     * 新增本平台用户与小鹅通用户ID映射
     */
    @Operation(summary = "新增本平台用户与小鹅通用户ID映射")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeusermapping:add')")
    @Log(title = "本平台用户与小鹅通用户ID映射", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody XiaoeUserMapping xiaoeUserMapping) {
        xiaoeUserMapping.setCreateBy(getUsername());
        xiaoeUserMapping.setCreateId(getUserId());
        return toAjax(xiaoeUserMappingService.save(xiaoeUserMapping));
    }

    /**
     * 修改本平台用户与小鹅通用户ID映射
     */
    @Operation(summary = "修改本平台用户与小鹅通用户ID映射")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeusermapping:edit')")
    @Log(title = "本平台用户与小鹅通用户ID映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody XiaoeUserMapping xiaoeUserMapping) {
        xiaoeUserMapping.setUpdateBy(getUsername());
        xiaoeUserMapping.setUpdateId(getUserId());
        return toAjax(xiaoeUserMappingService.updateById(xiaoeUserMapping));
    }

    /**
     * 删除本平台用户与小鹅通用户ID映射
     */
    @Operation(summary = "删除本平台用户与小鹅通用户ID映射")
    @PreAuthorize("@ss.hasPermi('bt10:xiaoeusermapping:remove')")
    @Log(title = "本平台用户与小鹅通用户ID映射", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(xiaoeUserMappingService.removeByIds(ids));
    }
}
