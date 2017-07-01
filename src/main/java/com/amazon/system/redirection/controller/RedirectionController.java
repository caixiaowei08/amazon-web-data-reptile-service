package com.amazon.system.redirection.controller;

import com.amazon.service.fund.controller.UserFundController;
import com.amazon.service.vip.service.UserMembershipService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/6/18.
 */
@Scope("prototype")
@Controller
@RequestMapping("/redirectionController")
public class RedirectionController extends BaseController{

    private static Logger logger = LogManager.getLogger(RedirectionController.class.getName());

    @Autowired
    private UserMembershipService userMembershipService;

    @RequestMapping(params = "goManagePromot")
    public String goManagePromot(HttpServletRequest request, HttpServletResponse response) {
        if(userMembershipService.isMemberShipVip()){
            return "pages/manage/managePromot";
        }else{
            return "pages/fund/memberShip";
        }
    }

    @RequestMapping(params = "goToChargeFund")
    public String goToChangeFund(HttpServletRequest request, HttpServletResponse response) {
        return "pages/fund/chargeFund";
    }

    @RequestMapping(params = "goToChargeMemberShip")
    public String goToChargeMemberShip(HttpServletRequest request, HttpServletResponse response) {
        return "pages/fund/memberShip";
    }

    @RequestMapping(params = "goUserDetailInfo")
    public String goUserDetailInfo(HttpServletRequest request, HttpServletResponse response) {
        return "pages/user/userDetail";
    }

    @RequestMapping(params = "goUserChangePwd")
    public String goUserChangePwd(HttpServletRequest request, HttpServletResponse response) {
        return "pages/user/changePwd";
    }

    @RequestMapping(params = "chargeFundFlow")
    public String goChargeFundFlow(HttpServletRequest request, HttpServletResponse response) {
        return "pages/fund/chargeFundFlow";
    }

    @RequestMapping(params = "goToEvaluateDetail")
    public String goToEvaluateDetail(HttpServletRequest request, HttpServletResponse response) {
        return "pages/evaluate/evaluateDetail";
    }

}
