package com.amazon.system.controller;

import org.framework.core.common.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/6/6.
 */
@Scope("prototype")
@Controller
@RequestMapping("/mainController")
public class MainController extends BaseController{

    @RequestMapping(params = "index")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "pages/main/index";
    }




}
