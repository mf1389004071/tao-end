package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.EventInfo;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 活动或线下课程主表 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IEventInfoService extends IService<EventInfo> {

    /**
     * 分页查询活动或线下课程主表
     *
     * @param eventInfo 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<EventInfo> page(EventInfo eventInfo, int pageNum, int pageSize);

    /**
     * 导出活动或线下课程主表
     *
     * @param eventInfo 查询条件
     * @param response 响应
     */
    void export(EventInfo eventInfo, HttpServletResponse response);

    /**
     * 根据ID获取详情（含负责人昵称等关联填充）
     *
     * @param id 主键
     * @return 详情，未找到返回 null
     */
    EventInfo getInfo(Long id);

}
