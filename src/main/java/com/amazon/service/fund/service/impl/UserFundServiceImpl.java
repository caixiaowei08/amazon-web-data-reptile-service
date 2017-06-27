package com.amazon.service.fund.service.impl;

import com.alipay.api.domain.AlipayTradePayModel;
import com.amazon.service.fund.controller.UserFundController;
import com.amazon.service.fund.entity.UserFundEntity;
import com.amazon.service.fund.service.UserFundService;
import com.amazon.service.fund.vo.UserFundVo;
import com.amazon.service.promot.price.entity.PromotPriceEntity;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.system.Constant;
import com.amazon.system.alipay.ChargeFundConfig;
import com.amazon.system.alipay.service.AlipayService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.core.utils.RegularExpressionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by User on 2017/6/10.
 */
@Service("userFundService")
@Transactional
public class UserFundServiceImpl extends BaseServiceImpl implements UserFundService {

    private static Logger logger = LogManager.getLogger(UserFundServiceImpl.class.getName());

    @Autowired
    private GlobalService globalService;

    @Autowired
    private AlipayService alipayService;

    public AjaxJson getUserFundInfo() {
        AjaxJson j = new AjaxJson();
        UserEntity  userSession = globalService.getUserEntityFromSession();
        if (userSession == null) {
            ContextHolderUtils.getSession().invalidate();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请重新登录");
            return j;
        }

        UserFundVo userFundVo = new UserFundVo();
        userFundVo.setAccount(userSession.getAccount());
        //获取账户资金信息
        DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
        userFundDetachedCriteria.add(Restrictions.eq("sellerId", userSession.getId()));
        List<UserFundEntity> userFundEntityList = globalService.getListByCriteriaQuery(userFundDetachedCriteria);
        if(CollectionUtils.isNotEmpty(userFundEntityList)) {
            UserFundEntity userFundEntity = userFundEntityList.get(0);
            userFundVo.setTotalFund(userFundEntity.getTotalFund());
            userFundVo.setUsableFund(userFundEntity.getUsableFund());
            userFundVo.setFreezeFund(userFundEntity.getFreezeFund());
        }
        DetachedCriteria promotPriceDetachedCriteria = DetachedCriteria.forClass(PromotPriceEntity.class);
        List<PromotPriceEntity> promotPriceEntityList = globalService.getListByCriteriaQuery(promotPriceDetachedCriteria);
        if(CollectionUtils.isNotEmpty(promotPriceEntityList)){
            PromotPriceEntity promotPriceEntity =  promotPriceEntityList.get(0);
            userFundVo.setExchangeRate(promotPriceEntity.getExchangeRate());
        }
        j.setContent(userFundVo);
        return j;
    }

    public void goChargeFund(HttpServletRequest request, HttpServletResponse response) {
        String chargefund =  request.getParameter("chargeFund").trim();
        String chargeSource =  request.getParameter("chargeSource");
        if(!RegularExpressionUtils.isMoney(chargefund)){
           return;
        }
        //获取当前汇率
        DetachedCriteria promotPriceDetachedCriteria = DetachedCriteria.forClass(PromotPriceEntity.class);
        List<PromotPriceEntity> promotPriceEntityList = globalService.getListByCriteriaQuery(promotPriceDetachedCriteria);
        BigDecimal exchangeRate = null;
        if(CollectionUtils.isNotEmpty(promotPriceEntityList)&& promotPriceEntityList.get(0).getExchangeRate()!=null){
            exchangeRate = promotPriceEntityList.get(0).getExchangeRate();
        }else{
            logger.error("请在数据库中设置人民币/美元换算汇率！");
        }

        BigDecimal chargefundDollar = new BigDecimal(chargefund);
        BigDecimal chargefundRMB = chargefundDollar.multiply(exchangeRate);
        chargefundRMB = chargefundRMB.setScale(2,BigDecimal.ROUND_UP);//向上转型
        AlipayTradePayModel alipayTradePayModel = new AlipayTradePayModel();
        alipayTradePayModel.setOutTradeNo(globalService.generateOrderNumber());
        alipayTradePayModel.setSubject(ChargeFundConfig.SUBJECT);
        alipayTradePayModel.setBody(ChargeFundConfig.BODY);
        alipayTradePayModel.setTotalAmount(chargefundRMB.toString());
        alipayTradePayModel.setProductCode(ChargeFundConfig.PROJECTCODE);
        try {
            alipayService.doAlipayTradePayRequestPost(alipayTradePayModel, request, response);
        } catch (IOException ie) {
            logger.error(ie);
        }catch (ServletException se){
            logger.error(se);
        }
    }
}
