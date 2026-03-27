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
import com.geek.tao.bt10.domain.MessageThread;
import com.geek.tao.bt10.service.IMessageThreadService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 私信会话 Controller（管理端）
 */
@Tag(name = "私信会话")
@RestController
@RequestMapping("/bt10/messagethread")
public class MessageThreadController extends BaseController {

    @Autowired
    private IMessageThreadService messageThreadService;

    @Operation(summary = "查询私信会话列表")
    @PreAuthorize("@ss.hasPermi('bt10:messagethread:list')")
    @GetMapping("/list")
    public TableDataInfo<MessageThread> list(MessageThread query) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<MessageThread> list = messageThreadService.page(query, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    @Operation(summary = "导出私信会话")
    @PreAuthorize("@ss.hasPermi('bt10:messagethread:export')")
    @Log(title = "私信会话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MessageThread query) {
        messageThreadService.export(query, response);
    }

    @Operation(summary = "获取私信会话详情")
    @PreAuthorize("@ss.hasPermi('bt10:messagethread:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(messageThreadService.getById(id));
    }

    @Operation(summary = "新增私信会话")
    @PreAuthorize("@ss.hasPermi('bt10:messagethread:add')")
    @Log(title = "私信会话", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MessageThread entity) {
        return toAjax(messageThreadService.save(entity));
    }

    @Operation(summary = "修改私信会话")
    @PreAuthorize("@ss.hasPermi('bt10:messagethread:edit')")
    @Log(title = "私信会话", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MessageThread entity) {
        entity.setUpdateBy(getUsername());
        entity.setUpdateId(getUserId());
        return toAjax(messageThreadService.updateById(entity));
    }

    @Operation(summary = "删除私信会话")
    @PreAuthorize("@ss.hasPermi('bt10:messagethread:remove')")
    @Log(title = "私信会话", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(messageThreadService.removeByIds(ids));
    }
}
