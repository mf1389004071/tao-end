package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.XiaoeUserMapping;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 本平台用户与小鹅通用户ID映射 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IXiaoeUserMappingService extends IService<XiaoeUserMapping> {

    /**
     * 分页查询本平台用户与小鹅通用户ID映射
     *
     * @param xiaoeUserMapping 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<XiaoeUserMapping> page(XiaoeUserMapping xiaoeUserMapping, int pageNum, int pageSize);

    /**
     * 导出本平台用户与小鹅通用户ID映射
     *
     * @param xiaoeUserMapping 查询条件
     * @param response 响应
     */
    void export(XiaoeUserMapping xiaoeUserMapping, HttpServletResponse response);


}
