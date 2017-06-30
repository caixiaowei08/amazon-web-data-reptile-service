package com.amazon.service.fund.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.amazon.service.fund.entity.UserFundEntity;
import com.amazon.service.fund.service.UserFundService;
import com.amazon.service.fund.vo.AlipayNotifyPojo;
import com.amazon.system.alipay.AlipayConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by User on 2017/6/10.
 */
@Scope("prototype")
@Controller
@RequestMapping("/userFundController")
public class UserFundController extends BaseController {

    private static Logger logger = LogManager.getLogger(UserFundController.class.getName());

    @Autowired
    private UserFundService userFundService;

    @RequestMapping(params = "getUserFundInfo")
    @ResponseBody
    public AjaxJson getUserFundInfo(HttpServletRequest request,HttpServletResponse response) {
        return userFundService.getUserFundInfo();
    }

    @RequestMapping("/doAlipayTradePagePay")
    public void doAlipayTradePagePay(HttpServletRequest request, HttpServletResponse response) {
        userFundService.goChargeFund(request,response);
    }

    /**
     * 支付宝回调交易验证
     * @param request
     * @param response
     */
    @RequestMapping("/doAlipayTradePageNotify")
    public void doAlipayTradePageNotify(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            }catch (UnsupportedEncodingException e){
                logger.error(valueStr,e);
            }
            logger.info(name+":"+valueStr);
            params.put(name, valueStr);
        }
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV2(
                    params,
                    AlipayConfig.ALIPAY_PUBLIC_KEY,
                    AlipayConfig.CHARSET,
                    AlipayConfig.SIGN_TYPE); //调用SDK验证签名
        }catch (AlipayApiException e){
            logger.error(e);
        }

        try {
            OutputStream out = response.getOutputStream();
            if(signVerified) {
                //商户订单号
                String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
                //支付宝交易号
                String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
                //交易状态
                String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
                logger.info("out_trade_no:"+out_trade_no);
                logger.info("trade_no:"+trade_no);
                logger.info("trade_status:"+trade_status);
                AlipayNotifyPojo alipayNotifyPojo = new AlipayNotifyPojo();
                alipayNotifyPojo.setOut_trade_no(out_trade_no);
                alipayNotifyPojo.setTrade_no(trade_no);
                alipayNotifyPojo.setTrade_status(trade_no);

                if(trade_status.equals("TRADE_FINISHED")){
                    logger.info("out_trade_no:"+out_trade_no);
                    logger.info("trade_no:"+trade_no);
                    logger.info("trade_status:"+trade_status);
                    logger.info("------------TRADE_FINISHED--------------");
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序
                    //注意：
                    //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                  AjaxJson j =  userFundService.notifyChargeFund(alipayNotifyPojo);
                  logger.info("处理TRADE_FINISHED完毕！"+JSON.toJSONString(j));
                }else if (trade_status.equals("TRADE_SUCCESS")){
                    logger.info("out_trade_no:"+out_trade_no);
                    logger.info("trade_no:"+trade_no);
                    logger.info("trade_status:"+trade_status);
                    logger.info("------------TRADE_SUCCESS--------------");
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序
                    //注意：
                    //付款完成后，支付宝系统发送该交易状态通知
                    AjaxJson j =  userFundService.notifyChargeFund(alipayNotifyPojo);
                    logger.info("处理TRADE_SUCCESS完毕！"+JSON.toJSONString(j));
                }
                logger.error("回调成功！");
                out.write("success".getBytes());
            }else{
                logger.error("回调验证失败！");
                out.write("fail".getBytes());
            }
        }catch (UnsupportedEncodingException e){
            logger.error("调用SDK验证签名",e);
        }catch (IOException ie){
            logger.error("IO异常",ie);
        }
    }

}
