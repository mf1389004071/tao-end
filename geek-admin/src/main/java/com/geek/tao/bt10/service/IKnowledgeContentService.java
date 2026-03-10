package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.KnowledgeContent;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 知识库内容 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IKnowledgeContentService extends IService<KnowledgeContent> {

    /**
     * 分页查询知识库内容
     *
     * @param knowledgeContent 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<KnowledgeContent> page(KnowledgeContent knowledgeContent, int pageNum, int pageSize);

    /**
     * 导出知识库内容
     *
     * @param knowledgeContent 查询条件
     * @param response 响应
     */
    void export(KnowledgeContent knowledgeContent, HttpServletResponse response);


}
