package com.amazon.service.promot.flow.controller;

import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.flow.service.PromotOrderEvaluateFlowService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.price.entity.PromotPriceEntity;
import com.amazon.service.recharge.controller.UserRechargeFundController;
import com.amazon.service.recharge.entity.UserRechargeFundEntity;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.system.Constant;
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
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.ContextHolderUtils;
import org.hibernate.criterion.DetachedCriteria;
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
@RequestMapping("/promotOrderEvaluateFlowController")
public class PromotOrderEvaluateFlowController extends BaseController {

    private static Logger logger = LogManager.getLogger(PromotOrderEvaluateFlowController.class.getName());

    @Autowired
    private PromotOrderEvaluateFlowService promotOrderEvaluateFlowService;

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEvaluateFlowEntity.class, dataGrid, request.getParameterMap());
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            return;
        }
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
        }
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("sellerId", userEntity.getId()));//加上用户
        DataGridReturn dataGridReturn = promotOrderEvaluateFlowService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doComplaint")
    @ResponseBody
    public AjaxJson doUpdate(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }
        if (promotOrderEvaluateFlowEntity.getId() == null
                || promotOrderEvaluateFlowEntity.getComplaint() == null
                || promotOrderEvaluateFlowEntity.getComplaint() < 0
                || promotOrderEvaluateFlowEntity.getComplaint() > 5
                ) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setContent("投诉信息有误，请确认后重试!");
            return j;
        }

        DetachedCriteria promotOrderEvaluateFlowDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateFlowDetachedCriteria.add(Restrictions.eq("id", promotOrderEvaluateFlowEntity.getId()));
        promotOrderEvaluateFlowDetachedCriteria.add(Restrictions.eq("sellerId", userEntity.getId()));
        List<PromotOrderEvaluateFlowEntity> promotOrderEvaluateFlowEntityList = promotOrderEvaluateFlowService.getListByCriteriaQuery(promotOrderEvaluateFlowDetachedCriteria);
        if (CollectionUtils.isEmpty(promotOrderEvaluateFlowEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未找到投诉的评价！");
            return j;
        }
        PromotOrderEvaluateFlowEntity t = promotOrderEvaluateFlowEntityList.get(0);
        try {
            t.setUpdateTime(new Date());
            t.setComplaint(promotOrderEvaluateFlowEntity.getComplaint());
            t.setRemark(promotOrderEvaluateFlowEntity.getRemark());
            promotOrderEvaluateFlowService.saveOrUpdate(t);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("投诉失败");
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setContent("投诉成功！");
        return j;
    }
}
