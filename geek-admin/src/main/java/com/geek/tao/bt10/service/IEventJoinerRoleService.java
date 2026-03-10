package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.EventJoinerRole;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 报名记录与活动角色的分配关系 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IEventJoinerRoleService extends IService<EventJoinerRole> {

    /**
     * 分页查询报名记录与活动角色的分配关系
     *
     * @param eventJoinerRole 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<EventJoinerRole> page(EventJoinerRole eventJoinerRole, int pageNum, int pageSize);

    /**
     * 导出报名记录与活动角色的分配关系
     *
     * @param eventJoinerRole 查询条件
     * @param response 响应
     */
    void export(EventJoinerRole eventJoinerRole, HttpServletResponse response);


}
