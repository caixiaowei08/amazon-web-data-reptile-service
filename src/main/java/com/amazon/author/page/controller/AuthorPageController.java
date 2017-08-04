package com.amazon.author.page.controller;

import org.framework.core.common.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/7/31.
 */
@Scope("prototype")
@Controller
@RequestMapping("/author/pageController")
public class AuthorPageController extends BaseController {

    @RequestMapping(params = "main")
    public String main(HttpServletRequest request, HttpServletResponse response) {
        return "author/main/main";
    }

    @RequestMapping(params = "evaluate")
    public String evaluate(HttpServletRequest request, HttpServletResponse response) {
        return "author/evaluate/evaluate";
    }

    @RequestMapping(params = "changePwd")
    public String changePwd(HttpServletRequest request, HttpServletResponse response) {
        return "author/userInfo/changePwd";
    }

    @RequestMapping(params = "promoteEvaluateDetail")
    public String promoteEvaluateDetail(HttpServletRequest request, HttpServletResponse response) {
        return "author/evaluate/promotEvaluteList";
    }


}
