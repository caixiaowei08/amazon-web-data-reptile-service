package com.amazon.buyer.account.controller;

import com.amazon.buyer.account.entity.BuyerUserEntity;
import com.amazon.buyer.account.service.BuyerUserService;
import com.amazon.buyer.utils.BuyerConstants;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.user.vo.UserVo;
import com.amazon.system.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.pojo.EmailCodePojo;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.core.utils.DateUtils.DateUtils;
import org.framework.core.utils.MailUtils;
import org.framework.core.utils.PasswordUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by User on 2017/7/8.
 */
@Scope("prototype")
@Controller
@RequestMapping("/buyerUserController")
public class BuyerUserController extends BaseController {

    private static Logger logger = LogManager.getLogger(BuyerUserController.class.getName());

    @Autowired
    private BuyerUserService buyerUserService;

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "doLogin")
    @ResponseBody
    public AjaxJson doLogin(BuyerUserEntity buyerUserEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BuyerUserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", buyerUserEntity.getAccount()));
        detachedCriteria.add(Restrictions.eq("pwd", PasswordUtil.getMD5Encryp(buyerUserEntity.getPwd())));
        List<BuyerUserEntity> buyerUserEntityList = buyerUserService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isNotEmpty(buyerUserEntityList)) {
            BuyerUserEntity sessionBuyerUser = buyerUserEntityList.get(0);
            sessionBuyerUser.setLoginTime(new Date());
            buyerUserService.saveOrUpdate(sessionBuyerUser);
            sessionBuyerUser.setPwd("");//session不能暴露密码
            ContextHolderUtils.getSession().setAttribute(BuyerConstants.BUYER_USER_SESSION_CONSTANTS, buyerUserService);
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

    @RequestMapping(params = "doLogOff")
    public void doLogOff(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = ContextHolderUtils.getSession();
        session.invalidate();//清空session中的所有数据
        try {
            response.sendRedirect("/userPageController.buyer?login");
        } catch (IOException e) {
            logger.info("退出登录失败！", e);
        }
    }

    @RequestMapping(params = "doCheckLogin")
    @ResponseBody
    public AjaxJson doCheckLogin(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        BuyerUserEntity buyerUserEntity = globalService.getBuyerUserEntityFromSession();
        if (buyerUserEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("登录超时，请重新登录！");
            return j;
        } else {
            return j;
        }
    }

    @RequestMapping(params = "doRegister")
    @ResponseBody
    public AjaxJson doRegister(BuyerUserEntity buyerUserEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BuyerUserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", buyerUserEntity.getAccount()));
        List<BuyerUserEntity> buyerUserEntityList = buyerUserService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isNotEmpty(buyerUserEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("邮箱号码已经被注册!");
            return j;
        }
        buyerUserEntity.setState(Constant.STATE_Y);
        buyerUserEntity.setPwd(PasswordUtil.getMD5Encryp(buyerUserEntity.getPwd()));
        buyerUserEntity.setUpdateTime(new Date());
        buyerUserEntity.setCreateTime(new Date());
        try {
            buyerUserService.save(buyerUserEntity);
        } catch (Exception e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("注册失败！");
        }
        j.setMsg("注册成功！");
        return j;
    }

    @RequestMapping(params = "sendEmailCode")
    @ResponseBody
    public AjaxJson sendEmailCode(BuyerUserEntity buyerUserEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String email = buyerUserEntity.getAccount();
        if (!MailUtils.isEmail(email)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入正确的邮箱！");
            return j;
        }

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BuyerUserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", email));
        List<BuyerUserEntity> buyerUserEntityList = buyerUserService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(buyerUserEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该邮箱账户不存在，请直接注册！");
            return j;
        }

        String emailCode = UUID.randomUUID().toString();
        EmailCodePojo emailCodePojo = new EmailCodePojo();
        emailCodePojo.setCode(emailCode);
        emailCodePojo.setCreateTime(new Date());
        emailCodePojo.setInvalidTime(DateUtils.addMinute(new Date(), 10));
        emailCodePojo.setEmail(email);
        ContextHolderUtils.getSession().setAttribute(Constant.EMAIL_CODE, emailCodePojo);
        try {
            globalService.sendEmailEmailCodePojo(emailCodePojo);
        } catch (Exception e) {

        }
        return j;
    }

    @RequestMapping(params = "doLookForPwd")
    @ResponseBody
    public AjaxJson doLookForPwd(BuyerUserEntity buyerUserEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String email = buyerUserEntity.getAccount();
        if (!MailUtils.isEmail(email)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("链接错误！");
            return j;
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BuyerUserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", email));
        List<BuyerUserEntity> buyerUserEntityList = buyerUserService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(buyerUserEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该邮箱账户不存在，请确认填写是否正确！");
            return j;
        }

        BuyerUserEntity buyerUserDb = buyerUserEntityList.get(0);

        if (buyerUserDb.getCheckCodeCheckTime().compareTo(new Date()) < 0) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("链接已经过了有效期!");
            return j;
        }

        if (buyerUserDb.getAccount().equals(buyerUserEntity.getAccount()) && buyerUserDb.getCheckCode().equals(buyerUserEntity.getCheckCode())) {
            buyerUserDb.setPwd(PasswordUtil.getMD5Encryp(buyerUserEntity.getPwd()));
            buyerUserDb.setCheckCode(null);
            buyerUserService.saveOrUpdate(buyerUserDb);
            j.setSuccess(AjaxJson.CODE_SUCCESS);
            j.setMsg("验证通过!");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_FAIL);
        j.setMsg("验证码错误！");
        return j;
    }


}
