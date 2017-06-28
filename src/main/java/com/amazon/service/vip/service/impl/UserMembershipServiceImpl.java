package com.amazon.service.vip.service.impl;

import com.alipay.api.domain.AlipayTradePayModel;
import com.amazon.service.fund.ConstantChargeType;
import com.amazon.service.fund.ConstantFund;
import com.amazon.service.fund.ConstantSource;
import com.amazon.service.fund.entity.UserFundEntity;
import com.amazon.service.promot.price.entity.PromotPriceEntity;
import com.amazon.service.recharge.entity.UserRechargeFundEntity;
import com.amazon.service.recharge.service.UserRechargeFundService;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.vip.constant.MemberConfig;
import com.amazon.service.vip.entity.UserMembershipEntity;
import com.amazon.service.vip.service.UserMembershipService;
import com.amazon.service.vip.vo.UserMembershipVo;
import com.amazon.system.alipay.ChargeFundConfig;
import com.amazon.system.alipay.service.AlipayService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.DateUtils.DateUtils;
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
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/10.
 */
@Service("userMembershipService")
@Transactional
public class UserMembershipServiceImpl extends BaseServiceImpl implements UserMembershipService {

    private static Logger logger = LogManager.getLogger(UserMembershipServiceImpl.class.getName());

    @Autowired
    private GlobalService globalService;

    @Autowired
    private UserRechargeFundService rechargeFundService;

    @Autowired
    private AlipayService alipayService;

    public AjaxJson getUserMembershipVo() {
        AjaxJson j = new AjaxJson();
        UserEntity userSession = globalService.getUserEntityFromSession();
        if (userSession == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请重新登录!");
            return j;
        }
        UserMembershipVo userMembershipVo = new UserMembershipVo();
        userMembershipVo.setAccount(userSession.getAccount());
        //获取账户资金信息
        DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
        userFundDetachedCriteria.add(Restrictions.eq("sellerId", userSession.getId()));
        List<UserFundEntity> userFundEntityList = globalService.getListByCriteriaQuery(userFundDetachedCriteria);
        if (CollectionUtils.isNotEmpty(userFundEntityList)) {
            UserFundEntity userFundEntity = userFundEntityList.get(0);
            userMembershipVo.setTotalFund(userFundEntity.getTotalFund());
            userMembershipVo.setUsableFund(userFundEntity.getUsableFund());
            userMembershipVo.setFreezeFund(userFundEntity.getFreezeFund());
        }

        DetachedCriteria userMembershipDetachedCriteria = DetachedCriteria.forClass(UserMembershipEntity.class);
        userMembershipDetachedCriteria.add(Restrictions.eq("sellerId", userSession.getId()));
        List<UserMembershipEntity> userMembershipEntityList = globalService.getListByCriteriaQuery(userMembershipDetachedCriteria);

        if (CollectionUtils.isEmpty(userMembershipEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能获取会员信息！");
            return j;
        }

        DetachedCriteria promotPriceDetachedCriteria = DetachedCriteria.forClass(PromotPriceEntity.class);
        List<PromotPriceEntity> promotPriceEntityList = globalService.getListByCriteriaQuery(promotPriceDetachedCriteria);
        if (CollectionUtils.isNotEmpty(promotPriceEntityList)) {
            PromotPriceEntity promotPriceEntity = promotPriceEntityList.get(0);
            userMembershipVo.setMemberShipMonthCost(promotPriceEntity.getMonthRent());
        }

        UserMembershipEntity userMembershipEntity = userMembershipEntityList.get(0);
        if (userMembershipEntity.getMembershipEndTime() == null) {
            userMembershipVo.setBeforeVip(false);
        } else {
            userMembershipVo.setBeforeVip(true);
            userMembershipVo.setMembershipEndTime(userMembershipEntity.getMembershipEndTime());
            if (DateUtils.compareTo(userMembershipEntity.getMembershipEndTime(), new Date()) > 0) {
                userMembershipVo.setVip(true);
            } else {
                userMembershipVo.setVip(false);
            }
        }
        j.setContent(userMembershipVo);
        return j;
    }

    public AjaxJson goToChargeVipMouth(UserMembershipVo userMembershipVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请重新登录！");
            return j;
        }

        DetachedCriteria promotPriceDetachedCriteria = DetachedCriteria.forClass(PromotPriceEntity.class);
        List<PromotPriceEntity> promotPriceEntityList = globalService.getListByCriteriaQuery(promotPriceDetachedCriteria);
        BigDecimal monthRent = null;
        BigDecimal exchangeRate = null;
        BigDecimal chargeFund = null;
        if (CollectionUtils.isNotEmpty(promotPriceEntityList) && promotPriceEntityList.get(0).getExchangeRate() != null) {
            monthRent = promotPriceEntityList.get(0).getMonthRent();
            exchangeRate = promotPriceEntityList.get(0).getExchangeRate();
        } else {
            logger.error("请在数据库中设置每月会员费用！");
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请在数据库中设置每月会员费用！");
            return j;
        }
        Integer memberShipMonth = userMembershipVo.getMemberShipMonth();

        if (memberShipMonth == null || memberShipMonth < 1) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("购买会员月份必须是正整数！");
            return j;
        }

        BigDecimal totalMonthRent = monthRent.multiply(new BigDecimal(memberShipMonth));
        chargeFund = totalMonthRent.divide(exchangeRate,2,BigDecimal.ROUND_DOWN);
        totalMonthRent = totalMonthRent.setScale(2, BigDecimal.ROUND_UP);

        AlipayTradePayModel alipayTradePayModel = new AlipayTradePayModel();
        alipayTradePayModel.setOutTradeNo(globalService.generateOrderNumber());
        alipayTradePayModel.setSubject(MemberConfig.SUBJECT);
        alipayTradePayModel.setBody(MemberConfig.BODY);
        alipayTradePayModel.setTotalAmount(totalMonthRent.toString());
        alipayTradePayModel.setProductCode(MemberConfig.PROJECTCODE);

        UserRechargeFundEntity userRechargeFundEntity = new UserRechargeFundEntity();
        userRechargeFundEntity.setMemberShipMonth(userMembershipVo.getMemberShipMonth());
        userRechargeFundEntity.setSellerId(userEntity.getId());
        userRechargeFundEntity.setPlatformOrderNum(alipayTradePayModel.getOutTradeNo());
        userRechargeFundEntity.setChargeFund(chargeFund);
        userRechargeFundEntity.setChargeFundRmb(totalMonthRent);
        userRechargeFundEntity.setExchangeRate(exchangeRate);
        userRechargeFundEntity.setState(ConstantFund.TO_BE_COMFIRM);//待确认
        userRechargeFundEntity.setChargeSource(ConstantSource.ZFB);//支付宝
        userRechargeFundEntity.setChargeType(ConstantChargeType.MEMBERSHIP_FUND);//会员充值
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
}
