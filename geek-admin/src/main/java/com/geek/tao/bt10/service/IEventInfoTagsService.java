package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.EventInfoTags;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 活动与标签多对多关联 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IEventInfoTagsService extends IService<EventInfoTags> {

    /**
     * 分页查询活动与标签多对多关联
     *
     * @param eventInfoTags 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<EventInfoTags> page(EventInfoTags eventInfoTags, int pageNum, int pageSize);

    /**
     * 导出活动与标签多对多关联
     *
     * @param eventInfoTags 查询条件
     * @param response 响应
     */
    void export(EventInfoTags eventInfoTags, HttpServletResponse response);


}
