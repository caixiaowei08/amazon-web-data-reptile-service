package com.amazon.service.user.service.impl;

import com.amazon.admin.constant.Constants;
import com.amazon.service.fund.entity.UserFundEntity;
import com.amazon.service.fund.service.UserFundService;
import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.flow.service.PromotOrderEvaluateFlowService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.user.service.UserService;
import com.amazon.service.user.vo.UserBaseInfoVo;
import com.amazon.service.vip.entity.UserMembershipEntity;
import com.amazon.service.vip.service.UserMembershipService;
import com.amazon.system.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.DateUtils.DateUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/6.
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    @Autowired
    private PromotOrderService promotOrderService;

    @Autowired
    private PromotOrderEvaluateFlowService promotOrderEvaluateFlowService;

    @Autowired
    private GlobalService globalService;

    @Autowired
    private UserFundService userFundService;

    @Autowired
    private UserMembershipService userMembershipService;


    public UserEntity doRegister(UserEntity userEntity) {
        //账户保存
        Integer id = (Integer) globalService.save(userEntity);
        UserEntity user = globalService.find(UserEntity.class, id);
        //对应资金信息记录 初始化
        UserFundEntity userFundEntity = new UserFundEntity();
        userFundEntity.setSellerId(user.getId());
        userFundEntity.setTotalFund(new BigDecimal(0.0000));
        userFundEntity.setUsableFund(new BigDecimal(0.0000));
        userFundEntity.setFreezeFund(new BigDecimal(0.0000));
        userFundEntity.setCreateTime(new Date());
        userFundEntity.setUpdateTime(new Date());
        userFundService.save(userFundEntity);
        //初始化会员账户 依据时间判定
        UserMembershipEntity userMembershipEntity = new UserMembershipEntity();
        userMembershipEntity.setSellerId(user.getId());
        userMembershipEntity.setTotalMembershipCost(new BigDecimal(0.0000));
        userMembershipService.save(userMembershipEntity);
        return user;
    }

    public UserBaseInfoVo doGetBaseUserInfo(UserEntity userEntity) {
        UserBaseInfoVo userBaseInfoVo = new UserBaseInfoVo();
        //开启状态的订单
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteria.add(Restrictions.eq("sellerId", userEntity.getId()));
        promotOrderDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_Y));
        userBaseInfoVo.setActiveOrderNum(promotOrderService.getRowCount(promotOrderDetachedCriteria));
        //今日的评论个数
        DetachedCriteria promotOrderEvaluateFlowDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateFlowDetachedCriteria.add(Restrictions.eq("sellerId",userEntity.getId()));
        promotOrderEvaluateFlowDetachedCriteria.add(Restrictions.eq("state", Constants.EVALUATE_STATE_REVIEW));
        promotOrderEvaluateFlowDetachedCriteria.add(Restrictions.ge("updateTime", DateUtils.getBeginOfDate()));
        userBaseInfoVo.setTodayEvaluateNum(promotOrderEvaluateFlowService.getRowCount(promotOrderEvaluateFlowDetachedCriteria));
        //求和联系人数
        DetachedCriteria promotOrderDetachedCriteriaBuyerNum = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteriaBuyerNum.add(Restrictions.eq("sellerId", userEntity.getId()));
        promotOrderDetachedCriteriaBuyerNum.setProjection(Projections.sum("buyerNum"));
        userBaseInfoVo.setBuyerNum(promotOrderService.getRowSum(promotOrderDetachedCriteriaBuyerNum));
        //求和总好评数
        DetachedCriteria promotOrderEvaluateFlowDetachedCriteriaAll = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateFlowDetachedCriteriaAll.add(Restrictions.eq("sellerId", userEntity.getId()));
        promotOrderEvaluateFlowDetachedCriteriaAll.add(Restrictions.eq("state", Constants.EVALUATE_STATE_REVIEW));
        userBaseInfoVo.setTotalEvaluateNum(promotOrderEvaluateFlowService.getRowCount(promotOrderEvaluateFlowDetachedCriteriaAll));
        //完成订单数
        DetachedCriteria promotOrderDetachedCriteriaDetailClose = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteriaDetailClose.add(Restrictions.eq("sellerId", userEntity.getId()));
        promotOrderDetachedCriteriaDetailClose.add(Restrictions.not(Restrictions.eq("state", Constant.STATE_Y)));
        userBaseInfoVo.setHistoryOrderNum(promotOrderService.getRowCount(promotOrderDetachedCriteriaDetailClose));
        //求订单消费记录
        DetachedCriteria promotOrderDetachedCriteriaConsumption = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteriaConsumption.add(Restrictions.eq("sellerId", userEntity.getId()));
        promotOrderDetachedCriteriaConsumption.setProjection(Projections.sum("consumption"));
        userBaseInfoVo.setTotalConsumption(promotOrderService.getRowBigDecimalSum(promotOrderDetachedCriteriaConsumption));
        userBaseInfoVo.setAccount(userEntity.getAccount());
        //计划订单计算
        DetachedCriteria detachedCriteriaConsumptionPlanBuyerNum = DetachedCriteria.forClass(PromotOrderEntity.class);
        detachedCriteriaConsumptionPlanBuyerNum.add(Restrictions.eq("sellerId", userEntity.getId()));
        detachedCriteriaConsumptionPlanBuyerNum.setProjection(Projections.sum("needReviewNum"));
        userBaseInfoVo.setPlanBuyerNum(promotOrderService.getRowSumPlanBuyerNum(detachedCriteriaConsumptionPlanBuyerNum));
        //个人资金计算
        DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
        userFundDetachedCriteria.add(Restrictions.eq("sellerId", userEntity.getId()));
        List<UserFundEntity> userFundEntityList = userFundService.getListByCriteriaQuery(userFundDetachedCriteria);
        if (CollectionUtils.isNotEmpty(userFundEntityList)) {
            UserFundEntity userFundEntity = userFundEntityList.get(0);
            userBaseInfoVo.setTotalFund(userFundEntity.getTotalFund());
            userBaseInfoVo.setUsableFund(userFundEntity.getUsableFund());
            userBaseInfoVo.setFreezeFund(userFundEntity.getFreezeFund());
        }
        DetachedCriteria userMembershipDetachedCriteria = DetachedCriteria.forClass(UserMembershipEntity.class);
        userMembershipDetachedCriteria.add(Restrictions.eq("sellerId", userEntity.getId()));
        List<UserMembershipEntity> userMembershipEntityList = userMembershipService.getListByCriteriaQuery(userMembershipDetachedCriteria);
        if (CollectionUtils.isNotEmpty(userMembershipEntityList)) {
            UserMembershipEntity userMembershipEntity = userMembershipEntityList.get(0);
            if (userMembershipEntity.getMembershipEndTime() == null) {
                userBaseInfoVo.setBeforeVip(false);
            } else {
                userBaseInfoVo.setBeforeVip(true);
                userBaseInfoVo.setMembershipEndTime(userMembershipEntity.getMembershipEndTime());
                if (DateUtils.compareTo(userMembershipEntity.getMembershipEndTime(), new Date()) > 0) {
                    userBaseInfoVo.setVip(true);
                } else {
                    userBaseInfoVo.setVip(false);
                }
            }
        }
        return userBaseInfoVo;
    }

}
