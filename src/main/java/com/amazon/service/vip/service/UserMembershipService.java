package com.amazon.service.vip.service;

import com.amazon.service.vip.vo.UserMembershipVo;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/6/10.
 */
public interface UserMembershipService extends BaseService {

    public AjaxJson getUserMembershipVo();

    public AjaxJson goToChargeVipMouth(UserMembershipVo userMembershipVo, HttpServletRequest request, HttpServletResponse response);


}
