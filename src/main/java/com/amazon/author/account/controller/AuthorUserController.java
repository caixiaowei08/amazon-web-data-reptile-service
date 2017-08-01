package com.amazon.author.account.controller;

import com.amazon.author.account.entity.AuthorUserEntity;
import com.amazon.author.account.service.AuthorUserService;
import com.amazon.author.common.constant.AuthorConstant;
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
 * Created by User on 2017/7/30.
 */
@Scope("prototype")
@Controller
@RequestMapping("/author/userController")
public class AuthorUserController extends BaseController {

    private static Logger logger = LogManager.getLogger(AuthorUserController.class.getName());

    @Autowired
    private AuthorUserService authorUserService;

    @RequestMapping(params = "login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "author/login/login";
    }


    @RequestMapping(params = "doLoginOff")
    public void doLoginOff(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = ContextHolderUtils.getSession();
        session.invalidate();//清空session中的所有数据
        ContextHolderUtils.getSession().invalidate();
        try {
            response.sendRedirect("/author/userController.author?login");
        } catch (IOException e) {
            logger.error("退出登录失败！", e);
        }
    }


    @RequestMapping(params = "doLogin")
    @ResponseBody
    public AjaxJson doLogin(AuthorUserEntity authorUserEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AuthorUserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", authorUserEntity.getAccount()));
        detachedCriteria.add(Restrictions.eq("pwd", PasswordUtil.getMD5Encryp(authorUserEntity.getPwd())));
        List<AuthorUserEntity> authorUserEntityList = authorUserService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isNotEmpty(authorUserEntityList)) {
            AuthorUserEntity sessionAuthorUser = authorUserEntityList.get(0);
            sessionAuthorUser.setLoginTime(new Date());
            authorUserService.saveOrUpdate(sessionAuthorUser);
            sessionAuthorUser.setPwd("");
            ContextHolderUtils.getSession().setAttribute(AuthorConstant.AUTHOR_SESSION_CONSTANTS, sessionAuthorUser);
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
