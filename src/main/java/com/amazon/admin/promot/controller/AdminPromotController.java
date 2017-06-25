package com.amazon.admin.promot.controller;

import com.amazon.admin.account.entity.AdminSystemEntity;
import com.amazon.admin.constant.Constants;
import com.amazon.admin.poi.service.PoiPromotService;
import com.amazon.admin.promot.service.AdminPromotService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGrid;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import com.amazon.system.system.bootstrap.utils.DatagridJsonUtils;
import org.framework.core.common.controller.BaseController;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.core.utils.DateUtils.DateUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/23.
 */
@Scope("prototype")
@Controller
@RequestMapping("/adminPromotController")
public class AdminPromotController extends BaseController {

    @Autowired
    private AdminPromotService adminPromotService;

    @Autowired
    private PoiPromotService poiPromotService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, dataGrid, request.getParameterMap());
        AdminSystemEntity adminSystemEntity = (AdminSystemEntity) ContextHolderUtils.getSession().getAttribute(Constants.ADMIN_SESSION_CONSTANTS);
        if(adminSystemEntity == null){
           return;
        }
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            //打印日志信息
        }
        DataGridReturn dataGridReturn = adminPromotService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "downPromotOrderExcel")
    public void downPromotOrderExcel(HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, null, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            //打印日志信息
        }
        criteriaQuery.getDetachedCriteria().addOrder(Order.desc("id"));
        List<PromotOrderEntity> promotOrderEntityList = adminPromotService.getListByCriteriaQuery(criteriaQuery.getDetachedCriteria());
        String excelFileNameHeader = "平台订单表" + DateUtils.getDate(new Date()) ;
        try{
            poiPromotService.downPromotOrderExcel(promotOrderEntityList,response,excelFileNameHeader);
        }catch (Exception e){

        }

    }




}
