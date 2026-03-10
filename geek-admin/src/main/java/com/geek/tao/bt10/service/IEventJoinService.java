package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.EventJoin;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户活动报名记录 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IEventJoinService extends IService<EventJoin> {

    /**
     * 分页查询用户活动报名记录
     *
     * @param eventJoin 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<EventJoin> page(EventJoin eventJoin, int pageNum, int pageSize);

    /**
     * 导出用户活动报名记录
     *
     * @param eventJoin 查询条件
     * @param response 响应
     */
    void export(EventJoin eventJoin, HttpServletResponse response);


}
