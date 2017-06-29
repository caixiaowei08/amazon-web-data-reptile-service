package com.amazon.service.recharge.controller;

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
@RequestMapping("/userRechargeFundController")
public class UserRechargeFundController extends BaseController {

    private static Logger logger = LogManager.getLogger(UserRechargeFundController.class.getName());

    @Autowired
    private UserRechargeFundService userRechargeFundService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(UserRechargeFundEntity.class, dataGrid, request.getParameterMap());
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
        DataGridReturn dataGridReturn = userRechargeFundService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }



    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(UserRechargeFundEntity userRechargeFundEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            userRechargeFundEntity.setUpdateTime(new Date());
            userRechargeFundEntity.setCreateTime(new Date());
            userRechargeFundService.save(userRechargeFundEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("保存失败！");
        }
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(UserRechargeFundEntity userRechargeFundEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String [] id_array = ids.split(",");
                for (int i = 0; i < id_array.length ; i++) {
                    userRechargeFundEntity = userRechargeFundService.find(UserRechargeFundEntity.class,Integer.parseInt(id_array[i]));
                    userRechargeFundService.delete(userRechargeFundEntity);
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
    public AjaxJson doGet(UserRechargeFundEntity userRechargeFundEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = userRechargeFundEntity.getId();
        if(id == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择你需要查看详情的数据！");
            return j;
        }
        UserRechargeFundEntity userRechargeFundEntityDb = userRechargeFundService.find(UserRechargeFundEntity.class, id);
        if(userRechargeFundEntityDb == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("数据不存在！");
            return j;
        }
        j.setContent(userRechargeFundEntityDb);
        return j;
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(UserRechargeFundEntity userRechargeFundEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        UserRechargeFundEntity t = userRechargeFundService.find(UserRechargeFundEntity.class, userRechargeFundEntity.getId());
        try {
            userRechargeFundEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(userRechargeFundEntity, t);
            userRechargeFundService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
        }
        return j;
    }

}
