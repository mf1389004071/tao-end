package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.CommunityInfo;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 合伙人创建的社群 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface ICommunityInfoService extends IService<CommunityInfo> {

    /**
     * 分页查询合伙人创建的社群
     *
     * @param communityInfo 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<CommunityInfo> page(CommunityInfo communityInfo, int pageNum, int pageSize);

    /**
     * 导出合伙人创建的社群
     *
     * @param communityInfo 查询条件
     * @param response 响应
     */
    void export(CommunityInfo communityInfo, HttpServletResponse response);


}
