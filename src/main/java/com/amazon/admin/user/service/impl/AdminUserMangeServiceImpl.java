package com.amazon.admin.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.amazon.admin.promot.controller.AdminPromotController;
import com.amazon.admin.user.service.AdminUserMangeService;
import com.amazon.admin.user.vo.VipVo;
import com.amazon.service.fund.ConstantFund;
import com.amazon.service.vip.entity.UserMembershipEntity;
import com.amazon.service.vip.service.UserMembershipService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.DateUtils.DateUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/7/4.
 */
@Service("adminUserMangeService")
@Transactional
public class AdminUserMangeServiceImpl implements AdminUserMangeService {

    private static Logger logger = LogManager.getLogger(AdminUserMangeServiceImpl.class.getName());

    @Autowired
    private UserMembershipService userMembershipService;

    @Autowired
    private GlobalService globalService;



    public AjaxJson chargeVipMonth(VipVo vipVo) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria userMembershipDetachedCriteria = DetachedCriteria.forClass(UserMembershipEntity.class);
        userMembershipDetachedCriteria.add(Restrictions.eq("sellerId", vipVo.getSellerId()));
        List<UserMembershipEntity> userMembershipEntityList = userMembershipService.getListByCriteriaQuery(userMembershipDetachedCriteria);
        if (CollectionUtils.isEmpty(userMembershipEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("缺失会员信息，请联系管理员！");
            return j;
        }
        UserMembershipEntity userMembershipEntity = userMembershipEntityList.get(0);
        logger.info("---------------userMembershipEntity--------------"+JSON.toJSONString(userMembershipEntity));
        //处理会员逻辑
        if (userMembershipEntity.getMembershipEndTime() == null) {
            Date beginOfDate = DateUtils.getBeginOfDate();
            userMembershipEntity.setMembershipStartTime(beginOfDate);
            Date membershipEndTime = DateUtils.addMonth(beginOfDate, vipVo.getMemberShipMonth());
            membershipEndTime = DateUtils.getEndOfDate(membershipEndTime);
            userMembershipEntity.setMembershipEndTime(membershipEndTime);
        } else {
            //判断是否到期
            Date membershipEndTime = userMembershipEntity.getMembershipEndTime();
            if (DateUtils.compareTo(membershipEndTime, new Date()) > 0){//未到期处理
                userMembershipEntity.setLastMembershipEndTime(membershipEndTime);
                membershipEndTime = DateUtils.addMonth(membershipEndTime,vipVo.getMemberShipMonth());
                membershipEndTime = DateUtils.getEndOfDate(membershipEndTime);
                userMembershipEntity.setMembershipEndTime(membershipEndTime);
            }else{//到期处理
                Date beginOfDate = DateUtils.getBeginOfDate();
                userMembershipEntity.setMembershipStartTime(beginOfDate);
                Date newMembershipEndTime = DateUtils.addMonth(beginOfDate, vipVo.getMemberShipMonth());
                newMembershipEndTime = DateUtils.getEndOfDate(newMembershipEndTime);
                userMembershipEntity.setMembershipEndTime(newMembershipEndTime);
                userMembershipEntity.setLastMembershipEndTime(membershipEndTime);
            }
        }
        userMembershipEntity.setChargeTime(new Date());
        logger.info("---------------userMembershipEntity saveOrUpdate --------------"+JSON.toJSONString(userMembershipEntity));
        userMembershipService.saveOrUpdate(userMembershipEntity);
        return j;
    }
}
