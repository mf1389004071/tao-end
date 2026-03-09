package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.KnowledgeComment;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 知识内容评论与回复，支持楼中楼与置顶 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-08
 */
public interface IKnowledgeCommentService extends IService<KnowledgeComment> {

    /**
     * 分页查询知识内容评论与回复，支持楼中楼与置顶
     *
     * @param knowledgeComment 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<KnowledgeComment> page(KnowledgeComment knowledgeComment, int pageNum, int pageSize);

    /**
     * 导出知识内容评论与回复，支持楼中楼与置顶
     *
     * @param knowledgeComment 查询条件
     * @param response 响应
     */
    void export(KnowledgeComment knowledgeComment, HttpServletResponse response);


}
