package com.geek.tao.bt10.controller;

import java.util.List;

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
import com.geek.tao.bt10.domain.PrivateMessage;
import com.geek.tao.bt10.service.IPrivateMessageService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 私信消息 Controller（管理端）
 */
@Tag(name = "私信消息")
@RestController
@RequestMapping("/bt10/privatemessage")
public class PrivateMessageController extends BaseController {

    @Autowired
    private IPrivateMessageService privateMessageService;

    @Operation(summary = "查询私信消息列表")
    @PreAuthorize("@ss.hasPermi('bt10:privatemessage:list')")
    @GetMapping("/list")
    public TableDataInfo<PrivateMessage> list(PrivateMessage query) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<PrivateMessage> list = privateMessageService.page(query, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    @Operation(summary = "导出私信消息")
    @PreAuthorize("@ss.hasPermi('bt10:privatemessage:export')")
    @Log(title = "私信消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PrivateMessage query) {
        privateMessageService.export(query, response);
    }

    @Operation(summary = "获取私信消息详情")
    @PreAuthorize("@ss.hasPermi('bt10:privatemessage:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(privateMessageService.getById(id));
    }

    @Operation(summary = "新增私信消息")
    @PreAuthorize("@ss.hasPermi('bt10:privatemessage:add')")
    @Log(title = "私信消息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PrivateMessage entity) {
        return toAjax(privateMessageService.save(entity));
    }

    @Operation(summary = "修改私信消息")
    @PreAuthorize("@ss.hasPermi('bt10:privatemessage:edit')")
    @Log(title = "私信消息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody PrivateMessage entity) {
        entity.setUpdateBy(getUsername());
        entity.setUpdateId(getUserId());
        return toAjax(privateMessageService.updateById(entity));
    }

    @Operation(summary = "删除私信消息")
    @PreAuthorize("@ss.hasPermi('bt10:privatemessage:remove')")
    @Log(title = "私信消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(privateMessageService.removeByIds(ids));
    }
}
