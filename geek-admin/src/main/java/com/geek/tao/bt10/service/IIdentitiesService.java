package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.Identities;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 系统身份定义表 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IIdentitiesService extends IService<Identities> {

    /**
     * 分页查询系统身份定义表
     *
     * @param identities 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<Identities> page(Identities identities, int pageNum, int pageSize);

    /**
     * 导出系统身份定义表
     *
     * @param identities 查询条件
     * @param response 响应
     */
    void export(Identities identities, HttpServletResponse response);


}
