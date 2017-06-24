package com.amazon.admin.evaluate.service.impl;

import com.amazon.admin.constant.Constants;
import com.amazon.admin.evaluate.service.EvaluateService;
import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.flow.service.PromotOrderEvaluateFlowService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
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
    private GlobalService globalService;

    public AjaxJson doAddEvaluateWithNoReviewUrl(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity) {
        //根据ASIN 查询有效订单
        //获取账户会员信息
        AjaxJson j = new AjaxJson();
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteria.add(Restrictions.eq("asinId", promotOrderEvaluateFlowEntity.getAsinId()));
        promotOrderDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_Y));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(promotOrderDetachedCriteria);
        if(CollectionUtils.isEmpty(promotOrderEntityList)){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setSuccess("未能根据ASIN找到推广订单！");
            return j;
        }
        PromotOrderEntity promotOrderEntity = promotOrderEntityList .get(0);
        //新增设置 评价状态
        promotOrderEvaluateFlowEntity.setPromotId(promotOrderEntity.getId());
        promotOrderEvaluateFlowEntity.setSellerId(promotOrderEntity.getSellerId());
        promotOrderEvaluateFlowEntity.setCreateTime(new Date());
        promotOrderEvaluateFlowEntity.setUpdateTime(new Date());
        promotOrderEvaluateFlowEntity.setState(Constants.EVALUATE_STATE_PENDING);

        promotOrderEvaluateFlowService.save(promotOrderEvaluateFlowEntity);

        return j;
    }
}
