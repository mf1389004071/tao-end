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
import com.geek.tao.bt10.domain.AiMeetingTranscripts;
import com.geek.tao.bt10.service.IAiMeetingTranscriptsService;
import com.mybatisflex.core.paginate.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 音视频转写与AI摘要Controller
 *
 * @author qm.wu
 * @date 2026-03-10
 */
@Tag(name = "音视频转写与AI摘要")
@RestController
@RequestMapping("/bt10/aimeetingtranscripts")
public class AiMeetingTranscriptsController extends BaseController {

    @Autowired
    private IAiMeetingTranscriptsService aiMeetingTranscriptsService;

    /**
     * 查询音视频转写与AI摘要列表
     */
    @Operation(summary = "查询音视频转写与AI摘要列表")
    @PreAuthorize("@ss.hasPermi('bt10:aimeetingtranscripts:list')")
    @GetMapping("/list")
    public TableDataInfo<AiMeetingTranscripts> list(AiMeetingTranscripts aiMeetingTranscripts) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<AiMeetingTranscripts> list = aiMeetingTranscriptsService.page(aiMeetingTranscripts, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }

    /**
     * 导出音视频转写与AI摘要列表
     */
    @Operation(summary = "导出音视频转写与AI摘要列表")
    @PreAuthorize("@ss.hasPermi('bt10:aimeetingtranscripts:export')")
    @Log(title = "音视频转写与AI摘要", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiMeetingTranscripts aiMeetingTranscripts) {
        aiMeetingTranscriptsService.export(aiMeetingTranscripts, response);
    }

    /**
     * 获取音视频转写与AI摘要详细信息
     */
    @Operation(summary = "获取音视频转写与AI摘要详细信息")
    @PreAuthorize("@ss.hasPermi('bt10:aimeetingtranscripts:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(aiMeetingTranscriptsService.getById(id));
    }

    /**
     * 新增音视频转写与AI摘要
     */
    @Operation(summary = "新增音视频转写与AI摘要")
    @PreAuthorize("@ss.hasPermi('bt10:aimeetingtranscripts:add')")
    @Log(title = "音视频转写与AI摘要", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AiMeetingTranscripts aiMeetingTranscripts) {
        return toAjax(aiMeetingTranscriptsService.save(aiMeetingTranscripts));
    }

    /**
     * 修改音视频转写与AI摘要
     */
    @Operation(summary = "修改音视频转写与AI摘要")
    @PreAuthorize("@ss.hasPermi('bt10:aimeetingtranscripts:edit')")
    @Log(title = "音视频转写与AI摘要", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AiMeetingTranscripts aiMeetingTranscripts) {
        aiMeetingTranscripts.setUpdateBy(getUsername());
        aiMeetingTranscripts.setUpdateId(getUserId());
        return toAjax(aiMeetingTranscriptsService.updateById(aiMeetingTranscripts));
    }

    /**
     * 删除音视频转写与AI摘要
     */
    @Operation(summary = "删除音视频转写与AI摘要")
    @PreAuthorize("@ss.hasPermi('bt10:aimeetingtranscripts:remove')")
    @Log(title = "音视频转写与AI摘要", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") List<Long> ids) {
        return toAjax(aiMeetingTranscriptsService.removeByIds(ids));
    }
}
