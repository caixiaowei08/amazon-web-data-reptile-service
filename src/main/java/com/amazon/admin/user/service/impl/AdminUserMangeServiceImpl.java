package com.amazon.admin.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.amazon.admin.promot.controller.AdminPromotController;
import com.amazon.admin.user.service.AdminUserMangeService;
import com.amazon.admin.user.vo.FundVo;
import com.amazon.admin.user.vo.VipVo;
import com.amazon.service.fund.ConstantFund;
import com.amazon.service.fund.entity.UserFundEntity;
import com.amazon.service.fund.service.UserFundService;
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

import java.math.BigDecimal;
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


    @Autowired
    private UserFundService userFundService;


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
        logger.info("---------------userMembershipEntity---充值之前-----------" + JSON.toJSONString(userMembershipEntity));
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
            if (DateUtils.compareTo(membershipEndTime, new Date()) > 0) {//未到期处理
                userMembershipEntity.setLastMembershipEndTime(membershipEndTime);
                membershipEndTime = DateUtils.addMonth(membershipEndTime, vipVo.getMemberShipMonth());
                membershipEndTime = DateUtils.getEndOfDate(membershipEndTime);
                userMembershipEntity.setMembershipEndTime(membershipEndTime);
            } else {//到期处理
                Date beginOfDate = DateUtils.getBeginOfDate();
                userMembershipEntity.setMembershipStartTime(beginOfDate);
                Date newMembershipEndTime = DateUtils.addMonth(beginOfDate, vipVo.getMemberShipMonth());
                newMembershipEndTime = DateUtils.getEndOfDate(newMembershipEndTime);
                userMembershipEntity.setMembershipEndTime(newMembershipEndTime);
                userMembershipEntity.setLastMembershipEndTime(membershipEndTime);
            }
        }
        userMembershipEntity.setChargeTime(new Date());
        logger.info("---------------userMembershipEntity saveOrUpdate 充值完成 --------------" + JSON.toJSONString(userMembershipEntity));
        userMembershipService.saveOrUpdate(userMembershipEntity);
        return j;
    }

    public AjaxJson chargeFund(FundVo fundVo) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
        userFundDetachedCriteria.add(Restrictions.eq("sellerId", fundVo.getSellerId()));
        List<UserFundEntity> userFundEntityList = userFundService.getListByCriteriaQuery(userFundDetachedCriteria);
        if (CollectionUtils.isEmpty(userFundEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("无法找到资金账户，请联系管理员！");
            return j;
        }

        BigDecimal chargeFund = new BigDecimal(0);
        try {
            chargeFund = new BigDecimal(fundVo.getChargeFund());
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("充值金额输入有误！");
            return j;
        }
        //资金变动
        UserFundEntity userFundEntity = userFundEntityList.get(0);
        logger.info("---------------userFundEntity----- 开始---------" + JSON.toJSONString(userFundEntity));
        userFundEntity.setTotalFund(userFundEntity.getTotalFund().add(chargeFund));
        userFundEntity.setUsableFund(userFundEntity.getUsableFund().add(chargeFund));
        userFundEntity.setUpdateTime(new Date());
        //充值流水变动
        userFundService.saveOrUpdate(userFundEntity);
        logger.info("---------------userFundEntity-----结束---------" + JSON.toJSONString(userFundEntity));
        return j;
    }
}
