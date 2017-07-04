package com.amazon.admin.skip.controller;

import org.framework.core.common.controller.BaseController;
import org.framework.core.global.service.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/6/22.
 */
@Scope("prototype")
@Controller
@RequestMapping("/skipController")
public class SkipController extends BaseController{

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "goToAdminMain")
    public String goToAdminMain(HttpServletRequest request, HttpServletResponse response) {
        return "admin/main/main";
    }

    @RequestMapping(params = "goToEvaluateDetail")
    public String goToEvaluateDetail(HttpServletRequest request, HttpServletResponse response) {
        if (globalService.isNotAdminLogin()) {
            return "/adminSystemController.admin?goAdminLogin";
        }
        return "admin/evaluate/promotEvaluteDetail";
    }

    @RequestMapping(params = "goAdminChangePwd")
    public String goAdminChangePwd(HttpServletRequest request, HttpServletResponse response) {
        if (globalService.isNotAdminLogin()) {
            return "/adminSystemController.admin?goAdminLogin";
        }
        return "admin/admin/changePwd";
    }

    @RequestMapping(params = "goPriceExchange")
    public String goPriceExchange(HttpServletRequest request, HttpServletResponse response) {
        if (globalService.isNotAdminLogin()) {
            return "/adminSystemController.admin?goAdminLogin";
        }
        return "admin/systemConfig/priceExchange";
    }

    @RequestMapping(params = "goQQContacts")
    public String goQQContacts(HttpServletRequest request, HttpServletResponse response) {
        if (globalService.isNotAdminLogin()) {
            return "/adminSystemController.admin?goAdminLogin";
        }
        return "admin/systemConfig/QQContacts";
    }

    @RequestMapping(params = "goUserManage")
    public String goUserManage(HttpServletRequest request, HttpServletResponse response) {
        if (globalService.isNotAdminLogin()) {
            return "/adminSystemController.admin?goAdminLogin";
        }
        return "admin/systemConfig/userManage";
    }

}
