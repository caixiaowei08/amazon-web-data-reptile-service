package com.amazon.system.controller;

import com.amazon.service.user.entity.UserEntity;
import org.framework.core.common.controller.BaseController;
import org.framework.core.global.service.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 2017/6/6.
 */
@Scope("prototype")
@Controller
@RequestMapping("/mainController")
public class MainController extends BaseController{

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "index")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            return "pages/login/login";
        }
        return "pages/main/index";
    }

}
