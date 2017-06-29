package com.amazon.system.redirection.controller;

import org.framework.core.common.controller.BaseController;
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

    @RequestMapping(params = "goManagePromot")
    public String goManagePromot(HttpServletRequest request, HttpServletResponse response) {
        return "pages/manage/managePromot";
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

}
