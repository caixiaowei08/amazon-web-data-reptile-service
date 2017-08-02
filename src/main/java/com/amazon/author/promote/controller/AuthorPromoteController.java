package com.amazon.author.promote.controller;

import com.amazon.author.account.controller.AuthorUserController;
import com.amazon.author.account.entity.AuthorUserEntity;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGrid;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import com.amazon.system.system.bootstrap.utils.DatagridJsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.global.service.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/7/31.
 */
@Scope("prototype")
@Controller
@RequestMapping("/author/promoteController")
public class AuthorPromoteController extends BaseController {

    private static Logger logger = LogManager.getLogger(AuthorUserController.class.getName());

    @Autowired
    private GlobalService globalService;

    @Autowired
    private PromotOrderService promotOrderService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, dataGrid, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.error("组装查询出错！", e);
        }
        DataGridReturn dataGridReturn = promotOrderService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(PromotOrderEntity promotOrderEntity,HttpServletRequest request, HttpServletResponse response){
        AjaxJson j = new AjaxJson();
        PromotOrderEntity promotOrderDb = promotOrderService.find(PromotOrderEntity.class, promotOrderEntity.getId());
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        j.setContent(promotOrderDb);
        return j;
    }


}
