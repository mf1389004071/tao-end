package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.EventSession;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 周期活动的单场次 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IEventSessionService extends IService<EventSession> {

    /**
     * 分页查询周期活动的单场次
     *
     * @param eventSession 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<EventSession> page(EventSession eventSession, int pageNum, int pageSize);

    /**
     * 导出周期活动的单场次
     *
     * @param eventSession 查询条件
     * @param response 响应
     */
    void export(EventSession eventSession, HttpServletResponse response);


}
