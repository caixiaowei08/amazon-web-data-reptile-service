package com.amazon.author.account.controller;

import com.amazon.author.account.entity.AuthorUserEntity;
import com.amazon.author.account.service.AuthorUserService;
import com.amazon.author.account.vo.AuthorUserVo;
import com.amazon.author.common.constant.AuthorConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.core.utils.PasswordUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "author/login/login";
    }


    @RequestMapping(params = "doLoginOff")
    public void doLoginOff(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = ContextHolderUtils.getSession();
        session.invalidate();
        ContextHolderUtils.getSession().invalidate();
        try {
            response.sendRedirect("/author/userController.author?login");
        } catch (IOException e) {
            logger.error("退出登录失败！", e);
        }
    }

    @RequestMapping(params = "doLoginCheck")
    @ResponseBody
    public AjaxJson doLoginCheck(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        if (authorUserEntity != null) {
            j.setSuccess(AjaxJson.CODE_SUCCESS);
            j.setMsg("处于登录态！");
            return j;
        } else {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
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
            if (sessionAuthorUser.getStatus().equals(AuthorConstant.AUTHOR_FREEZE_CONSTANTS)) {
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("该账号已被冻结！");
                return j;
            }
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

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        if (authorUserEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setContent(authorUserEntity);
        return j;
    }

    @RequestMapping(params = "doChangeAuthorPwdByPwd")
    @ResponseBody
    public AjaxJson doChangeAuthorPwdByPwd(AuthorUserVo authorUserVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        if (authorUserEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AuthorUserEntity.class);
        detachedCriteria.add(Restrictions.eq("id", authorUserEntity.getId()));
        detachedCriteria.add(Restrictions.eq("account", authorUserEntity.getAccount()));
        detachedCriteria.add(Restrictions.eq("pwd", PasswordUtil.getMD5Encryp(authorUserVo.getOldPwd())));
        List<AuthorUserEntity> authorUserEntityList = authorUserService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(authorUserEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("原始密码错误！");
            return j;
        }
        AuthorUserEntity authorUserDb = authorUserEntityList.get(0);
        try {
            authorUserDb.setPwd(PasswordUtil.getMD5Encryp(authorUserVo.getNewPwd()));
            authorUserDb.setUpdateTime(new Date());
            authorUserService.saveOrUpdate(authorUserDb);
        } catch (Exception e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改密码失败！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("修改成功！");
        return j;
    }


}
