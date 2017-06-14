package com.amazon.system.filter;

import com.amazon.service.user.entity.UserEntity;
import com.amazon.system.Constant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录过滤器session判断
 * Created by User on 2017/6/6.
 */
public class LoginFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        String url = req.getServletPath();
        System.out.println("url:" + url);
        if (url.equals("/loginController.do")||url.equals("/userController.do")) {
            chain.doFilter(request, response);
        } else if(session.getAttribute(Constant.USER_SESSION_CONSTANTS) == null){
            session.invalidate();
            res.sendRedirect("/loginController.do?login");
        }else{
            chain.doFilter(request, response);
        }
    }
    public void destroy() {

    }
}
