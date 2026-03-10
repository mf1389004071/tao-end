package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.EventCheckIn;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 单次签到记录 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IEventCheckInService extends IService<EventCheckIn> {

    /**
     * 分页查询单次签到记录
     *
     * @param eventCheckIn 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<EventCheckIn> page(EventCheckIn eventCheckIn, int pageNum, int pageSize);

    /**
     * 导出单次签到记录
     *
     * @param eventCheckIn 查询条件
     * @param response 响应
     */
    void export(EventCheckIn eventCheckIn, HttpServletResponse response);


}
