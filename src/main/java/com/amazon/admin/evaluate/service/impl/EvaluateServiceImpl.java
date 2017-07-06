package com.amazon.admin.evaluate.service.impl;

import com.alibaba.fastjson.JSON;
import com.amazon.admin.account.controller.AdminSystemController;
import com.amazon.admin.constant.Constants;
import com.amazon.admin.evaluate.ComplaintConstant;
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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.BeanUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/23.
 */
@Service("evaluateService")
@Transactional
public class EvaluateServiceImpl extends BaseServiceImpl implements EvaluateService {

    private static Logger logger = LogManager.getLogger(EvaluateServiceImpl.class.getName());

    @Autowired
    private PromotOrderService promotOrderService;

    @Autowired
    private PromotOrderEvaluateFlowService promotOrderEvaluateFlowService;

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private UserFundService userFundService;

    public AjaxJson doAddEvaluateWithNoReviewUrl(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity) {
        AjaxJson j = new AjaxJson();
        logger.info("---doAddEvaluateWithNoReviewUrl-start------" + JSON.toJSONString(promotOrderEvaluateFlowEntity));
        DetachedCriteria promotOrderEvaluateExistDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateExistDetachedCriteria.add(Restrictions.eq("amzOrderId", promotOrderEvaluateFlowEntity.getAmzOrderId()));
        List<PromotOrderEvaluateFlowEntity> promotOrderExistFlowEntityList = promotOrderService.getListByCriteriaQuery(promotOrderEvaluateExistDetachedCriteria);
        if (CollectionUtils.isNotEmpty(promotOrderExistFlowEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("不能重复录入亚马逊订单！");
            return j;
        }
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteria.add(Restrictions.eq("asinId", promotOrderEvaluateFlowEntity.getAsinId()));
        promotOrderDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_Y));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(promotOrderDetachedCriteria);
        if (CollectionUtils.isEmpty(promotOrderEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能根据ASIN找到推广订单！");
            logger.log(Level.ERROR, "未能根据ASIN找到推广订单！----ASIN:" + promotOrderEvaluateFlowEntity.getAsinId());
            return j;
        }
        PromotOrderEntity promotOrderEntity = promotOrderEntityList.get(0);
        //新增设置 评价状态
        promotOrderEvaluateFlowEntity.setPromotId(promotOrderEntity.getId());
        promotOrderEvaluateFlowEntity.setSellerId(promotOrderEntity.getSellerId());
        promotOrderEvaluateFlowEntity.setCreateTime(new Date());
        promotOrderEvaluateFlowEntity.setUpdateTime(new Date());
        promotOrderEvaluateFlowEntity.setState(Constants.EVALUATE_STATE_PENDING);
        promotOrderEntity.setBuyerNum(promotOrderEntity.getBuyerNum() + 1);
        promotOrderEntity.setUpdateTime(new Date());
        promotOrderService.saveOrUpdate(promotOrderEntity);
        promotOrderEvaluateFlowService.save(promotOrderEvaluateFlowEntity);
        return j;
    }

    public AjaxJson doAddEvaluateWithReviewUrl(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity) throws Exception {
        logger.info("----------doAddEvaluateWithReviewUrl--start--------" + JSON.toJSONString(promotOrderEvaluateFlowEntity));
        AjaxJson j = new AjaxJson();
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteria.add(Restrictions.eq("asinId", promotOrderEvaluateFlowEntity.getAsinId()));
        promotOrderDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_Y));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(promotOrderDetachedCriteria);
        logger.info("----------doAddEvaluateWithReviewUrl--promotOrderEntityList--------" + JSON.toJSONString(promotOrderEntityList));
        if (CollectionUtils.isEmpty(promotOrderEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能根据ASIN找到推广订单！");
            logger.log(Level.ERROR, "未能根据ASIN找到推广订单！----ASIN:" + promotOrderEvaluateFlowEntity.getAsinId());
            return j;
        }
        PromotOrderEntity promotOrderEntity = promotOrderEntityList.get(0);
        AmazonEvaluateReviewPojo amazonEvaluateReviewPojo = spiderService.spiderAmazonEvaluateReviewPojo(promotOrderEvaluateFlowEntity.getReviewUrl(), 2);
        logger.info("----------doAddEvaluateWithReviewUrl--amazonEvaluateReviewPojo--------" + JSON.toJSONString(amazonEvaluateReviewPojo));
        if (!StringUtils.hasText(amazonEvaluateReviewPojo.getReviewContent()) ||
                !StringUtils.hasText(amazonEvaluateReviewPojo.getReviewStar()) ||
                !StringUtils.hasText(amazonEvaluateReviewPojo.getReviewDate())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请确认给定的评价链接是否正确！");
            logger.log(Level.ERROR, "请确认给定的评价链接是否正确！----ASIN:" + promotOrderEvaluateFlowEntity.getAsinId());
            return j;
        }

        //开始校验 订单评价是否已经被使用
        DetachedCriteria promotOrderEvaluateDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateDetachedCriteria.add(Restrictions.eq("reviewCode", amazonEvaluateReviewPojo.getReviewCode()));
        List<PromotOrderEvaluateFlowEntity> promotOrderEvaluateFlowEntityList = promotOrderService.getListByCriteriaQuery(promotOrderEvaluateDetachedCriteria);
        if (CollectionUtils.isNotEmpty(promotOrderEvaluateFlowEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该评价已经被使用，请核对！");
            logger.log(Level.ERROR, "该评价已经被使用，请核对！----reviewCode:" + amazonEvaluateReviewPojo.getReviewCode());
            return j;
        }
        //ASIN 是否正确
        if (!promotOrderEntity.getAsinId().equals(amazonEvaluateReviewPojo.getAsin())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("评价ASIN编号和商品ASIN不匹配！");
            logger.log(Level.ERROR, "该评价已经被使用，请核对！----ASIN:" + amazonEvaluateReviewPojo.getAsin());
            return j;
        }
        //账目变动
        DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
        userFundDetachedCriteria.add(Restrictions.eq("sellerId", promotOrderEntity.getSellerId()));
        List<UserFundEntity> userFundEntityList = userFundService.getListByCriteriaQuery(userFundDetachedCriteria);
        logger.info("----------doAddEvaluateWithReviewUrl--userFundEntityList--------" + JSON.toJSONString(userFundEntityList));
        if (CollectionUtils.isEmpty(userFundEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("无法找到资金账户，请联系管理员！");
            return j;
        }
        UserFundEntity userFundEntity = userFundEntityList.get(0);
        BigDecimal tatalPrice = promotOrderEntity.getReviewPrice().add(promotOrderEntity.getCashback());
        if (userFundEntity.getFreezeFund().compareTo(tatalPrice) < 0
                || userFundEntity.getTotalFund().compareTo(tatalPrice) < 0) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("账户资金不足，请核实!");
            return j;
        }

        if (promotOrderEntity.getGuaranteeFund().compareTo(tatalPrice) < 0) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("订单保证金不足！");
            return j;
        }
        DetachedCriteria promotOrderEvaluateExistDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateExistDetachedCriteria.add(Restrictions.eq("promotId", promotOrderEntity.getId()));
        promotOrderEvaluateExistDetachedCriteria.add(Restrictions.eq("sellerId", promotOrderEntity.getSellerId()));
        promotOrderEvaluateExistDetachedCriteria.add(Restrictions.eq("asinId", amazonEvaluateReviewPojo.getAsin()));
        promotOrderEvaluateExistDetachedCriteria.add(Restrictions.eq("state", Constants.EVALUATE_STATE_PENDING));
        promotOrderEvaluateExistDetachedCriteria.add(Restrictions.eq("amzOrderId", promotOrderEvaluateFlowEntity.getAmzOrderId()));
        List<PromotOrderEvaluateFlowEntity> promotOrderExistFlowEntityList = promotOrderService.getListByCriteriaQuery(promotOrderEvaluateExistDetachedCriteria);
        PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntityExist = null;
        if (CollectionUtils.isNotEmpty(promotOrderExistFlowEntityList)) {
            promotOrderEvaluateFlowEntityExist = promotOrderExistFlowEntityList.get(0);
        }
        userFundEntity.setTotalFund(userFundEntity.getTotalFund().subtract(promotOrderEntity.getReviewPrice()).subtract(promotOrderEntity.getCashback()));
        userFundEntity.setFreezeFund(userFundEntity.getFreezeFund().subtract(promotOrderEntity.getReviewPrice()).subtract(promotOrderEntity.getCashback()));
        if (promotOrderEvaluateFlowEntityExist == null) {
            promotOrderEntity.setBuyerNum(promotOrderEntity.getBuyerNum() + 1);
        }
        promotOrderEntity.setEvaluateNum(promotOrderEntity.getEvaluateNum() + 1);
        promotOrderEntity.setConsumption(promotOrderEntity.getConsumption().add(promotOrderEntity.getReviewPrice()));
        promotOrderEntity.setCashBackConsumption(promotOrderEntity.getCashBackConsumption().add(promotOrderEntity.getCashback()));
        promotOrderEntity.setGuaranteeFund(promotOrderEntity.getGuaranteeFund().subtract(promotOrderEntity.getReviewPrice()).subtract(promotOrderEntity.getCashback()));

        //新增设置 评价状态
        if (promotOrderEvaluateFlowEntityExist != null) {
            promotOrderEvaluateFlowEntity.setId(promotOrderEvaluateFlowEntityExist.getId());
        }
        promotOrderEvaluateFlowEntity.setPromotId(promotOrderEntity.getId());
        promotOrderEvaluateFlowEntity.setSellerId(promotOrderEntity.getSellerId());
        promotOrderEvaluateFlowEntity.setAsinId(promotOrderEvaluateFlowEntity.getAsinId());
        promotOrderEvaluateFlowEntity.setState(Constants.EVALUATE_STATE_REVIEW);
        promotOrderEvaluateFlowEntity.setAmzOrderId(promotOrderEvaluateFlowEntity.getAmzOrderId());
        promotOrderEvaluateFlowEntity.setReviewCode(amazonEvaluateReviewPojo.getReviewCode());
        promotOrderEvaluateFlowEntity.setReviewContent(amazonEvaluateReviewPojo.getReviewContent());
        promotOrderEvaluateFlowEntity.setReviewUrl(amazonEvaluateReviewPojo.getReviewUrl());
        promotOrderEvaluateFlowEntity.setReviewStar(Double.parseDouble(amazonEvaluateReviewPojo.getReviewStar().trim().substring(0, 2)));
        promotOrderEvaluateFlowEntity.setReviewDate(amazonEvaluateReviewPojo.getReviewDate());
        promotOrderEvaluateFlowEntity.setComplaint(ComplaintConstant.COMPLAINT_ZERO);
        promotOrderEvaluateFlowEntity.setBuyerId("-1");
        if (promotOrderEvaluateFlowEntityExist == null) {
            promotOrderEvaluateFlowEntity.setCreateTime(new Date());
        }

        //完成订单指定数量的评价 订单关闭
        if (promotOrderEntity.getNeedReviewNum().equals(promotOrderEntity.getEvaluateNum())) {
            promotOrderEntity.setState(Constant.STATE_N);
        }

        try {
            promotOrderEvaluateFlowEntity.setUpdateTime(new Date());
            userFundService.saveOrUpdate(userFundEntity);
            promotOrderService.saveOrUpdate(promotOrderEntity);
            if (promotOrderEvaluateFlowEntityExist == null) {
                promotOrderEvaluateFlowService.save(promotOrderEvaluateFlowEntity);
            } else {
                BeanUtils.copyBeanNotNull2Bean(promotOrderEvaluateFlowEntity, promotOrderEvaluateFlowEntityExist);
                promotOrderEvaluateFlowService.saveOrUpdate(promotOrderEvaluateFlowEntityExist);
            }
        } catch (Exception e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存出错！");
            logger.info(e.toString());
        }

        return j;
    }

    public AjaxJson doDelPromotOrderEvaluate(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity) {
        AjaxJson j = new AjaxJson();
        PromotOrderEntity promotOrderEntity = promotOrderService.find(PromotOrderEntity.class, promotOrderEvaluateFlowEntity.getPromotId());
        if (promotOrderEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("评价对应推广订单不存在！");
            return j;
        }
        if (promotOrderEvaluateFlowEntity.getState().equals(Constants.EVALUATE_STATE_PENDING)) {
            promotOrderEntity.setBuyerNum(promotOrderEntity.getBuyerNum() - 1);
            promotOrderEntity.setUpdateTime(new Date());
            promotOrderService.saveOrUpdate(promotOrderEntity);
            promotOrderEvaluateFlowService.delete(promotOrderEvaluateFlowEntity);

        } else if (promotOrderEvaluateFlowEntity.getState().equals(Constants.EVALUATE_STATE_REVIEW)) {
            //账目变动
            DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
            userFundDetachedCriteria.add(Restrictions.eq("sellerId", promotOrderEntity.getSellerId()));
            List<UserFundEntity> userFundEntityList = userFundService.getListByCriteriaQuery(userFundDetachedCriteria);
            if (CollectionUtils.isEmpty(userFundEntityList)) {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("无法找到资金账户，请联系管理员！");
                return j;
            }
            UserFundEntity userFundEntity = userFundEntityList.get(0);
            userFundEntity.setTotalFund(userFundEntity.getTotalFund().add(promotOrderEntity.getCashback())
                    .add(promotOrderEntity.getReviewPrice()));

            if (promotOrderEntity.getState().equals(Constant.STATE_Y)) {
                userFundEntity.setFreezeFund(userFundEntity.getFreezeFund().add(promotOrderEntity.getCashback())
                        .add(promotOrderEntity.getReviewPrice()));
            } else {
                userFundEntity.setUsableFund(userFundEntity.getUsableFund().add(promotOrderEntity.getCashback())
                        .add(promotOrderEntity.getReviewPrice()));
            }
            userFundEntity.setUpdateTime(new Date());

            if (promotOrderEntity.getState().equals(Constant.STATE_Y)) {
                promotOrderEntity.setGuaranteeFund(
                        promotOrderEntity.getGuaranteeFund()
                                .add(promotOrderEntity.getCashback())
                                .add(promotOrderEntity.getReviewPrice())
                );
            }
            promotOrderEntity.setEvaluateNum(promotOrderEntity.getEvaluateNum() - 1);
            promotOrderEntity.setBuyerNum(promotOrderEntity.getBuyerNum() - 1);
            promotOrderEntity.setCashBackConsumption(promotOrderEntity.getCashBackConsumption().subtract(promotOrderEntity.getCashback()));
            promotOrderEntity.setConsumption(promotOrderEntity.getConsumption().subtract(promotOrderEntity.getReviewPrice()));
            promotOrderEntity.setUpdateTime(new Date());
            userFundService.saveOrUpdate(userFundEntity);
            promotOrderService.saveOrUpdate(promotOrderEntity);
            promotOrderEvaluateFlowService.delete(promotOrderEvaluateFlowEntity);
        }
        return j;
    }
}
