package com.amazon.system.filter;

import com.alibaba.fastjson.JSON;
import com.amazon.admin.constant.Constants;
import com.amazon.system.Constant;
import org.framework.core.common.model.json.AjaxJson;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by User on 2017/6/29.
 */
public class AdminLoginFilter  implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        String url = req.getServletPath();
        if (url.equals("/adminSystemController.admin")) {
            chain.doFilter(request, response);
        }else if(url.equals("/admin.admin")){
            session.invalidate();
            res.sendRedirect("/adminSystemController.admin?goAdminLogin");
        }else if(session.getAttribute(Constants.ADMIN_SESSION_CONSTANTS) == null){
            session.invalidate();
            res.sendRedirect("/adminSystemController.admin?goAdminLogin");
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
        }else{
            chain.doFilter(request, response);
        }
    }
    public void destroy() {

    }
}
