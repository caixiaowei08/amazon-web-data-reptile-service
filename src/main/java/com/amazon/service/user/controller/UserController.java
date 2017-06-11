package com.amazon.service.user.controller;

import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.user.service.UserService;
import com.amazon.service.user.vo.UserVo;
import com.amazon.system.Constant;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/6.
 */
@Scope("prototype")
@Controller
@RequestMapping("/userController")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "doLogin")
    @ResponseBody
    public AjaxJson doLogin(UserEntity userEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", userEntity.getAccount()));
        detachedCriteria.add(Restrictions.eq("pwd", PasswordUtil.getMD5Encryp(userEntity.getPwd())));
        List<UserEntity> userEntityList = userService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isNotEmpty(userEntityList)) {
            UserEntity sessionUser = userEntityList.get(0);
            ContextHolderUtils.getSession().setAttribute(Constant.USER_SESSION_CONSTANTS, sessionUser);
            j.setSuccess(AjaxJson.CODE_SUCCESS);
            j.setMsg("登录成功！");
            return j;
        } else {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("账号或者密码错误！");
            return j;
        }
    }

    @RequestMapping(params = "doRegister")
    @ResponseBody
    public AjaxJson doRegister(UserEntity userEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", userEntity.getAccount()));
        List userEntityList = userService.getListByCriteriaQuery(detachedCriteria);

        if (CollectionUtils.isNotEmpty(userEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("邮箱号码已经被注册!");
            return j;
        }
        userEntity.setState(Constant.STATE_Y);
        userEntity.setPwd(PasswordUtil.getMD5Encryp(userEntity.getPwd()));
        userEntity.setUpdateTime(new Date());
        userEntity.setCreateTime(new Date());
        try {
            userService.save(userEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("创建账户失败！");
        }
        j.setMsg("注册成功！");
        return j;
    }

    @RequestMapping(params = "sendEmailCode")
    @ResponseBody
    public AjaxJson sendEmailCode(UserEntity userEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();

        String email = userEntity.getAccount();
        if (!MailUtils.isEmail(email)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入正确的邮箱！");
            return j;
        }

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", email));
        List<UserEntity> userEntityList = userService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(userEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该邮箱账户不存在，请直接注册！");
            return j;
        }

        String emailCode = MailUtils.getRandomNum();
        EmailCodePojo emailCodePojo = new EmailCodePojo();
        emailCodePojo.setCode(emailCode);
        emailCodePojo.setCreateTime(new Date());
        emailCodePojo.setInvalidTime(DateUtils.addMinute(new Date(), 10));
        emailCodePojo.setEmail(email);
        ContextHolderUtils.getSession().setAttribute(Constant.EMAIL_CODE, emailCodePojo);
        try {
            globalService.sendEmailEmailCodePojo(emailCodePojo);
        } catch (Exception e) {
            //@TODO 邮件发送失败处理
        }
        return j;
    }

    @RequestMapping(params = "doLookForPwd")
    @ResponseBody
    public AjaxJson doLookForPwd(UserVo userVo, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();

        String email = userVo.getAccount();
        if (!MailUtils.isEmail(email)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入正确的邮箱！");
            return j;
        }

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", email));
        List<UserEntity> userEntityList = userService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(userEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该邮箱账户不存在，请确认填写是否正确！");
            return j;
        }
        EmailCodePojo emailCodePojo = (EmailCodePojo) ContextHolderUtils.getSession().getAttribute(Constant.EMAIL_CODE);

        if ((emailCodePojo != null) && (userVo.getAccount().equals(emailCodePojo.getEmail())) &&
                userVo.getEmailCode().equals(emailCodePojo.getCode())) {
            if(StringUtils.hasText(userVo.getPwd())){
                UserEntity userEntity = userEntityList.get(0);
                userEntity.setPwd(PasswordUtil.getMD5Encryp(userVo.getPwd()));
                userService.saveOrUpdate(userEntity);
                return j;
            }
        }
        j.setSuccess(AjaxJson.CODE_FAIL);
        j.setMsg("验证码错误！");
        return j;
    }


}
