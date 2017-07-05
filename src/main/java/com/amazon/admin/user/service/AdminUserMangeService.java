package com.amazon.admin.user.service;

import com.amazon.admin.user.vo.FundVo;
import com.amazon.admin.user.vo.VipVo;
import org.framework.core.common.model.json.AjaxJson;

/**
 * Created by User on 2017/7/4.
 */
public interface AdminUserMangeService {

    public AjaxJson chargeVipMonth(VipVo vipVo);

    public AjaxJson chargeFund(FundVo fundVo);


}
