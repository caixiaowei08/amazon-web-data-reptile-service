package com.amazon.admin.evaluate.controller;

import com.amazon.admin.account.entity.AdminSystemEntity;
import com.amazon.admin.constant.Constants;
import com.amazon.admin.evaluate.service.EvaluateService;
import com.amazon.admin.promot.controller.AdminPromotController;
import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.flow.service.PromotOrderEvaluateFlowService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.system.Constant;
import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGrid;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import com.amazon.system.system.bootstrap.utils.DatagridJsonUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.core.utils.RegularExpressionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.GenericServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by User on 2017/6/23.
 */
@Scope("prototype")
@Controller
@RequestMapping("/evaluateController")
public class EvaluateController extends BaseController {

    private static Logger logger = LogManager.getLogger(EvaluateController.class.getName());

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private PromotOrderService promotOrderService;

    @Autowired
    private PromotOrderEvaluateFlowService promotOrderEvaluateFlowService;

    @Autowired
    private GlobalService globalService;

    @RequestMapping(params = "goEvaluateDetail")
    public String goAdminPageIndex(HttpServletRequest request, HttpServletResponse response) {
        if (globalService.isNotAdminLogin()) {
            return "/adminSystemController.admin?goAdminLogin";
        }
        return "admin/evaluate/evaluateDetail";
    }

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        logger.info("----evaluateController----doAdd---start----");
        if (globalService.isNotAdminLogin()) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请用管理员账户登录！");
            return j;
        }
        String amzOrderId = promotOrderEvaluateFlowEntity.getAmzOrderId();
        String asinId = promotOrderEvaluateFlowEntity.getAsinId();
        String reviewUrl = promotOrderEvaluateFlowEntity.getReviewUrl();
        if ((!StringUtils.hasText(amzOrderId)) || (!StringUtils.hasText(asinId))) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("亚马逊订单号和ASIN编号必填！");
            return j;
        }

        try {
            if (StringUtils.hasText(reviewUrl)) {
                //解决 电脑评价 和 手机网站评价不一致问题----》 统一转换为 PC端 评价链接
                //https://www.amazon.com/gp/aw/review/B01CJ6SO6O/R27F2QIMFNUZLS?ref_=glimp_1rv_cl
                if (reviewUrl.contains(Constant.AMAZON_URL_PRODUCT_COMMENT_PHONE_FLAG)) {//手机网站
                    promotOrderEvaluateFlowEntity.setReviewUrl(
                            Constant.AMAZON_URL_PRODUCT_COMMENT.replace("#",
                                    reviewUrl.substring(reviewUrl.lastIndexOf("/") + 1, reviewUrl.lastIndexOf("?")))
                    );
                }
                logger.info("----evaluateController----doAddEvaluateWithReviewUrl---start----");
                j = evaluateService.doAddEvaluateWithReviewUrl(promotOrderEvaluateFlowEntity);
            } else {
                logger.info("----evaluateController----doAddEvaluateWithNoReviewUrl---start----");
                j = evaluateService.doAddEvaluateWithNoReviewUrl(promotOrderEvaluateFlowEntity);
            }
        } catch (Exception e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("添加评论失败！");
            logger.error(e.fillInStackTrace());
            return j;
        }
        return j;
    }

    @RequestMapping(params = "doAddReviewUrl")
    @ResponseBody
    public AjaxJson doAddReviewUrl(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        logger.info("----evaluateController----doAddReviewUrl---start----");
        if (globalService.isNotAdminLogin()) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请用管理员账户登录！");
            return j;
        }

        if (StringUtils.isEmpty(promotOrderEvaluateFlowEntity.getReviewUrl())
                || !RegularExpressionUtils.isHttpOrHttps(promotOrderEvaluateFlowEntity.getReviewUrl())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入评价链接或者有效的评价链接！");
            return j;
        }

        PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowDb = promotOrderEvaluateFlowService.find(PromotOrderEvaluateFlowEntity.class, promotOrderEvaluateFlowEntity.getId());

        if (promotOrderEvaluateFlowDb == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("评论已被删除！");
            return j;
        }

        if (promotOrderEvaluateFlowDb.getState().equals(Constants.EVALUATE_STATE_REVIEW)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("评论链接已被追加，处于review状态！");
            return j;
        }

        String reviewUrl = promotOrderEvaluateFlowEntity.getReviewUrl();
        try {
            if (reviewUrl.contains(Constant.AMAZON_URL_PRODUCT_COMMENT_PHONE_FLAG)) {//手机网站
                promotOrderEvaluateFlowEntity.setReviewUrl(
                        Constant.AMAZON_URL_PRODUCT_COMMENT.replace("#",
                                reviewUrl.substring(reviewUrl.lastIndexOf("/") + 1, reviewUrl.lastIndexOf("?")))
                );
            }
            j = evaluateService.doAddReviewUrl(promotOrderEvaluateFlowEntity, promotOrderEvaluateFlowDb);
        } catch (Exception e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("追加评论失败！");
            logger.error(e.fillInStackTrace());
            return j;
        }
        return j;
    }

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        if (globalService.isNotAdminLogin()) {
            return;
        }
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEvaluateFlowEntity.class, dataGrid, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
        }
        DataGridReturn dataGridReturn = evaluateService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(PromotOrderEntity promotOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();

        if (globalService.isNotAdminLogin()) {
            j.setSuccess(AjaxJson.RELOGIN);
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


    @RequestMapping(params = "doFindComment")
    @ResponseBody
    public AjaxJson doFindComment(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        if (globalService.isNotAdminLogin()) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请先登录管理账号！");
            return j;
        }
        Integer id = promotOrderEvaluateFlowEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入评价编号！");
            return j;
        }
        PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntityDb = promotOrderEvaluateFlowService.find(PromotOrderEvaluateFlowEntity.class, id);
        if (promotOrderEvaluateFlowEntityDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("评论不存在或者被删除！");
            return j;
        }
        j.setContent(promotOrderEvaluateFlowEntityDb);
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        if (globalService.isNotAdminLogin()) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请先登录管理账号！");
            return j;
        }
        PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowDb = evaluateService.find(PromotOrderEvaluateFlowEntity.class, promotOrderEvaluateFlowEntity.getId());
        if (promotOrderEvaluateFlowDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("评价已被删除或者不存在！");
            return j;
        }

        try {
            j = evaluateService.doDelPromotOrderEvaluate(promotOrderEvaluateFlowDb);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("删除失败！");
            return j;
        }
        return j;
    }

}
