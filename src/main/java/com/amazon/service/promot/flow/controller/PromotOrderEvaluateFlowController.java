package com.amazon.service.promot.flow.controller;

import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.flow.service.PromotOrderEvaluateFlowService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.recharge.controller.UserRechargeFundController;
import com.amazon.service.recharge.entity.UserRechargeFundEntity;
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
import org.framework.core.utils.BeanUtils;
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
import java.io.IOException;
import java.util.Date;

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

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEvaluateFlowEntity.class, dataGrid, request.getParameterMap());
        UserEntity userEntity = (UserEntity) ContextHolderUtils.getSession().getAttribute(Constant.USER_SESSION_CONSTANTS);
        if (userEntity == null) {
            try {
                response.sendRedirect("/loginController.do?login");
                return;
            } catch (IOException e) {
                logger.info("退出登录失败！", e);
            }

        }
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.info("组装查询出错！", e);
        }
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("sellerId", userEntity.getId()));//加上用户
        DataGridReturn dataGridReturn = promotOrderEvaluateFlowService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }


    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            promotOrderEvaluateFlowEntity.setUpdateTime(new Date());
            promotOrderEvaluateFlowEntity.setCreateTime(new Date());
            promotOrderEvaluateFlowService.save(promotOrderEvaluateFlowEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
        }
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String [] id_array = ids.split(",");
                for (int i = 0; i < id_array.length ; i++) {
                    promotOrderEvaluateFlowEntity = promotOrderEvaluateFlowService.find(PromotOrderEvaluateFlowEntity.class,Integer.parseInt(id_array[i]));
                    promotOrderEvaluateFlowService.delete(promotOrderEvaluateFlowEntity);
                }
            }else{
                j.setSuccess(AjaxJson.CODE_FAIL);
                j.setMsg("请选择需要删除的数据！");
                return j;
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("删除失败！");
        }
        return j;
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = promotOrderEvaluateFlowEntity.getId();
        if(id == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择你需要查看详情的数据！");
            return j;
        }
        PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowDb = promotOrderEvaluateFlowService.find(PromotOrderEvaluateFlowEntity.class, id);
        if(promotOrderEvaluateFlowDb == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("数据不存在！");
            return j;
        }
        j.setContent(promotOrderEvaluateFlowDb);
        return j;
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        PromotOrderEvaluateFlowEntity t = promotOrderEvaluateFlowService.find(PromotOrderEvaluateFlowEntity.class, promotOrderEvaluateFlowEntity.getId());
        try {
            promotOrderEvaluateFlowEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(promotOrderEvaluateFlowEntity, t);
            promotOrderEvaluateFlowService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
        }
        return j;
    }
}
