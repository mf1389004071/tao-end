package com.geek.tao.bt10.controller;

import com.geek.common.core.controller.BaseController;
import com.geek.common.core.page.PageDomain;
import com.geek.common.core.page.TableDataInfo;
import com.geek.common.core.page.TableSupport;
import com.geek.tao.bt10.domain.Tags;
import com.geek.tao.bt10.service.ITagsService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * C 端标签列表（无需后台权限点，供小程序筛选）
 */
@Tag(name = "C端标签")
@RestController
@RequestMapping("/bt10/cust/tags")
public class CustTagsController extends BaseController {

    @Autowired
    private ITagsService tagsService;

    @Operation(summary = "标签分页列表")
    @GetMapping("/list")
    public TableDataInfo<Tags> list(Tags tags) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<Tags> list = tagsService.page(tags, pageDomain.getPageNum(), pageDomain.getPageSize());
        return getDataTable(list);
    }
}
