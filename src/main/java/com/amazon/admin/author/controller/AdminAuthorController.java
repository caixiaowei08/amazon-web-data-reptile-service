package com.amazon.admin.author.controller;

import com.alibaba.fastjson.JSON;
import com.amazon.admin.account.entity.AdminSystemEntity;
import com.amazon.author.account.controller.AuthorUserController;
import com.amazon.author.account.entity.AuthorUserEntity;
import com.amazon.author.account.service.AuthorUserService;
import com.amazon.author.common.constant.AuthorConstant;
import com.amazon.buyer.account.entity.BuyerUserEntity;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
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
import org.framework.core.utils.PasswordUtil;
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
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/8/2.
 */
@Scope("prototype")
@Controller
@RequestMapping("/adminAuthorController")
public class AdminAuthorController extends BaseController {

    private static Logger logger = LogManager.getLogger(AdminAuthorController.class.getName());

    @Autowired
    private GlobalService globalService;

    @Autowired
    private AuthorUserService authorUserService;

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(AuthorUserEntity.class, dataGrid, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.error("组装查询出错！", e);
        }
        DataGridReturn dataGridReturn = authorUserService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, AuthorUserEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(AuthorUserEntity authorUserEntity, HttpServletRequest request, HttpServletResponse response) {
        logger.info("新增普通管理员 start -----" + JSON.toJSONString(authorUserEntity));
        AjaxJson j = new AjaxJson();
        if (StringUtils.isEmpty(authorUserEntity.getAccount())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入普通会员账号！");
            return j;
        }

        if (StringUtils.isEmpty(authorUserEntity.getPwd())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入普通会员登录密码！");
            return j;
        }

        AdminSystemEntity adminSystemEntity = globalService.getAdminEntityFromSession();
        if (adminSystemEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }
        logger.info("新增普通管理员 adminSystemEntity -----" + JSON.toJSONString(adminSystemEntity));
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AuthorUserEntity.class);
        detachedCriteria.add(Restrictions.eq("account", authorUserEntity.getAccount()));
        List<AuthorUserEntity> authorUserEntityList = authorUserService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isNotEmpty(authorUserEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("注册账号已存在！");
            return j;
        }
        authorUserEntity.setName(authorUserEntity.getAccount());
        authorUserEntity.setStatus(AuthorConstant.AUTHOR_NORMAL_CONSTANTS);
        authorUserEntity.setPwd(PasswordUtil.getMD5Encryp(authorUserEntity.getPwd()));
        authorUserEntity.setCreateTime(new Date());
        authorUserEntity.setUpdateTime(new Date());
        try {
            authorUserService.save(authorUserEntity);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("新增普通会员失败！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setMsg("新增普通会员成功！");
        logger.info("新增普通会员成功 -----" + JSON.toJSONString(authorUserEntity));
        return j;
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(AuthorUserEntity authorUserEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            AuthorUserEntity authorUserDb = authorUserService.find(AuthorUserEntity.class, authorUserEntity.getId());
            j.setSuccess(AjaxJson.CODE_SUCCESS);
            j.setContent(authorUserDb);
            return j;
        } catch (Exception e) {
            logger.error(e);
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改普通会员失败！");
            return j;
        }
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(AuthorUserEntity authorUserEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            AuthorUserEntity authorUserDb = authorUserService.find(AuthorUserEntity.class, authorUserEntity.getId());
            authorUserDb.setStatus(authorUserEntity.getStatus());
            authorUserDb.setUpdateTime(new Date());
            authorUserService.saveOrUpdate(authorUserDb);
        } catch (Exception e) {
            logger.error(e);
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改普通会员失败！");
            return j;
        }
        return j;
    }


}
