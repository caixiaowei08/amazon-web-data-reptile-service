package com.amazon.admin.evaluate.controller;

import org.framework.core.common.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/6/23.
 */
@Scope("prototype")
@Controller
@RequestMapping("/evaluateController")
public class EvaluateController extends BaseController{

    @RequestMapping(params = "goEvaluateDetail")
    public String goAdminPageIndex(HttpServletRequest request, HttpServletResponse response) {
        return "admin/evaluate/evaluateDetail";
    }

}
