package com.amazon.buyer.account.controller;

import com.amazon.buyer.account.entity.BuyerUserEntity;
import com.amazon.buyer.account.service.BuyerUserService;
import com.amazon.buyer.account.vo.BuyerUserVo;
import com.amazon.buyer.utils.BuyerConstants;
import com.amazon.buyer.utils.PaymentAccountConstants;
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
import org.springframework.beans.factory.annotation.Value;
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
import java.util.UUID;

/**
 * Created by User on 2017/7/8.
 */
@Scope("prototype")
@Controller
@RequestMapping("/buyerUserController")
public class BuyerUserController extends BaseController {

    private static Logger logger = LogManager.getLogger(BuyerUserController.class.getName());

    private String domainName = "http://localhost:8888/";


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
            ContextHolderUtils.getSession().setAttribute(BuyerConstants.BUYER_USER_SESSION_CONSTANTS, sessionBuyerUser);
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

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        BuyerUserEntity buyerUserEntity = globalService.getBuyerUserEntityFromSession();
        if (buyerUserEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("登录超时，请重新登录！");
            return j;
        } else {
            BuyerUserEntity buyerUserDb = buyerUserService.find(BuyerUserEntity.class,buyerUserEntity.getId());
            j.setSuccess(AjaxJson.CODE_SUCCESS);
            j.setContent(buyerUserDb);
            return j;
        }
    }

    @RequestMapping(params = "doChangePwd")
    @ResponseBody
    public AjaxJson doChangePwd(BuyerUserVo buyerUserVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        BuyerUserEntity buyerUserSession = globalService.getBuyerUserEntityFromSession();
        if (buyerUserSession == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("Log in again !");
            return j;
        }

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BuyerUserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", buyerUserSession.getAccount()));
        detachedCriteria.add(Restrictions.eq("pwd", PasswordUtil.getMD5Encryp(buyerUserVo.getOldPwd())));
        List<BuyerUserEntity> buyerUserEntityList = buyerUserService.getListByCriteriaQuery(detachedCriteria);
        if(CollectionUtils.isEmpty(buyerUserEntityList)){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg(" old password you entered is incorrect!");
            return j;
        }
        BuyerUserEntity buyerUserEntity = buyerUserEntityList.get(0);
        buyerUserEntity.setPwd(PasswordUtil.getMD5Encryp(buyerUserVo.getNewPwd()));
        buyerUserEntity.setUpdateTime(new Date());
        try {
            buyerUserService.saveOrUpdate(buyerUserEntity);
        }catch (Exception e){
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("Failed to modify password !");
            return j;
        }
        return j;
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
        buyerUserEntity.setDefaultPaymentAccount(PaymentAccountConstants.ZFB);
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

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(BuyerUserEntity buyerUserEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        BuyerUserEntity buyerUserSession = globalService.getBuyerUserEntityFromSession();
        if (buyerUserSession == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("Log in again !");
            return j;
        }

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BuyerUserEntity.class);
        detachedCriteria.add(Restrictions.eq("id", buyerUserSession.getId()));
        List<BuyerUserEntity> buyerUserEntityList = buyerUserService.getListByCriteriaQuery(detachedCriteria);
        if(CollectionUtils.isEmpty(buyerUserEntityList)){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("The user has been deleted !");
            return j;
        }
        BuyerUserEntity buyerUserDB = buyerUserEntityList.get(0);
        buyerUserDB.setDefaultPaymentAccount(buyerUserEntity.getDefaultPaymentAccount());
        buyerUserDB.setWeChatAccount(buyerUserEntity.getWeChatAccount());
        buyerUserDB.setZfbAccount(buyerUserEntity.getZfbAccount());
        buyerUserDB.setPaypalAccount(buyerUserEntity.getPaypalAccount());
        buyerUserDB.setUpdateTime(new Date());
        try {
            buyerUserService.saveOrUpdate(buyerUserDB);
        }catch (Exception e){
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("Unable to change !");
            return j;
        }
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

        BuyerUserEntity buyerUserDb = buyerUserEntityList.get(0);

        String emailCode = UUID.randomUUID().toString();
        EmailCodePojo emailCodePojo = new EmailCodePojo();
        emailCodePojo.setCode("<a href='"+domainName+
                "buyerUserController.buyer?doLookForPwd&checkCode="+emailCode+
                "&account="+buyerUserDb.getAccount()+"'>"+domainName+
                "buyerUserController.buyer?doLookForPwd&checkCode="+emailCode+
                "&account="+buyerUserDb.getAccount()+"</a>"
        );
        emailCodePojo.setEmail(email);
        buyerUserDb.setCheckCode(emailCode);
        buyerUserDb.setCheckCodeCheckTime(DateUtils.addMinute(new Date(), 10));
        try {
            buyerUserService.saveOrUpdate(buyerUserDb);
            globalService.sendEmailEmailBuyerCheckCodePojo(emailCodePojo);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("该邮箱账户不存在，请直接注册！");
            return j;
        }
        j.setMsg("Send mail successfully!");
        return j;
    }

    @RequestMapping(params = "doLookForPwd")
    public String doLookForPwd(BuyerUserEntity buyerUserEntity, HttpServletRequest request) {
        String email = buyerUserEntity.getAccount();
        if (!MailUtils.isEmail(email)) {
            return "buyer/user/failVerification";
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BuyerUserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", email));
        List<BuyerUserEntity> buyerUserEntityList = buyerUserService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(buyerUserEntityList)) {
            return "buyer/user/failVerification";
        }
        BuyerUserEntity buyerUserDb = buyerUserEntityList.get(0);
        if (buyerUserDb.getCheckCodeCheckTime().compareTo(new Date()) < 0) {
            return "buyer/user/failVerification";
        }
        if (buyerUserDb.getAccount().equals(buyerUserEntity.getAccount()) && buyerUserDb.getCheckCode().equals(buyerUserEntity.getCheckCode())) {
            ContextHolderUtils.getSession().setAttribute(BuyerConstants.CHECK_BUYER_USER_SESSION_CONSTANTS,buyerUserEntity);
            return "buyer/user/successfulVerification";
        }else{
            return "buyer/user/failVerification";
        }
    }

    @RequestMapping(params = "changePwdByCheckCode")
    @ResponseBody
    public AjaxJson changePwdByCheckCode(BuyerUserEntity buyerUserEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        BuyerUserEntity buyerUserEntitySession = (BuyerUserEntity)ContextHolderUtils.getSession().getAttribute(BuyerConstants.CHECK_BUYER_USER_SESSION_CONSTANTS);
        if(buyerUserEntitySession == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请重试！");
            return j;
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BuyerUserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", buyerUserEntitySession.getAccount()));
        List<BuyerUserEntity> buyerUserEntityList = buyerUserService.getListByCriteriaQuery(detachedCriteria);
        if(CollectionUtils.isEmpty(buyerUserEntityList)){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请重试！");
            return j;
        }
        BuyerUserEntity buyerUserEntityDb =  buyerUserEntityList.get(0);
        buyerUserEntityDb.setState(Constant.STATE_Y);
        buyerUserEntityDb.setCheckCode(null);
        buyerUserEntityDb.setCheckCodeCheckTime(null);
        buyerUserEntityDb.setPwd(PasswordUtil.getMD5Encryp(buyerUserEntity.getPwd()));
        buyerUserEntityDb.setUpdateTime(new Date());
        try {
            buyerUserService.saveOrUpdate(buyerUserEntityDb);
        } catch (Exception e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改失败！");
        }
        j.setMsg("successfully changed！");
        return j;
    }
}
