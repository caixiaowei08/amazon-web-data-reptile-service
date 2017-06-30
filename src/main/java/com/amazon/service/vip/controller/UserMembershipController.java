package com.amazon.service.vip.controller;

import com.amazon.service.recharge.controller.UserRechargeFundController;
import com.amazon.service.recharge.entity.UserRechargeFundEntity;
import com.amazon.service.recharge.service.UserRechargeFundService;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.vip.entity.UserMembershipEntity;
import com.amazon.service.vip.service.UserMembershipService;
import com.amazon.service.vip.vo.UserMembershipVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by User on 2017/6/10.
 */
@Scope("prototype")
@Controller
@RequestMapping("/userMembershipController")
public class UserMembershipController extends BaseController {

    private static Logger logger = LogManager.getLogger(UserMembershipController.class.getName());

    @Autowired
    private UserMembershipService userMembershipService;

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "getUserMembershipInfo")
    @ResponseBody
    public AjaxJson getUserMembershipVo(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }
        j = userMembershipService.getUserMembershipVo();
        return j;
    }

    @RequestMapping("goToMemberShipCharge")
    public void goToMemberShipCharge(UserMembershipVo userMembershipVo, HttpServletRequest request, HttpServletResponse response) {
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            try {
                response.sendRedirect("/loginController.do?login");
            }catch (IOException e){
                logger.error(e);
            }
            return;
        }
        AjaxJson j = userMembershipService.goToChargeVipMouth(userMembershipVo, request, response);
    }

}
