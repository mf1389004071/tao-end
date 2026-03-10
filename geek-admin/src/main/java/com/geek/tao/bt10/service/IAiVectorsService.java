package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.AiVectors;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * AI向量 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IAiVectorsService extends IService<AiVectors> {

    /**
     * 分页查询AI向量
     *
     * @param aiVectors 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<AiVectors> page(AiVectors aiVectors, int pageNum, int pageSize);

    /**
     * 导出AI向量
     *
     * @param aiVectors 查询条件
     * @param response 响应
     */
    void export(AiVectors aiVectors, HttpServletResponse response);


}
