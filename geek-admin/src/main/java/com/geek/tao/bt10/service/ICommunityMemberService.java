package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.CommunityMember;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 社群与用户的成员关系 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface ICommunityMemberService extends IService<CommunityMember> {

    /**
     * 分页查询社群与用户的成员关系
     *
     * @param communityMember 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<CommunityMember> page(CommunityMember communityMember, int pageNum, int pageSize);

    /**
     * 导出社群与用户的成员关系
     *
     * @param communityMember 查询条件
     * @param response 响应
     */
    void export(CommunityMember communityMember, HttpServletResponse response);


}
