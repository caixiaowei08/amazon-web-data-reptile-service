package com.amazon.author.common.filter;

import com.alibaba.fastjson.JSON;
import com.amazon.author.common.constant.AuthorConstant;
import org.framework.core.common.model.json.AjaxJson;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by User on 2017/7/31.
 */
public class AuthorFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        String url = req.getServletPath();
        if (url.equals("/author")) {
            session.invalidate();
            res.sendRedirect("/author/userController.author?login");
        } else if (url.equals("/author/userController.author")) {
            chain.doFilter(request, response);
        } else if (session.getAttribute(AuthorConstant.AUTHOR_SESSION_CONSTANTS) != null) {
            chain.doFilter(request, response);
        } else if (url.equals("/author/pageController.author")) {
            res.sendRedirect("/author/userController.author?login");
        } else {
            session.invalidate();
            AjaxJson j = new AjaxJson();
            res.setContentType("application/json");
            res.setHeader("Cache-Control", "no-store");
            res.setHeader("Content-type", "application/json;charset=UTF-8");
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            res.getWriter().write(JSON.toJSONString(j));
        }
    }

    public void destroy() {

    }
}
