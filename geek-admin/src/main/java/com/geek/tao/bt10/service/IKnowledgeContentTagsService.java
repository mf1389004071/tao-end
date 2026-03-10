package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.KnowledgeContentTags;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 知识内容与标签多对多关联 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IKnowledgeContentTagsService extends IService<KnowledgeContentTags> {

    /**
     * 分页查询知识内容与标签多对多关联
     *
     * @param knowledgeContentTags 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<KnowledgeContentTags> page(KnowledgeContentTags knowledgeContentTags, int pageNum, int pageSize);

    /**
     * 导出知识内容与标签多对多关联
     *
     * @param knowledgeContentTags 查询条件
     * @param response 响应
     */
    void export(KnowledgeContentTags knowledgeContentTags, HttpServletResponse response);


}
