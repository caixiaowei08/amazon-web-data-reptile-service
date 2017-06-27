package com.amazon.service.vip.service.impl;

import com.amazon.service.fund.entity.UserFundEntity;
import com.amazon.service.fund.vo.UserFundVo;
import com.amazon.service.promot.price.entity.PromotPriceEntity;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.vip.entity.UserMembershipEntity;
import com.amazon.service.vip.service.UserMembershipService;
import com.amazon.service.vip.vo.UserMembershipVo;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.core.utils.DateUtils.DateUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/10.
 */
@Service("userMembershipService")
@Transactional
public class UserMembershipServiceImpl extends BaseServiceImpl implements UserMembershipService {

    @Autowired
    private GlobalService globalService;

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

        if(CollectionUtils.isEmpty(userMembershipEntityList)){
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
}
