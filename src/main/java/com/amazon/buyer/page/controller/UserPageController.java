package com.amazon.buyer.page.controller;

import org.framework.core.common.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/7/8.
 */
@Scope("prototype")
@Controller
@RequestMapping("/userPageController")
public class UserPageController extends BaseController{

    @RequestMapping(params = "login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "buyer/user/login";
    }

    @RequestMapping(params = "register")
    public String register(HttpServletRequest request, HttpServletResponse response) {
        return "buyer/user/register";
    }

    @RequestMapping(params = "resetPwd")
    public String sendEmailResetPwd(HttpServletRequest request, HttpServletResponse response) {
        return "buyer/user/sendEmailResetPwd";
    }

    @RequestMapping(params = "index")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "buyer/main/index";
    }


}
