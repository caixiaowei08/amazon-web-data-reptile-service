package com.amazon.service.recharge.controller;

import com.amazon.admin.poi.service.PoiPromotService;
import com.amazon.service.fund.ConstantFund;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.recharge.entity.UserRechargeFundEntity;
import com.amazon.service.recharge.service.UserRechargeFundService;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.system.Constant;
import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGrid;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import com.amazon.system.system.bootstrap.utils.DatagridJsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.core.utils.DateUtils.DateUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
import java.util.List;

/**
 * Created by User on 2017/6/10.
 */
@Scope("prototype")
@Controller
@RequestMapping("/userRechargeFundController")
public class UserRechargeFundController extends BaseController {

    private static Logger logger = LogManager.getLogger(UserRechargeFundController.class.getName());

    @Autowired
    private UserRechargeFundService userRechargeFundService;

    @Autowired
    private PoiPromotService poiPromotService;

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(UserRechargeFundEntity.class, dataGrid, request.getParameterMap());
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            return;
        }
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.info("组装查询出错！", e);
        }
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("sellerId", userEntity.getId()));//加上用户
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("state", ConstantFund.SUCCESS));
        DataGridReturn dataGridReturn = userRechargeFundService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "downChargeFundFlowExcel")
    public void downChargeFundFlowExcel(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            return;
        }
        CriteriaQuery criteriaQuery = new CriteriaQuery(UserRechargeFundEntity.class, null, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
        }
        criteriaQuery.getDetachedCriteria().addOrder(Order.desc("createTime"));
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("sellerId", userEntity.getId()));
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("state", ConstantFund.SUCCESS));
        List<UserRechargeFundEntity> userRechargeFundEntityList = userRechargeFundService.getListByCriteriaQuery(criteriaQuery.getDetachedCriteria());
        String excelFileNameHeader = "充值记录报表" + DateUtils.getDate(new Date());
        try {
            poiPromotService.downChargeFundFlowExcel(userRechargeFundEntityList, response, excelFileNameHeader);
        } catch (Exception e) {
            logger.error("downChargeFundFlowExcel 错误！",e);
        }
    }
}
