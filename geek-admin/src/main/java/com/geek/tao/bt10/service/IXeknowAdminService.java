package com.geek.tao.bt10.service;

import java.util.Map;

import com.geek.tao.bt10.domain.XeknowReportEncryptedReq;

/**
 * xeknow 采集上报服务
 */
public interface IXeknowAdminService {

    /**
     * 解密上报并按 source 落库
     */
    Map<String, Object> ingestReport(XeknowReportEncryptedReq req);

    /**
     * 分页下发缺手机号且已到期的小鹅 user_id
     *
     * @param page     页码，从 1 起
     * @param pageSize 每页条数，默认 100
     */
    Map<String, Object> listPhoneMissingUserIds(int page, int pageSize);
}
