package com.amazon.system.redirection.controller;

import com.amazon.service.fund.constant.ConstantChargeAndOrderFlag;
import com.amazon.service.fund.controller.UserFundController;
import com.amazon.service.spider.SpiderConstant;
import com.amazon.service.spider.pojo.AmazonPageInfoPojo;
import com.amazon.service.vip.service.UserMembershipService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.ContextHolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/6/18.
 */
@Scope("prototype")
@Controller
@RequestMapping("/redirectionController")
public class RedirectionController extends BaseController {

    private static Logger logger = LogManager.getLogger(RedirectionController.class.getName());

    @Autowired
    private UserMembershipService userMembershipService;

    @RequestMapping(params = "goManagePromot")
    public String goManagePromot(HttpServletRequest request, HttpServletResponse response) {
        if (userMembershipService.isMemberShipVip()) {
            return "pages/manage/managePromot";
        } else {
            return "pages/fund/memberShip";
        }
    }

    @RequestMapping(params = "goToChargeFund")
    public String goToChangeFund(HttpServletRequest request, HttpServletResponse response) {
        String chargeFund = request.getParameter("chargeFund");
        ContextHolderUtils.getSession().removeAttribute(ConstantChargeAndOrderFlag.CHARGE_FOR_ORDER_FLAG);
        if (StringUtils.hasText(chargeFund)) {//余额不足跳转充值
            AmazonPageInfoPojo amazonPageInfoPojo = (AmazonPageInfoPojo) ContextHolderUtils.getSession().getAttribute(SpiderConstant.AMAZON_PAGE_INFO_POJO);
            if (amazonPageInfoPojo != null) {//session充值标识
                ContextHolderUtils.getSession().setAttribute(ConstantChargeAndOrderFlag.CHARGE_FOR_ORDER_FLAG, amazonPageInfoPojo);
            }
        }
        return "pages/fund/chargeFund";
    }

    @RequestMapping(params = "goToChargeMemberShip")
    public String goToChargeMemberShip(HttpServletRequest request, HttpServletResponse response) {
        return "pages/fund/memberShip";
    }

    @RequestMapping(params = "goUserDetailInfo")
    public String goUserDetailInfo(HttpServletRequest request, HttpServletResponse response) {
        return "pages/user/userDetail";
    }

    @RequestMapping(params = "goUserChangePwd")
    public String goUserChangePwd(HttpServletRequest request, HttpServletResponse response) {
        return "pages/user/changePwd";
    }

    @RequestMapping(params = "chargeFundFlow")
    public String goChargeFundFlow(HttpServletRequest request, HttpServletResponse response) {
        return "pages/fund/chargeFundFlow";
    }

    @RequestMapping(params = "goToEvaluateDetail")
    public String goToEvaluateDetail(HttpServletRequest request, HttpServletResponse response) {
        return "pages/evaluate/evaluateDetail";
    }

}
