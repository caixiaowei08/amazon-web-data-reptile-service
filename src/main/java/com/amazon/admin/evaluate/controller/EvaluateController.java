package com.amazon.admin.evaluate.controller;

import com.amazon.admin.account.entity.AdminSystemEntity;
import com.amazon.admin.constant.Constants;
import com.amazon.admin.evaluate.service.EvaluateService;
import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGrid;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import com.amazon.system.system.bootstrap.utils.DatagridJsonUtils;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.ContextHolderUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/6/23.
 */
@Scope("prototype")
@Controller
@RequestMapping("/evaluateController")
public class EvaluateController extends BaseController {

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private PromotOrderService promotOrderService;

    @RequestMapping(params = "goEvaluateDetail")
    public String goAdminPageIndex(HttpServletRequest request, HttpServletResponse response) {
        return "admin/evaluate/evaluateDetail";
    }

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity,HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        String amzOrderId =  promotOrderEvaluateFlowEntity.getAmzOrderId();
        String asinId =  promotOrderEvaluateFlowEntity.getAsinId();
        String reviewUrl =  promotOrderEvaluateFlowEntity.getReviewUrl();
        if((!StringUtils.hasText(amzOrderId))||(!StringUtils.hasText(asinId))){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("亚马逊订单号和ASIN编号必填！");
            return j;
        }

        if(StringUtils.hasText(reviewUrl)){
            j =  evaluateService.doAddEvaluateWithReviewUrl(promotOrderEvaluateFlowEntity);
        }else{
            j =  evaluateService.doAddEvaluateWithNoReviewUrl(promotOrderEvaluateFlowEntity);
        }
        return j;
    }



    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        AdminSystemEntity adminSystemSession = (AdminSystemEntity) ContextHolderUtils.getSession().getAttribute(Constants.ADMIN_SESSION_CONSTANTS);
        if (adminSystemSession == null) {
            return;
        }

        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEvaluateFlowEntity.class, dataGrid, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {

        }
        DataGridReturn dataGridReturn = evaluateService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(PromotOrderEntity promotOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        AdminSystemEntity adminSystemSession = (AdminSystemEntity) ContextHolderUtils.getSession().getAttribute(Constants.ADMIN_SESSION_CONSTANTS);
        if (adminSystemSession == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请先登录管理账号！");
            return j;
        }
        Integer id = promotOrderEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未输入推广订单ID！");
            return j;
        }
        PromotOrderEntity promotOrderEntityDb = promotOrderService.find(PromotOrderEntity.class, id);
        if (promotOrderEntityDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("订单不存在！");
            return j;
        }
        j.setContent(promotOrderEntityDb);
        return j;
    }




}
