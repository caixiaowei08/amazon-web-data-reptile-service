package com.amazon.service.fund.service.impl;

import com.alipay.api.domain.AlipayTradePayModel;
import com.amazon.service.fund.ConstantChargeType;
import com.amazon.service.fund.ConstantFund;
import com.amazon.service.fund.ConstantSource;
import com.amazon.service.fund.controller.UserFundController;
import com.amazon.service.fund.entity.UserFundEntity;
import com.amazon.service.fund.service.UserFundService;
import com.amazon.service.fund.vo.AlipayNotifyPojo;
import com.amazon.service.fund.vo.UserFundVo;
import com.amazon.service.promot.price.entity.PromotPriceEntity;
import com.amazon.service.recharge.controller.UserRechargeFundController;
import com.amazon.service.recharge.entity.UserRechargeFundEntity;
import com.amazon.service.recharge.service.UserRechargeFundService;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.vip.entity.UserMembershipEntity;
import com.amazon.service.vip.service.UserMembershipService;
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
import org.framework.core.utils.DateUtils.DateUtils;
import org.framework.core.utils.RegularExpressionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    private UserFundService userFundService;

    @Autowired
    private UserMembershipService userMembershipService;

    @Autowired
    private UserRechargeFundService rechargeFundService;

    public AjaxJson getUserFundInfo() {
        AjaxJson j = new AjaxJson();
        UserEntity userSession = globalService.getUserEntityFromSession();
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
        if (CollectionUtils.isNotEmpty(userFundEntityList)) {
            UserFundEntity userFundEntity = userFundEntityList.get(0);
            userFundVo.setTotalFund(userFundEntity.getTotalFund());
            userFundVo.setUsableFund(userFundEntity.getUsableFund());
            userFundVo.setFreezeFund(userFundEntity.getFreezeFund());
        }
        DetachedCriteria promotPriceDetachedCriteria = DetachedCriteria.forClass(PromotPriceEntity.class);
        List<PromotPriceEntity> promotPriceEntityList = globalService.getListByCriteriaQuery(promotPriceDetachedCriteria);
        if (CollectionUtils.isNotEmpty(promotPriceEntityList)) {
            PromotPriceEntity promotPriceEntity = promotPriceEntityList.get(0);
            userFundVo.setExchangeRate(promotPriceEntity.getExchangeRate());
        }
        j.setContent(userFundVo);
        return j;
    }

    public AjaxJson goChargeFund(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String chargefund = request.getParameter("chargeFund").trim();
        String chargeSource = request.getParameter("chargeSource");
        if (!RegularExpressionUtils.isMoney(chargefund)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("充值金额有误，请重新输入！");
            return j;
        }

        UserEntity userEntity = globalService.getUserEntityFromSession();

        if (userEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请重新登录！");
            return j;
        }

        //获取当前汇率
        DetachedCriteria promotPriceDetachedCriteria = DetachedCriteria.forClass(PromotPriceEntity.class);
        List<PromotPriceEntity> promotPriceEntityList = globalService.getListByCriteriaQuery(promotPriceDetachedCriteria);
        BigDecimal exchangeRate = null;
        if (CollectionUtils.isNotEmpty(promotPriceEntityList) && promotPriceEntityList.get(0).getExchangeRate() != null) {
            exchangeRate = promotPriceEntityList.get(0).getExchangeRate();
        } else {
            logger.error("请在数据库中设置人民币/美元换算汇率！");
        }

        BigDecimal chargefundDollar = new BigDecimal(chargefund);
        BigDecimal chargefundRMB = chargefundDollar.multiply(exchangeRate);
        chargefundRMB = chargefundRMB.setScale(2, BigDecimal.ROUND_UP);//向上转型
        AlipayTradePayModel alipayTradePayModel = new AlipayTradePayModel();
        alipayTradePayModel.setOutTradeNo(globalService.generateOrderNumber());
        alipayTradePayModel.setSubject(ChargeFundConfig.SUBJECT);
        alipayTradePayModel.setBody(ChargeFundConfig.BODY);
        alipayTradePayModel.setTotalAmount(chargefundRMB.toString());
        alipayTradePayModel.setProductCode(ChargeFundConfig.PROJECTCODE);

        UserRechargeFundEntity userRechargeFundEntity = new UserRechargeFundEntity();
        userRechargeFundEntity.setSellerId(userEntity.getId());
        userRechargeFundEntity.setPlatformOrderNum(alipayTradePayModel.getOutTradeNo());
        userRechargeFundEntity.setChargeFund(chargefundDollar);
        userRechargeFundEntity.setChargeFundRmb(chargefundRMB);
        userRechargeFundEntity.setExchangeRate(exchangeRate);
        userRechargeFundEntity.setState(ConstantFund.TO_BE_COMFIRM);//待确认
        userRechargeFundEntity.setChargeSource(ConstantSource.ZFB);//支付宝
        userRechargeFundEntity.setChargeType(ConstantChargeType.BALANCE_FUND);//余额充值
        userRechargeFundEntity.setStartTime(new Date());
        userRechargeFundEntity.setCreateTime(new Date());
        userRechargeFundEntity.setUpdateTime(new Date());
        rechargeFundService.save(userRechargeFundEntity);
        try {
            alipayService.doAlipayTradePayRequestPost(alipayTradePayModel, request, response);
        } catch (IOException ie) {
            logger.error(ie);
        } catch (ServletException se) {
            logger.error(se);
        }
        return j;
    }

    public AjaxJson notifyChargeFund(AlipayNotifyPojo alipayNotifyPojo) {
        AjaxJson j = new AjaxJson();
        //根据platformOrderNum查询交易信息
        if (alipayNotifyPojo == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("支付宝异步信息不能为空！");
            return j;
        }
        if (!StringUtils.hasText(alipayNotifyPojo.getOut_trade_no())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("平台订单号为空！");
            return j;
        }

        DetachedCriteria userRechargeFundDetachedCriteria = DetachedCriteria.forClass(UserRechargeFundEntity.class);
        userRechargeFundDetachedCriteria.add(Restrictions.eq("platformOrderNum", alipayNotifyPojo.getOut_trade_no()));
        userRechargeFundDetachedCriteria.add(Restrictions.eq("state", ConstantFund.TO_BE_COMFIRM));//待确认状态
        List<UserRechargeFundEntity> userRechargeFundEntityList = rechargeFundService.getListByCriteriaQuery(userRechargeFundDetachedCriteria);
        if (!CollectionUtils.isNotEmpty(userRechargeFundEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能找到初始充值订单！");
            return j;
        }

        UserRechargeFundEntity userRechargeFundEntity = userRechargeFundEntityList.get(0);
        if (userRechargeFundEntity.getChargeType().equals(ConstantChargeType.BALANCE_FUND)) {//余额充值
            DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
            userFundDetachedCriteria.add(Restrictions.eq("sellerId", userRechargeFundEntity.getSellerId()));
            List<UserFundEntity> userFundEntityList = userFundService.getListByCriteriaQuery(userFundDetachedCriteria);
            if (CollectionUtils.isEmpty(userFundEntityList)) {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("无法找到资金账户，请联系管理员！");
                return j;
            }
            //资金变动
            UserFundEntity userFundEntity = userFundEntityList.get(0);
            userFundEntity.setTotalFund(userFundEntity.getTotalFund().add(userRechargeFundEntity.getChargeFund()));
            userFundEntity.setUsableFund(userFundEntity.getUsableFund().add(userRechargeFundEntity.getChargeFund()));
            userFundEntity.setUpdateTime(new Date());
            //充值流水变动
            userRechargeFundEntity.setZfbOrderNum(alipayNotifyPojo.getTrade_no());
            userRechargeFundEntity.setNotifyInfo(alipayNotifyPojo.getTrade_status());
            userRechargeFundEntity.setConfirmTime(new Date());
            userRechargeFundEntity.setState(ConstantFund.SUCCESS);
            userFundService.saveOrUpdate(userFundEntity);
            rechargeFundService.saveOrUpdate(userRechargeFundEntity);
        } else if (userRechargeFundEntity.getChargeType().equals(ConstantChargeType.BALANCE_FUND)) {//会员充值
            DetachedCriteria userMembershipDetachedCriteria = DetachedCriteria.forClass(UserMembershipEntity.class);
            userMembershipDetachedCriteria.add(Restrictions.eq("sellerId", userRechargeFundEntity.getSellerId()));
            List<UserMembershipEntity> userMembershipEntityList = userFundService.getListByCriteriaQuery(userMembershipDetachedCriteria);
            if (CollectionUtils.isEmpty(userMembershipEntityList)) {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("无法找到资金账户，请联系管理员！");
                return j;
            }

            UserMembershipEntity userMembershipEntity = userMembershipEntityList.get(0);
            //处理会员逻辑
            if (userMembershipEntity.getMembershipEndTime() == null) {
                Date beginOfDate = DateUtils.getBeginOfDate();
                userMembershipEntity.setMembershipStartTime(beginOfDate);
                DateUtils.addMonth(beginOfDate, userRechargeFundEntity.getMemberShipMonth());
                userMembershipEntity.setMembershipEndTime(beginOfDate);
                userMembershipEntity.setChargeFund(userRechargeFundEntity.getChargeFundRmb());
                userMembershipEntity.setTotalMembershipCost(userRechargeFundEntity.getChargeFundRmb());
            } else {
                //判断是否到期
                Date membershipEndTime = userMembershipEntity.getMembershipEndTime();
                if (DateUtils.compareTo(membershipEndTime, new Date()) > 0){//未到期处理
                    userMembershipEntity.setLastMembershipEndTime(membershipEndTime);
                    DateUtils.addMonth(membershipEndTime,userRechargeFundEntity.getMemberShipMonth());
                    userMembershipEntity.setMembershipEndTime(membershipEndTime);
                }else{//到期处理
                    Date beginOfDate = DateUtils.getBeginOfDate();
                    userMembershipEntity.setMembershipStartTime(beginOfDate);
                    DateUtils.addMonth(beginOfDate, userRechargeFundEntity.getMemberShipMonth());
                    userMembershipEntity.setMembershipEndTime(beginOfDate);
                    userMembershipEntity.setLastMembershipEndTime(membershipEndTime);
                }
                userMembershipEntity.setChargeFund(userRechargeFundEntity.getChargeFundRmb());
                userMembershipEntity.setTotalMembershipCost(userMembershipEntity.getTotalMembershipCost().add(userRechargeFundEntity.getChargeFundRmb()));
            }
            userMembershipEntity.setChargeTime(new Date());
            //充值流水变动
            userRechargeFundEntity.setZfbOrderNum(alipayNotifyPojo.getTrade_no());
            userRechargeFundEntity.setNotifyInfo(alipayNotifyPojo.getTrade_status());
            userRechargeFundEntity.setConfirmTime(new Date());
            userRechargeFundEntity.setState(ConstantFund.SUCCESS);//交易成功
            userMembershipService.saveOrUpdate(userMembershipEntity);
            rechargeFundService.saveOrUpdate(userRechargeFundEntity);
        }
        return j;
    }
}
