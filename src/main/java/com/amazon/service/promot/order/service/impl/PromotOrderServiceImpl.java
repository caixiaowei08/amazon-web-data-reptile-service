package com.amazon.service.promot.order.service.impl;

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

import java.math.BigDecimal;
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
        AjaxJson j = new AjaxJson();
        //获取账户资金信息
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
        PromotPriceEntity promotPriceEntity = promotPriceService.find(PromotPriceEntity.class, 1);
        if (promotPriceEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未设置价格信息，请联系管理员！");
            return j;
        }

        //目标好评数
        Integer needReviewNum = promotOrderEntity.getNeedReviewNum();
        //好评价
        BigDecimal reviewPrice = promotPriceEntity.getReviewPrice();
        //总价
        BigDecimal totalPrice = (new BigDecimal(needReviewNum)).multiply(reviewPrice);
        if (totalPrice.compareTo(userFundEntity.getUsableFund()) > 0) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("可用余额不足，请充值！");
            return j;
        }

        userFundEntity.setUsableFund(userFundEntity.getUsableFund().subtract(totalPrice));
        userFundEntity.setFreezeFund(userFundEntity.getFreezeFund().add(totalPrice));
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
        promotOrderEntity.setNeedReviewNum(promotOrderEntity.getNeedReviewNum());
        promotOrderEntity.setDayReviewNum(promotOrderEntity.getDayReviewNum());
        promotOrderEntity.setBuyerNum(0);
        promotOrderEntity.setEvaluateNum(0);
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

        DetachedCriteria promotOrderEvaluateDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateDetachedCriteria.add(Restrictions.eq("sellerId", t.getSellerId()));
        promotOrderEvaluateDetachedCriteria.add(Restrictions.eq("promotId", t.getId()));
        promotOrderEvaluateDetachedCriteria.add(Restrictions.eq("state", Constants.EVALUATE_STATE_PENDING));
        List<PromotOrderEvaluateFlowEntity> promotOrderEvaluateFlowEntityList = promotOrderEvaluateFlowService.getListByCriteriaQuery(promotOrderEvaluateDetachedCriteria);
        if(CollectionUtils.isNotEmpty(promotOrderEvaluateFlowEntityList)){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该订单有pending状态的评价，不能关闭！");
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
