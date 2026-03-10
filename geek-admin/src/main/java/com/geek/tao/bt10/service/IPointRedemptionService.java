package com.geek.tao.bt10.service;

import com.geek.tao.bt10.domain.PointRedemption;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 用户积分兑换记录 服务层接口
 *
 * @author qm.wu
 * @date 2026-03-10
 */
public interface IPointRedemptionService extends IService<PointRedemption> {

    /**
     * 分页查询用户积分兑换记录
     *
     * @param pointRedemption 查询条件
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    Page<PointRedemption> page(PointRedemption pointRedemption, int pageNum, int pageSize);

    /**
     * 导出用户积分兑换记录
     *
     * @param pointRedemption 查询条件
     * @param response 响应
     */
    void export(PointRedemption pointRedemption, HttpServletResponse response);


}
