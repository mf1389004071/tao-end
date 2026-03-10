package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.KnowledgeCategory;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 知识库分类 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IKnowledgeCategoryService extends IService<KnowledgeCategory> {

    /**
     * 分页查询知识库分类
     *
     * @param knowledgeCategory 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<KnowledgeCategory> page(KnowledgeCategory knowledgeCategory, int pageNum, int pageSize);

    /**
     * 导出知识库分类
     *
     * @param knowledgeCategory 查询条件
     * @param response 响应
     */
    void export(KnowledgeCategory knowledgeCategory, HttpServletResponse response);


}
