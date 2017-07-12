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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sun.security.krb5.JavaxSecurityAuthKerberosAccess;

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
        promotOrderEvaluateExistDetachedCriteria.add(Restrictions.eq("asinId", promotOrderEvaluateFlowEntity.getAsinId()));
        List<PromotOrderEvaluateFlowEntity> promotOrderExistFlowEntityList = promotOrderService.getListByCriteriaQuery(promotOrderEvaluateExistDetachedCriteria);
        if (CollectionUtils.isNotEmpty(promotOrderExistFlowEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("亚马逊订单号和ASIN编号同时相同的评价已存在，勿重复录入！");
            return j;
        }
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteria.add(Restrictions.eq("asinId", promotOrderEvaluateFlowEntity.getAsinId()));
        promotOrderDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_Y));
        promotOrderDetachedCriteria.addOrder(Order.desc("addDate"));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(promotOrderDetachedCriteria);
        if (CollectionUtils.isEmpty(promotOrderEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能根据ASIN找到推广订单！");
            logger.log(Level.ERROR, "未能根据ASIN找到推广订单！----ASIN:" + promotOrderEvaluateFlowEntity.getAsinId());
            return j;
        }

        PromotOrderEntity currentPromotOrder = null;
        for (PromotOrderEntity promotOrderEntity : promotOrderEntityList) {
            if (promotOrderEntity.getNeedReviewNum().intValue() <= (promotOrderEntity.getBuyerNum().intValue())) {
                continue;
            }
            currentPromotOrder = promotOrderEntity;
        }
        if (currentPromotOrder == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("买家数量已经超出需要，无需添加新的评价！");
            return j;
        }
        //新增设置 评价状态
        promotOrderEvaluateFlowEntity.setPromotId(currentPromotOrder.getId());
        promotOrderEvaluateFlowEntity.setSellerId(currentPromotOrder.getSellerId());
        promotOrderEvaluateFlowEntity.setCreateTime(new Date());
        promotOrderEvaluateFlowEntity.setUpdateTime(new Date());
        promotOrderEvaluateFlowEntity.setState(Constants.EVALUATE_STATE_PENDING);

        currentPromotOrder.setBuyerNum(currentPromotOrder.getBuyerNum() + 1);
        currentPromotOrder.setUpdateTime(new Date());
        promotOrderService.saveOrUpdate(currentPromotOrder);
        promotOrderEvaluateFlowService.save(promotOrderEvaluateFlowEntity);
        return j;
    }

    public AjaxJson doAddEvaluateWithReviewUrl(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity) throws Exception {
        logger.info("----------doAddEvaluateWithReviewUrl--start--------" + JSON.toJSONString(promotOrderEvaluateFlowEntity));
        AjaxJson j = new AjaxJson();
        DetachedCriteria promotOrderEvaluateExistDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateExistDetachedCriteria.add(Restrictions.eq("amzOrderId", promotOrderEvaluateFlowEntity.getAmzOrderId()));
        promotOrderEvaluateExistDetachedCriteria.add(Restrictions.eq("asinId", promotOrderEvaluateFlowEntity.getAsinId()));
        List<PromotOrderEvaluateFlowEntity> promotOrderExistFlowEntityList = promotOrderService.getListByCriteriaQuery(promotOrderEvaluateExistDetachedCriteria);
        if (CollectionUtils.isNotEmpty(promotOrderExistFlowEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("亚马逊订单号和ASIN编号同时相同的评价已存在，勿重复录入！");
            return j;
        }
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteria.add(Restrictions.eq("asinId", promotOrderEvaluateFlowEntity.getAsinId()));
        promotOrderDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_Y));
        promotOrderDetachedCriteria.addOrder(Order.asc("addDate"));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(promotOrderDetachedCriteria);
        if (CollectionUtils.isEmpty(promotOrderEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能根据ASIN找到推广订单！");
            logger.log(Level.ERROR, "未能根据ASIN找到推广订单！----ASIN:" + promotOrderEvaluateFlowEntity.getAsinId());
            return j;
        }
        PromotOrderEntity currentPromotOrder = null;
        for (PromotOrderEntity promotOrderEntity : promotOrderEntityList) {
            if (promotOrderEntity.getNeedReviewNum().intValue() <= (promotOrderEntity.getBuyerNum().intValue())) {
                continue;
            } else {
                currentPromotOrder = promotOrderEntity;
                break;
            }
        }

        if (currentPromotOrder == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("买家数量已经超出需要，无需添加新的评价！");
            return j;
        }

        AmazonEvaluateReviewPojo amazonEvaluateReviewPojo = spiderService.spiderAmazonEvaluateReviewPojo(promotOrderEvaluateFlowEntity.getReviewUrl(), 4);
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
        if (!currentPromotOrder.getAsinId().equals(amazonEvaluateReviewPojo.getAsin())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("评价ASIN编号和商品ASIN不匹配！");
            logger.log(Level.ERROR, "评价ASIN编号和商品ASIN不匹配！----ASIN:" + amazonEvaluateReviewPojo.getAsin());
            return j;
        }
        //账目变动
        DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
        userFundDetachedCriteria.add(Restrictions.eq("sellerId", currentPromotOrder.getSellerId()));
        List<UserFundEntity> userFundEntityList = userFundService.getListByCriteriaQuery(userFundDetachedCriteria);
        logger.info("----------doAddEvaluateWithReviewUrl--userFundEntityList--------" + JSON.toJSONString(userFundEntityList));
        if (CollectionUtils.isEmpty(userFundEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("无法找到资金账户，请联系管理员！");
            return j;
        }
        UserFundEntity userFundEntity = userFundEntityList.get(0);
        BigDecimal totalPrice = currentPromotOrder.getReviewPrice().add(currentPromotOrder.getCashback());
        if (currentPromotOrder.getGuaranteeFund().compareTo(totalPrice) < 0) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("订单保证金不足！");
            return j;
        }

        //资金变动
        userFundEntity.setFreezeFund(userFundEntity.getFreezeFund().subtract(totalPrice));
        userFundEntity.setTotalFund(userFundEntity.getTotalFund().subtract(totalPrice));
        userFundEntity.setUpdateTime(new Date());
        //订单变动
        currentPromotOrder.setBuyerNum(currentPromotOrder.getBuyerNum() + 1);
        currentPromotOrder.setEvaluateNum(currentPromotOrder.getEvaluateNum() + 1);
        currentPromotOrder.setConsumption(currentPromotOrder.getConsumption().add(currentPromotOrder.getReviewPrice()));
        currentPromotOrder.setCashBackConsumption(currentPromotOrder.getCashBackConsumption().add(currentPromotOrder.getCashback()));
        currentPromotOrder.setGuaranteeFund(currentPromotOrder.getGuaranteeFund().subtract(totalPrice));
        currentPromotOrder.setUpdateTime(new Date());

        if (currentPromotOrder.getNeedReviewNum().intValue() <= (currentPromotOrder.getEvaluateNum().intValue())) {
            currentPromotOrder.setState(Constant.STATE_N);
        }

        //评价新增
        promotOrderEvaluateFlowEntity.setPromotId(currentPromotOrder.getId());
        promotOrderEvaluateFlowEntity.setSellerId(currentPromotOrder.getSellerId());
        promotOrderEvaluateFlowEntity.setAsinId(promotOrderEvaluateFlowEntity.getAsinId());
        promotOrderEvaluateFlowEntity.setBuyerId(promotOrderEvaluateFlowEntity.getBuyerId());
        promotOrderEvaluateFlowEntity.setState(Constants.EVALUATE_STATE_REVIEW);
        promotOrderEvaluateFlowEntity.setAmzOrderId(promotOrderEvaluateFlowEntity.getAmzOrderId());
        promotOrderEvaluateFlowEntity.setReviewCode(amazonEvaluateReviewPojo.getReviewCode());
        promotOrderEvaluateFlowEntity.setReviewContent(amazonEvaluateReviewPojo.getReviewContent());
        promotOrderEvaluateFlowEntity.setReviewUrl(amazonEvaluateReviewPojo.getReviewUrl());
        promotOrderEvaluateFlowEntity.setReviewStar(Double.parseDouble(amazonEvaluateReviewPojo.getReviewStar().trim().substring(0, 2)));
        promotOrderEvaluateFlowEntity.setReviewDate(amazonEvaluateReviewPojo.getReviewDate());
        promotOrderEvaluateFlowEntity.setComplaint(ComplaintConstant.COMPLAINT_ZERO);
        promotOrderEvaluateFlowEntity.setUpdateTime(new Date());
        promotOrderEvaluateFlowEntity.setCreateTime(new Date());

        try {
            userFundService.saveOrUpdate(userFundEntity);
            promotOrderEvaluateFlowService.save(promotOrderEvaluateFlowEntity);
            promotOrderService.saveOrUpdate(currentPromotOrder);
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


    public AjaxJson doAddReviewUrl(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowDb) {
        AjaxJson j = new AjaxJson();
        logger.info("----------PromotOrderEvaluateFlowEntity--start--------" + JSON.toJSONString(promotOrderEvaluateFlowEntity));
        logger.info("----------promotOrderEvaluateFlowDb--start--------" + JSON.toJSONString(promotOrderEvaluateFlowDb));
        PromotOrderEntity promotOrderEntity = promotOrderService.find(PromotOrderEntity.class, promotOrderEvaluateFlowDb.getPromotId());
        if (promotOrderEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setSuccess("订单已被删除或不存在!");
            return j;
        }
        if (promotOrderEntity.getState().equals(Constant.STATE_N)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setSuccess("订单已被关闭，不能追加评论链接！");
            return j;
        }

        AmazonEvaluateReviewPojo amazonEvaluateReviewPojo = spiderService.spiderAmazonEvaluateReviewPojo(promotOrderEvaluateFlowEntity.getReviewUrl(), 4);
        logger.info("----------doAddEvaluateWithReviewUrl--amazonEvaluateReviewPojo--------" + JSON.toJSONString(amazonEvaluateReviewPojo));
        if (StringUtils.isEmpty(amazonEvaluateReviewPojo.getReviewContent()) ||
                StringUtils.isEmpty(amazonEvaluateReviewPojo.getReviewStar()) ||
                StringUtils.isEmpty(amazonEvaluateReviewPojo.getReviewDate())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请确认给定的评价链接是否正确！");
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
        BigDecimal totalPrice = promotOrderEntity.getReviewPrice().add(promotOrderEntity.getCashback());
        if (promotOrderEntity.getGuaranteeFund().compareTo(totalPrice) < 0) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("订单保证金不足！");
            return j;
        }

        //资金变动
        userFundEntity.setFreezeFund(userFundEntity.getFreezeFund().subtract(totalPrice));
        userFundEntity.setTotalFund(userFundEntity.getTotalFund().subtract(totalPrice));
        userFundEntity.setUpdateTime(new Date());
        //订单变动
        promotOrderEntity.setEvaluateNum(promotOrderEntity.getEvaluateNum() + 1);
        promotOrderEntity.setConsumption(promotOrderEntity.getConsumption().add(promotOrderEntity.getReviewPrice()));
        promotOrderEntity.setCashBackConsumption(promotOrderEntity.getCashBackConsumption().add(promotOrderEntity.getCashback()));
        promotOrderEntity.setGuaranteeFund(promotOrderEntity.getGuaranteeFund().subtract(totalPrice));
        promotOrderEntity.setUpdateTime(new Date());

        if (promotOrderEntity.getNeedReviewNum().intValue() <= (promotOrderEntity.getEvaluateNum().intValue())) {
            promotOrderEntity.setState(Constant.STATE_N);
        }
        //评价新增
        promotOrderEvaluateFlowDb.setState(Constants.EVALUATE_STATE_REVIEW);
        promotOrderEvaluateFlowDb.setReviewCode(amazonEvaluateReviewPojo.getReviewCode());
        promotOrderEvaluateFlowDb.setReviewContent(amazonEvaluateReviewPojo.getReviewContent());
        promotOrderEvaluateFlowDb.setReviewUrl(amazonEvaluateReviewPojo.getReviewUrl());
        promotOrderEvaluateFlowDb.setReviewStar(Double.parseDouble(amazonEvaluateReviewPojo.getReviewStar().trim().substring(0, 2)));
        promotOrderEvaluateFlowDb.setReviewDate(amazonEvaluateReviewPojo.getReviewDate());
        promotOrderEvaluateFlowDb.setComplaint(ComplaintConstant.COMPLAINT_ZERO);
        promotOrderEvaluateFlowDb.setUpdateTime(new Date());
        try {
            logger.info("------userFundEntity-----"+JSON.toJSONString(userFundEntity));
            logger.info("------promotOrderEvaluateFlowEntity-----"+JSON.toJSONString(promotOrderEvaluateFlowEntity));
            logger.info("------promotOrderEntity-----"+JSON.toJSONString(promotOrderEntity));
            userFundService.saveOrUpdate(userFundEntity);
            promotOrderEvaluateFlowService.saveOrUpdate(promotOrderEvaluateFlowDb);
            promotOrderService.saveOrUpdate(promotOrderEntity);
        } catch (Exception e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存出错！");
            logger.error(e.fillInStackTrace());
        }
        return j;
    }
}
