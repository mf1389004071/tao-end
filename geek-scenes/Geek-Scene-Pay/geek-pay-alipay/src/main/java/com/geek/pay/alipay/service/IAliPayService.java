package com.geek.pay.alipay.service;

import java.util.Map;

import com.geek.pay.service.PayService;

public interface IAliPayService extends PayService {
    public void callback(Map<String, String> params);
}
