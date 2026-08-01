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
     * 下发缺手机号且已到期的小鹅 user_id
     */
    Map<String, Object> listPhoneMissingUserIds(int limit);
}
