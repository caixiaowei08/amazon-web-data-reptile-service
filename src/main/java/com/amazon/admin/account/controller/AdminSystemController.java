package com.amazon.admin.account.controller;

import com.amazon.admin.account.entity.AdminSystemEntity;
import com.amazon.admin.account.service.AdminSystemService;
import com.amazon.admin.constant.Constants;
import com.amazon.service.user.controller.UserController;
import com.amazon.service.user.entity.UserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.core.utils.PasswordUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/22.
 */
@Scope("prototype")
@Controller
@RequestMapping("/adminSystemController")
public class AdminSystemController extends BaseController {

    private static Logger logger = LogManager.getLogger(AdminSystemController.class.getName());

    @Autowired
    private AdminSystemService adminSystemService;

    @RequestMapping(params = "goAdminLogin")
    public String goAdminPageIndex(HttpServletRequest request, HttpServletResponse response) {
        return "admin/login/login";
    }

    @RequestMapping(params = "doLogOff")
    public void doLogOff(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = ContextHolderUtils.getSession();
        session.invalidate();//清空session中的所有数据
        try {
            response.sendRedirect("/adminSystemController.admin?goAdminLogin");
        } catch (IOException e) {
            logger.info("退出登录失败！", e);
        }

    }


    @RequestMapping(params = "doLogin")
    @ResponseBody
    public AjaxJson doLogin(AdminSystemEntity adminSystemEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AdminSystemEntity.class);
        detachedCriteria.add(Restrictions.eq("account", adminSystemEntity.getAccount()));
        detachedCriteria.add(Restrictions.eq("pwd", PasswordUtil.getMD5Encryp(adminSystemEntity.getPwd())));
        List<AdminSystemEntity> adminSystemEntityList = adminSystemService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isNotEmpty(adminSystemEntityList)) {
            AdminSystemEntity adminSystemSession = adminSystemEntityList.get(0);
            adminSystemEntity.setLoginTime(new Date());
            adminSystemService.saveOrUpdate(adminSystemSession);
            adminSystemEntity.setPwd("");//session不能暴露密码
            ContextHolderUtils.getSession().setAttribute(Constants.ADMIN_SESSION_CONSTANTS, adminSystemSession);
            //更新登录时间
            j.setSuccess(AjaxJson.CODE_SUCCESS);
            j.setMsg("登录成功！");
            return j;
        } else {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("账号或者密码错误！");
            return j;
        }
    }




}
