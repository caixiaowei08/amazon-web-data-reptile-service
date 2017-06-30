package com.amazon.admin.skip.controller;

import org.framework.core.common.controller.BaseController;
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

    @RequestMapping(params = "goToAdminMain")
    public String goToAdminMain(HttpServletRequest request, HttpServletResponse response) {
        return "admin/main/main";
    }

    @RequestMapping(params = "goToEvaluateDetail")
    public String goToEvaluateDetail(HttpServletRequest request, HttpServletResponse response) {
        return "admin/evaluate/promotEvaluteDetail";
    }

}
