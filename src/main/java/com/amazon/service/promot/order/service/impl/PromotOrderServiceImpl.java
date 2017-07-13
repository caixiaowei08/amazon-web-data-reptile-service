package com.amazon.service.promot.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.amazon.admin.constant.Constants;
import com.amazon.service.fund.entity.UserFundEntity;
import com.amazon.service.fund.service.UserFundService;
import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.flow.service.PromotOrderEvaluateFlowService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.service.promot.price.entity.PromotPriceEntity;
import com.amazon.service.promot.price.service.PromotPriceService;
import com.amazon.service.spider.pojo.AmazonPageInfoPojo;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.vip.entity.UserMembershipEntity;
import com.amazon.service.vip.service.UserMembershipService;
import com.amazon.system.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.global.service.GlobalService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/10.
 */
@Service("promotOrderService")
@Transactional
public class PromotOrderServiceImpl extends BaseServiceImpl implements PromotOrderService {

    private static Logger logger = LogManager.getLogger(PromotOrderServiceImpl.class.getName());

    @Autowired
    private PromotOrderService promotOrderService;

    @Autowired
    private UserFundService userFundService;

    @Autowired
    private UserMembershipService userMembershipService;

    @Autowired
    private PromotPriceService promotPriceService;

    @Autowired
    private GlobalService globalService;

    @Autowired
    private PromotOrderEvaluateFlowService promotOrderEvaluateFlowService;


    public AjaxJson doAddNewPromot(UserEntity userEntity, AmazonPageInfoPojo amazonPageInfoPojo, PromotOrderEntity promotOrderEntity) {
        logger.info("----创建订单开始-promotOrderEntity---"+ JSON.toJSONString(promotOrderEntity));
        logger.info("----创建订单开始-userEntity---"+ JSON.toJSONString(userEntity));
        logger.info("----创建订单开始-amazonPageInfoPojo---"+ JSON.toJSONString(amazonPageInfoPojo));
        AjaxJson j = new AjaxJson();
        DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
        userFundDetachedCriteria.add(Restrictions.eq("sellerId", userEntity.getId()));
        List<UserFundEntity> userFundEntityList = userFundService.getListByCriteriaQuery(userFundDetachedCriteria);
        if (CollectionUtils.isEmpty(userFundEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能获取账户资金信息！");
            return j;
        }
        //获取账户会员信息
        DetachedCriteria userMembershipDetachedCriteria = DetachedCriteria.forClass(UserMembershipEntity.class);
        userMembershipDetachedCriteria.add(Restrictions.eq("sellerId", userEntity.getId()));
        List<UserMembershipEntity> userMembershipEntityList = userMembershipService.getListByCriteriaQuery(userMembershipDetachedCriteria);
        if (CollectionUtils.isEmpty(userMembershipEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能获取账户会员信息！");
            return j;
        }
        UserFundEntity userFundEntity = userFundEntityList.get(0);
        UserMembershipEntity userMembershipEntity = userMembershipEntityList.get(0);
        Date membershipEndTime = userMembershipEntity.getMembershipEndTime();
        if (membershipEndTime == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("您还不是会员,请开通会员！");
            return j;
        }
        if (membershipEndTime.compareTo(new Date()) < 0) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("您的会员已到期，请购买会员！");
            return j;
        }
        DetachedCriteria promotPriceEntityDetachedCriteria = DetachedCriteria.forClass(PromotPriceEntity.class);
        List<PromotPriceEntity> promotPriceEntityList = promotPriceService.getListByCriteriaQuery(promotPriceEntityDetachedCriteria);
        if (CollectionUtils.isEmpty(promotPriceEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未设置价格信息，请联系管理员！");
            return j;
        }
        PromotPriceEntity promotPriceEntity = promotPriceEntityList.get(0);
        //目标好评数
        Integer needReviewNum = promotOrderEntity.getNeedReviewNum();
        //好评价
        BigDecimal reviewPrice = promotPriceEntity.getReviewPrice();
        String cashback = promotOrderEntity.getSalePrice().trim();
        try {
            if (StringUtils.hasText(cashback)) {
                promotOrderEntity.setCashback(new BigDecimal(cashback.substring(1, cashback.length())));
            } else {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("未能获取商品价格，请联系客服！");
                return j;
            }

        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("商品价格解析有误，请联系客服！");
            return j;
        }

        //总价
        BigDecimal totalPrice = (new BigDecimal(needReviewNum)).multiply(reviewPrice.add(promotOrderEntity.getCashback()));
        if (totalPrice.compareTo(userFundEntity.getUsableFund()) > 0) {
            j.setSuccess(AjaxJson.CODE_RECHARGE);
            j.setMsg("可用余额不足，请先充值" + new DecimalFormat("#.00").format(totalPrice.subtract(userFundEntity.getUsableFund())) + "美元");
            j.setContent(new DecimalFormat("#.00").format(totalPrice.subtract(userFundEntity.getUsableFund())));
            return j;
        }

        userFundEntity.setUsableFund(userFundEntity.getUsableFund().subtract(totalPrice));
        userFundEntity.setFreezeFund(userFundEntity.getFreezeFund().add(totalPrice));
        userFundEntity.setUpdateTime(new Date());
        userFundService.saveOrUpdate(userFundEntity);
        //检查资金情况
        promotOrderEntity.setSellerId(userEntity.getId());
        promotOrderEntity.setAsinId(amazonPageInfoPojo.getAsin());
        promotOrderEntity.setProductUrl(amazonPageInfoPojo.getPageUrl());
        promotOrderEntity.setProductTitle(amazonPageInfoPojo.getProductTitle());
        promotOrderEntity.setBrand(amazonPageInfoPojo.getBrand());
        promotOrderEntity.setThumbnail(amazonPageInfoPojo.getLandingImage());
        promotOrderEntity.setState(Constant.STATE_Y);
        promotOrderEntity.setSalePrice(amazonPageInfoPojo.getPriceblockSaleprice());
        promotOrderEntity.setAddDate(new Date());
        promotOrderEntity.setFinishDate(promotOrderEntity.getFinishDate());
        promotOrderEntity.setReviewPrice(reviewPrice);
        promotOrderEntity.setGuaranteeFund(totalPrice);
        promotOrderEntity.setConsumption(new BigDecimal("0.00"));
        promotOrderEntity.setCashBackConsumption(new BigDecimal("0.00"));
        promotOrderEntity.setNeedReviewNum(promotOrderEntity.getNeedReviewNum());
        promotOrderEntity.setDayReviewNum(promotOrderEntity.getDayReviewNum());
        promotOrderEntity.setBuyerNum(0);
        promotOrderEntity.setEvaluateNum(0);
        promotOrderEntity.setRemark(promotOrderEntity.getRemark().trim());
        promotOrderEntity.setCreateTime(new Date());
        promotOrderEntity.setUpdateTime(new Date());
        promotOrderService.save(promotOrderEntity);
        return j;
    }

    public AjaxJson doClosedPromotOrderById(PromotOrderEntity promotOrderEntity) {
        AjaxJson j = new AjaxJson();
        if (promotOrderEntity == null || promotOrderEntity.getId() == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择您需要关闭的订单！");
            logger.error("PromotOrderEntity输入错误参数");
            return j;
        }

        PromotOrderEntity t = globalService.find(PromotOrderEntity.class, promotOrderEntity.getId());
        if (t.getState().equals(Constant.STATE_N)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("订单已经关闭！");
            return j;
        }
        DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
        userFundDetachedCriteria.add(Restrictions.eq("sellerId", t.getSellerId()));
        List<UserFundEntity> userFundEntityList = userFundService.getListByCriteriaQuery(userFundDetachedCriteria);
        if (CollectionUtils.isEmpty(userFundEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未找到账户信息！");
            return j;
        }
        UserFundEntity userFundEntity = userFundEntityList.get(0);
        BigDecimal guaranteeFund = t.getGuaranteeFund();
        t.setState(Constant.STATE_N);
        t.setUpdateTime(new Date());
        userFundEntity.setFreezeFund(userFundEntity.getFreezeFund().subtract(guaranteeFund));
        userFundEntity.setUsableFund(userFundEntity.getUsableFund().add(guaranteeFund));
        userFundEntity.setUpdateTime(new Date());
        userFundService.saveOrUpdate(userFundEntity);
        promotOrderService.saveOrUpdate(t);
        return j;
    }
}
