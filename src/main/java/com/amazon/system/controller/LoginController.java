package com.amazon.system.controller;

import org.framework.core.common.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/5/30.
 */
@Scope("prototype")
@Controller
@RequestMapping("/loginController")
public class LoginController extends BaseController{

    @RequestMapping(params = "login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "pages/login/login";
    }

   /* @RequestMapping(params = "register")
    public String register(HttpServletRequest request, HttpServletResponse response) {
        return "pages/login/register";
    }
*/
    @RequestMapping(params = "lookForPwd")
    public String lookForPwd(HttpServletRequest request, HttpServletResponse response) {
        return "pages/login/lookForPwd";
    }

}
