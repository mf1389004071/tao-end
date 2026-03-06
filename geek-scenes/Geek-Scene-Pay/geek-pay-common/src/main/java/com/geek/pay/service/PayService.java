package com.geek.pay.service;

import com.geek.pay.domain.PayOrder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface PayService {
    String payUrl(PayOrder payOrder);

    String notify(HttpServletRequest servletRequest, HttpServletResponse response, PayOrder payOrder, String type)
            throws Exception;

    PayOrder query(PayOrder payOrder);

    PayOrder refund(PayOrder payOrder);
}
