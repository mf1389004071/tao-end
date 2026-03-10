package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.Tags;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 通用标签定义表 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface ITagsService extends IService<Tags> {

    /**
     * 分页查询通用标签定义表
     *
     * @param tags 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<Tags> page(Tags tags, int pageNum, int pageSize);

    /**
     * 导出通用标签定义表
     *
     * @param tags 查询条件
     * @param response 响应
     */
    void export(Tags tags, HttpServletResponse response);


}
