package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.Notifications;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户站内通知 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface INotificationsService extends IService<Notifications> {

    /**
     * 分页查询用户站内通知
     *
     * @param notifications 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<Notifications> page(Notifications notifications, int pageNum, int pageSize);

    /**
     * 导出用户站内通知
     *
     * @param notifications 查询条件
     * @param response 响应
     */
    void export(Notifications notifications, HttpServletResponse response);


}
