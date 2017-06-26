package com.amazon.system.alipay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayObject;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.amazon.service.fund.controller.UserFundController;
import com.amazon.system.alipay.AlipayClientSingleton;
import com.amazon.system.alipay.AlipayConfig;
import com.amazon.system.alipay.service.AlipayService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 2017/6/25.
 */
@Service("alipayService")
@Transactional
public class AlipayServiceImpl implements AlipayService {

    private static Logger logger = LogManager.getLogger(AlipayServiceImpl.class.getName());

    public void doAlipayTradePayRequestPost(AlipayTradePayModel alipayTradePayModel, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException{
        AlipayClient alipayClient = AlipayClientSingleton.getInstance();
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setBizModel(alipayTradePayModel);
        String form ="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();
        }catch (AlipayApiException e){
            logger.error(e);
        }
        httpResponse.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }
}
