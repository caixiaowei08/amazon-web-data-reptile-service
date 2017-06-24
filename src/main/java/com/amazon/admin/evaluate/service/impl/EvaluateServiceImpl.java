package com.amazon.admin.evaluate.service.impl;

import com.amazon.admin.constant.Constants;
import com.amazon.admin.evaluate.service.EvaluateService;
import com.amazon.service.fund.entity.UserFundEntity;
import com.amazon.service.fund.service.UserFundService;
import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.flow.service.PromotOrderEvaluateFlowService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.service.spider.pojo.AmazonEvaluateReviewPojo;
import com.amazon.service.spider.service.SpiderService;
import com.amazon.service.vip.entity.UserMembershipEntity;
import com.amazon.system.Constant;
import org.apache.commons.collections.CollectionUtils;
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
 * Created by User on 2017/6/23.
 */
@Service("evaluateService")
@Transactional
public class EvaluateServiceImpl extends BaseServiceImpl implements EvaluateService {

    @Autowired
    private PromotOrderService promotOrderService;

    @Autowired
    private PromotOrderEvaluateFlowService promotOrderEvaluateFlowService;

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private UserFundService userFundService;

    public AjaxJson doAddEvaluateWithNoReviewUrl(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity) {
        //根据ASIN 查询有效订单
        //获取账户会员信息
        AjaxJson j = new AjaxJson();
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteria.add(Restrictions.eq("asinId", promotOrderEvaluateFlowEntity.getAsinId()));
        promotOrderDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_Y));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(promotOrderDetachedCriteria);
        if (CollectionUtils.isEmpty(promotOrderEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setSuccess("未能根据ASIN找到推广订单！");
            return j;
        }
        PromotOrderEntity promotOrderEntity = promotOrderEntityList.get(0);
        //新增设置 评价状态
        promotOrderEvaluateFlowEntity.setPromotId(promotOrderEntity.getId());
        promotOrderEvaluateFlowEntity.setSellerId(promotOrderEntity.getSellerId());
        promotOrderEvaluateFlowEntity.setCreateTime(new Date());
        promotOrderEvaluateFlowEntity.setUpdateTime(new Date());
        promotOrderEvaluateFlowEntity.setState(Constants.EVALUATE_STATE_PENDING);

        promotOrderEvaluateFlowService.save(promotOrderEvaluateFlowEntity);

        return j;
    }

    public AjaxJson doAddEvaluateWithReviewUrl(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity) {
        //根据ASIN 查询有效订单
        //获取账户会员信息
        AjaxJson j = new AjaxJson();
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteria.add(Restrictions.eq("asinId", promotOrderEvaluateFlowEntity.getAsinId()));
        promotOrderDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_Y));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(promotOrderDetachedCriteria);
        if (CollectionUtils.isEmpty(promotOrderEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setSuccess("未能根据ASIN找到推广订单！");
            return j;
        }
        PromotOrderEntity promotOrderEntity = promotOrderEntityList.get(0);
        AmazonEvaluateReviewPojo amazonEvaluateReviewPojo = spiderService.spiderAmazonEvaluateReviewPojo(promotOrderEvaluateFlowEntity.getReviewUrl(), 2);
        //开始校验 订单评价是否已经被使用
        DetachedCriteria promotOrderEvaluateDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateDetachedCriteria.add(Restrictions.eq("reviewCode", amazonEvaluateReviewPojo.getReviewCode()));
        List<PromotOrderEvaluateFlowEntity> promotOrderEvaluateFlowEntityList = promotOrderService.getListByCriteriaQuery(promotOrderEvaluateDetachedCriteria);
        if (CollectionUtils.isNotEmpty(promotOrderEvaluateFlowEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setSuccess("该评价已经被使用，请核对！");
            return j;
        }
        //ASIN 是否正确
        if (!promotOrderEntity.getAsinId().equals(amazonEvaluateReviewPojo.getAsin())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setSuccess("评价ASIN编号和商品ASIN不匹配！");
            return j;
        }

        //账目变动
        DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
        userFundDetachedCriteria.add(Restrictions.eq("sellerId", promotOrderEntity.getSellerId()));
        List<UserFundEntity> userFundEntityList = promotOrderService.getListByCriteriaQuery(userFundDetachedCriteria);
        if (CollectionUtils.isEmpty(userFundEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setSuccess("无法找到资金账户，请联系管理员！");
            return j;
        }
        UserFundEntity userFundEntity = userFundEntityList.get(0);
        BigDecimal reviewPrice = promotOrderEntity.getReviewPrice();
        if (userFundEntity.getFreezeFund().compareTo(reviewPrice) < 0
                || userFundEntity.getTotalFund().compareTo(reviewPrice)< 0) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setSuccess("账户资金不足，请核实!");
            return j;
        }

        if (promotOrderEntity.getGuaranteeFund().compareTo(reviewPrice) < 0) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setSuccess("订单保证金不足！");
            return j;
        }

        userFundEntity.setTotalFund(userFundEntity.getTotalFund().subtract(reviewPrice));
        userFundEntity.setFreezeFund(userFundEntity.getFreezeFund().subtract(reviewPrice));
        promotOrderEntity.setEvaluateNum(promotOrderEntity.getEvaluateNum() + 1);
        promotOrderEntity.setConsumption(promotOrderEntity.getConsumption().add(reviewPrice));
        promotOrderEntity.setGuaranteeFund(promotOrderEntity.getGuaranteeFund().subtract(reviewPrice));
        //新增设置 评价状态
        promotOrderEvaluateFlowEntity.setPromotId(promotOrderEntity.getId());
        promotOrderEvaluateFlowEntity.setSellerId(promotOrderEntity.getSellerId());
        promotOrderEvaluateFlowEntity.setAsinId(promotOrderEvaluateFlowEntity.getAsinId());
        promotOrderEvaluateFlowEntity.setState(Constants.EVALUATE_STATE_REVIEW);
        promotOrderEvaluateFlowEntity.setAmzOrderId(promotOrderEvaluateFlowEntity.getAmzOrderId());
        promotOrderEvaluateFlowEntity.setReviewCode(amazonEvaluateReviewPojo.getReviewCode());
        promotOrderEvaluateFlowEntity.setReviewContent(amazonEvaluateReviewPojo.getReviewContent());
        promotOrderEvaluateFlowEntity.setReviewUrl(amazonEvaluateReviewPojo.getReviewUrl());
        promotOrderEvaluateFlowEntity.setReviewStar(Double.parseDouble(amazonEvaluateReviewPojo.getReviewStar().trim().substring(0,2)));
        promotOrderEvaluateFlowEntity.setReviewDate(amazonEvaluateReviewPojo.getReviewDate());
        promotOrderEvaluateFlowEntity.setComplaint(0);
        promotOrderEvaluateFlowEntity.setBuyerId("-1");
        promotOrderEvaluateFlowEntity.setCreateTime(new Date());
        promotOrderEvaluateFlowEntity.setUpdateTime(new Date());
        userFundService.saveOrUpdate(userFundEntity);
        promotOrderService.saveOrUpdate(promotOrderEntity);
        promotOrderEvaluateFlowService.saveOrUpdate(promotOrderEvaluateFlowEntity);
        return j;
    }
}
