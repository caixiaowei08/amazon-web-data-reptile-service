package com.amazon.system.alipay.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 2017/6/25.
 */
public interface AlipayService {

    /**
     * 统一下单接口 支付宝调用
     * @param alipayTradePayModel
     * @param httpRequest
     * @param httpResponse
     * @throws ServletException
     * @throws IOException
     */
    public void doAlipayTradePayRequestPost(AlipayTradePayModel alipayTradePayModel, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException;



}
