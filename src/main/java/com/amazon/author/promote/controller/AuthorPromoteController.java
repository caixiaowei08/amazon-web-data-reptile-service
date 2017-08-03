package com.amazon.author.promote.controller;

import com.amazon.author.account.controller.AuthorUserController;
import com.amazon.author.account.entity.AuthorUserEntity;
import com.amazon.author.poi.service.AuthorPoiService;
import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
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
import org.framework.core.utils.DateUtils.DateUtils;
import org.framework.core.utils.PasswordUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private AuthorPoiService authorPoiService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, dataGrid, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.error("组装查询出错！", e);
        }
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        if (authorUserEntity == null) {
            return;
        }
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("authorId", authorUserEntity.getId()));
        DataGridReturn dataGridReturn = promotOrderService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(PromotOrderEntity promotOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        if (authorUserEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        detachedCriteria.add(Restrictions.eq("id",promotOrderEntity.getId()));
        detachedCriteria.add(Restrictions.eq("authorId", authorUserEntity.getId()));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(detachedCriteria);
        if(CollectionUtils.isNotEmpty(promotOrderEntityList)){
            j.setSuccess(AjaxJson.CODE_SUCCESS);
            j.setContent(promotOrderEntityList.get(0));
            return j;
        }else{
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("获取订单信息失败！");
            return j;
        }
    }

    @RequestMapping(params = "downLoadPromoteOrderExcel")
    public void downLoadPromoteOrderExcel(PromotOrderEntity promotOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        if (authorUserEntity == null) {
            return;
        }
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, null, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
        }
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("authorId",authorUserEntity.getId()));
        criteriaQuery.getDetachedCriteria().addOrder(Order.desc("addDate"));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(criteriaQuery.getDetachedCriteria());
        String excelFileNameHeader = "平台订单表(订单管理端)" + DateUtils.getDate(new Date());
        try {
            authorPoiService.downLoadPromoteOrderExcel(promotOrderEntityList, response, excelFileNameHeader);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
        }
    }

}
