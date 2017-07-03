package com.amazon.system.filter;

import com.alibaba.fastjson.JSON;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.system.Constant;
import org.framework.core.common.model.json.AjaxJson;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

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
        if (url.equals("/loginController.do")
                || url.equals("/userController.do")
                || url.equals("/userFundController/doAlipayTradePageNotify.do")) {
            chain.doFilter(request, response);
        } else if (session.getAttribute(Constant.USER_SESSION_CONSTANTS) == null) {
            session.invalidate();
            res.sendRedirect("/loginController.do?login");
            AjaxJson j = new AjaxJson();
            j.setSuccess(AjaxJson.RELOGIN);
            res.setContentType("application/json");
            res.setHeader("Cache-Control", "no-store");
            res.setHeader("Content-type", "text/html;charset=UTF-8");
            try {
                PrintWriter pw = res.getWriter();
                pw.write(JSON.toJSONString(j));
                pw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        } else {//有session user 通过
            chain.doFilter(request, response);
        }
    }

    public void destroy() {

    }
}
