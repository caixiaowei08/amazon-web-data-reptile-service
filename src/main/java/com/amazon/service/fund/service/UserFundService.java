package com.amazon.service.fund.service;

import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.common.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/6/10.
 */
public interface UserFundService extends BaseService {

    public AjaxJson getUserFundInfo();

    public void goChargeFund(HttpServletRequest request, HttpServletResponse response);


}
