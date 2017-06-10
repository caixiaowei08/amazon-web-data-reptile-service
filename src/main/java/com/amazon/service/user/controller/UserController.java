package com.amazon.service.user.controller;

import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.user.service.UserService;
import com.amazon.system.Constant;
import org.apache.commons.collections.CollectionUtils;
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

    @RequestMapping(params = "doLogin")
    @ResponseBody
    public AjaxJson doLogin(UserEntity userEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UserEntity.class);
        detachedCriteria.add(Restrictions.eq("account",userEntity.getAccount()));
        detachedCriteria.add(Restrictions.eq("pwd",PasswordUtil.getMD5Encryp(userEntity.getPwd())));
        List<UserEntity> userEntityList = userService.getListByCriteriaQuery(detachedCriteria);
        if(CollectionUtils.isNotEmpty(userEntityList)){
            UserEntity sessionUser = userEntityList.get(0);
            ContextHolderUtils.getSession().setAttribute(Constant.USER_SESSION_CONSTANTS,sessionUser);
            j.setSuccess(AjaxJson.CODE_SUCCESS);
            j.setMsg("登录成功！");
            return j;
        }else{
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
        detachedCriteria.add(Restrictions.eq("account",userEntity.getAccount()));
        List userEntityList = userService.getListByCriteriaQuery(detachedCriteria);
        if(CollectionUtils.isNotEmpty(userEntityList)){
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


}
