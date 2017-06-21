package com.amazon.service.user.service.impl;

import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.flow.service.PromotOrderEvaluateFlowService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.user.service.UserService;
import com.amazon.service.user.vo.UserBaseInfoVo;
import com.amazon.system.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.utils.DateUtils.DateUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public UserBaseInfoVo doGetBaseUserInfo(UserEntity userEntity) {
        UserBaseInfoVo userBaseInfoVo = new UserBaseInfoVo();
        //开启状态的订单
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteria.add(Restrictions.eq("sellerId", userEntity.getId()));
        promotOrderDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_Y));
        userBaseInfoVo.setActiveOrderNum(promotOrderService.getRowCount(promotOrderDetachedCriteria));
        //今日的评论个数
        DetachedCriteria promotOrderDetachedCriteriaDetail = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteriaDetail.add(Restrictions.eq("sellerId", userEntity.getId()));
        promotOrderDetachedCriteriaDetail.add(Restrictions.eq("state", Constant.STATE_Y));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(promotOrderDetachedCriteriaDetail);
        if (CollectionUtils.isNotEmpty(promotOrderEntityList)) {
            DetachedCriteria promotOrderEvaluateFlowDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
            List<Integer> params = new ArrayList<Integer>();
            for (PromotOrderEntity promotOrderEntity :promotOrderEntityList) {
                params.add(promotOrderEntity.getId());
            }
            promotOrderEvaluateFlowDetachedCriteria.add(Restrictions.in("promotId",params));
            promotOrderEvaluateFlowDetachedCriteria.add(Restrictions.ge("createTime", DateUtils.getBeginOfDate()));
            userBaseInfoVo.setTodayEvaluateNum(promotOrderEvaluateFlowService.getRowCount(promotOrderEvaluateFlowDetachedCriteria));
        } else {
            userBaseInfoVo.setTodayEvaluateNum(0);
        }
        //求和联系人数
        DetachedCriteria promotOrderDetachedCriteriaBuyerNum = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteriaBuyerNum.add(Restrictions.eq("sellerId", userEntity.getId()));
        promotOrderDetachedCriteriaBuyerNum.add(Restrictions.eq("state", Constant.STATE_Y));
        promotOrderDetachedCriteriaBuyerNum.setProjection(Projections.sum("buyerNum"));
        userBaseInfoVo.setBuyerNum(promotOrderService.getRowSum(promotOrderDetachedCriteriaBuyerNum));
        //求和总好评数
        DetachedCriteria promotOrderEvaluateFlowDetachedCriteriaAll = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateFlowDetachedCriteriaAll.add(Restrictions.in("sellerId",userEntity.getId()));
        userBaseInfoVo.setTotalEvaluateNum(promotOrderEvaluateFlowService.getRowCount(promotOrderEvaluateFlowDetachedCriteriaAll));
        //完成订单数
        DetachedCriteria promotOrderDetachedCriteriaDetailClose = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteriaDetailClose.add(Restrictions.eq("sellerId", userEntity.getId()));
        promotOrderDetachedCriteriaDetailClose.add(Restrictions.not(Restrictions.eq("state", Constant.STATE_Y)));
        userBaseInfoVo.setHistoryOrderNum(promotOrderService.getRowCount(promotOrderDetachedCriteriaDetailClose));
        //求订单从消费记录
        DetachedCriteria promotOrderDetachedCriteriaConsumption = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteriaConsumption.add(Restrictions.eq("sellerId", userEntity.getId()));
        promotOrderDetachedCriteriaConsumption.setProjection(Projections.sum("consumption"));
        userBaseInfoVo.setTotalConsumption(promotOrderService.getRowBigDecimalSum(promotOrderDetachedCriteriaConsumption));
        return userBaseInfoVo;
    }
}
