package com.amazon.admin.promot.controller;

import com.amazon.admin.account.controller.AdminSystemController;
import com.amazon.admin.account.entity.AdminSystemEntity;
import com.amazon.admin.constant.Constants;
import com.amazon.admin.poi.service.PoiPromotService;
import com.amazon.admin.promot.service.AdminPromotService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGrid;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import com.amazon.system.system.bootstrap.utils.DatagridJsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.core.utils.DateUtils.DateUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    private static Logger logger = LogManager.getLogger(AdminPromotController.class.getName());

    @Autowired
    private AdminPromotService adminPromotService;

    @Autowired
    private PoiPromotService poiPromotService;

    @Autowired
    private GlobalService globalService;

    @Autowired
    private PromotOrderService promotOrderService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, dataGrid, request.getParameterMap());
        if(globalService.isNotAdminLogin()){
            return;
        }

        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            //打印日志信息
            logger.error(e);
        }
        DataGridReturn dataGridReturn = adminPromotService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "downPromotOrderExcel")
    public void downPromotOrderExcel(HttpServletRequest request, HttpServletResponse response) {
        if(globalService.isNotAdminLogin()){
            return;
        }
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, null, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            //打印日志信息
            logger.error(e);
        }
        criteriaQuery.getDetachedCriteria().addOrder(Order.desc("id"));
        List<PromotOrderEntity> promotOrderEntityList = adminPromotService.getListByCriteriaQuery(criteriaQuery.getDetachedCriteria());
        String excelFileNameHeader = "平台订单表" + DateUtils.getDate(new Date()) ;
        try{
            poiPromotService.downPromotOrderExcel(promotOrderEntityList,response,excelFileNameHeader);
        }catch (Exception e){
            logger.error(e);
        }
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(PromotOrderEntity promotOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        if(globalService.isNotAdminLogin()){
            j.setContent(AjaxJson.RELOGIN);
            j.setMsg("请登录管理员账号！");
            return j;
        }
        Integer id = promotOrderEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择您需要查看信息详情！");
            return j;
        }
        PromotOrderEntity promotOrderDb = promotOrderService.find(PromotOrderEntity.class,promotOrderEntity.getId());
        j.setContent(promotOrderDb);
        return j;
    }




}
