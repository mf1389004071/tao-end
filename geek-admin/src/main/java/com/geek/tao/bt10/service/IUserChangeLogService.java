package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.UserChangeLog;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户关键字段变更记录 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IUserChangeLogService extends IService<UserChangeLog> {

    /**
     * 分页查询用户关键字段变更记录
     *
     * @param userChangeLog 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<UserChangeLog> page(UserChangeLog userChangeLog, int pageNum, int pageSize);

    /**
     * 导出用户关键字段变更记录
     *
     * @param userChangeLog 查询条件
     * @param response 响应
     */
    void export(UserChangeLog userChangeLog, HttpServletResponse response);


}
