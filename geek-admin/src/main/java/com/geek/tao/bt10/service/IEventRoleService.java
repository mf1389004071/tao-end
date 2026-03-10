package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.EventRole;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 活动所需角色 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IEventRoleService extends IService<EventRole> {

    /**
     * 分页查询活动所需角色
     *
     * @param eventRole 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<EventRole> page(EventRole eventRole, int pageNum, int pageSize);

    /**
     * 导出活动所需角色
     *
     * @param eventRole 查询条件
     * @param response 响应
     */
    void export(EventRole eventRole, HttpServletResponse response);


}
