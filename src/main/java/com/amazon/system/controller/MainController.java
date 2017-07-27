package com.amazon.system.controller;

import com.amazon.service.user.entity.UserEntity;
import org.framework.core.common.controller.BaseController;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.ContextHolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by User on 2017/6/6.
 */
@Scope("prototype")
@Controller
@RequestMapping("/mainController")
public class MainController extends BaseController {

    @RequestMapping(params = "index")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String JSESSIONID = request.getParameter("jsessionid");
        if (StringUtils.hasText(JSESSIONID)) {
            HttpSession session = ContextHolderUtils.getSession();
            Cookie cookie = new Cookie("JSESSIONID", session.getId());
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return "pages/main/index";
    }

}
