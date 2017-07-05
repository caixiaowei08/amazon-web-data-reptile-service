package com.amazon.admin.user.controller;

import com.alibaba.fastjson.JSON;
import com.amazon.admin.user.service.AdminUserMangeService;
import com.amazon.admin.user.vo.FundVo;
import com.amazon.admin.user.vo.VipVo;
import com.amazon.service.fund.entity.UserFundEntity;
import com.amazon.service.fund.service.UserFundService;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.user.service.UserService;
import com.amazon.service.vip.entity.UserMembershipEntity;
import com.amazon.service.vip.service.UserMembershipService;
import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGrid;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import com.amazon.system.system.bootstrap.utils.DatagridJsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.RegularExpressionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 2017/7/4.
 */
@Scope("prototype")
@Controller
@RequestMapping("/adminUserMangeController")
public class AdminUserMangeController extends BaseController {

    private static Logger logger = LogManager.getLogger(AdminUserMangeController.class.getName());

    @Autowired
    private GlobalService globalService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMembershipService userMembershipService;

    @Autowired
    private AdminUserMangeService adminUserMangeService;

    @Autowired
    private UserFundService userFundService;

    @RequestMapping(params = "doGetVipInfo")
    @ResponseBody
    public AjaxJson doGetVipInfo(VipVo vipVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        if (globalService.isNotAdminLogin()) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录!");
            return j;
        }

        if (vipVo.getSellerId() == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请求参数有误！");
            return j;
        }

        UserEntity userEntity = userService.find(UserEntity.class, vipVo.getSellerId());
        DetachedCriteria userMembershipDetachedCriteria = DetachedCriteria.forClass(UserMembershipEntity.class);
        userMembershipDetachedCriteria.add(Restrictions.eq("sellerId", userEntity.getId()));
        List<UserMembershipEntity> userMembershipEntityList = userMembershipService.getListByCriteriaQuery(userMembershipDetachedCriteria);
        if (CollectionUtils.isEmpty(userMembershipEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("缺失会员信息，请联系管理员！");
            return j;
        }
        UserMembershipEntity userMembershipEntity = userMembershipEntityList.get(0);
        vipVo.setId(userMembershipEntity.getId());
        vipVo.setSellerId(userMembershipEntity.getSellerId());
        vipVo.setAccount(userEntity.getAccount());
        vipVo.setMembershipEndTime(userMembershipEntity.getMembershipEndTime());
        j.setContent(vipVo);
        return j;
    }

    @RequestMapping(params = "doGetFundInfo")
    @ResponseBody
    public AjaxJson doGetFundInfo(FundVo fundVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        if (globalService.isNotAdminLogin()) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录!");
            return j;
        }

        if (fundVo.getSellerId() == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请求参数有误！");
            return j;
        }

        UserEntity userEntity = userService.find(UserEntity.class, fundVo.getSellerId());
        DetachedCriteria userFundDetachedCriteria = DetachedCriteria.forClass(UserFundEntity.class);
        userFundDetachedCriteria.add(Restrictions.eq("sellerId", userEntity.getId()));
        List<UserFundEntity> userFundEntityList = userFundService.getListByCriteriaQuery(userFundDetachedCriteria);
        if (CollectionUtils.isEmpty(userFundEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("缺失买家资金信息，请联系管理员！");
            return j;
        }
        UserFundEntity userFundEntity = userFundEntityList.get(0);
        fundVo.setId(userFundEntity.getId());
        fundVo.setSellerId(userFundEntity.getSellerId());
        fundVo.setAccount(userEntity.getAccount());
        fundVo.setTotalFund(userFundEntity.getTotalFund());
        fundVo.setFreezeFund(userFundEntity.getFreezeFund());
        fundVo.setUsableFund(userFundEntity.getUsableFund());
        j.setContent(fundVo);
        return j;
    }

    @RequestMapping(params = "doChargeVip")
    @ResponseBody
    public AjaxJson doChargeVip(VipVo vipVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        if (globalService.isNotAdminLogin()) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录!");
            return j;
        }
        if (vipVo.getSellerId() == null
                || vipVo.getMemberShipMonth() == null
                || vipVo.getMemberShipMonth() < 1) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请求参数有误！");
            return j;
        }
        logger.info("会员充值---开始-----"+ JSON.toJSONString(vipVo));
        UserEntity userEntity = userService.find(UserEntity.class, vipVo.getSellerId());
        if(userEntity == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该卖家不存在！");
            return j;
        }
        try {
            j = adminUserMangeService.chargeVipMonth(vipVo);
        }catch (Exception e){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setContent("充值会员失败！");
            logger.error(e.fillInStackTrace());
            return j;
        }
        logger.info("会员充值---结束-----"+ JSON.toJSONString(j));
        return j;
    }

    @RequestMapping(params = "doFundCharge")
    @ResponseBody
    public AjaxJson doFundCharge(FundVo fundVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        if (globalService.isNotAdminLogin()) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录!");
            return j;
        }
        if (fundVo.getSellerId() == null||StringUtils.isEmpty(fundVo.getChargeFund())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请求参数有误！");
            return j;
        }

        if(!RegularExpressionUtils.isMoney(fundVo.getChargeFund())){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("金额输入错误,请检查！");
            return j;
        }


        UserEntity userEntity = userService.find(UserEntity.class, fundVo.getSellerId());
        if(userEntity == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该卖家不存在！");
            return j;
        }
        logger.info("资金充值---开始-----"+ JSON.toJSONString(fundVo));
        try {
            j = adminUserMangeService.chargeFund(fundVo);
        }catch (Exception e){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setContent("充值金额失败！");
            logger.error(e.fillInStackTrace());
            return j;
        }
        logger.info("资金充值---结束-----"+ JSON.toJSONString(j));
        return j;
    }


    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(UserEntity.class, dataGrid, request.getParameterMap());
        if (globalService.isNotAdminLogin()) {
            return;
        }
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.error(e);
        }
        String account = request.getParameter("account").trim();
        if (StringUtils.hasText(account)) {
            criteriaQuery.getDetachedCriteria().add(Restrictions.eq("account", account));
        }
        DataGridReturn dataGridReturn = userService.getDataGridReturn(criteriaQuery);
        dataGrid.setField("id,account,state,loginTime,createTime,");
        DatagridJsonUtils.listToObj(dataGridReturn, UserEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }


}
