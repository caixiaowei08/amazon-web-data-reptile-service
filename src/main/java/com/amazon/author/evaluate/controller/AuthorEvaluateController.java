package com.amazon.author.evaluate.controller;

import com.amazon.admin.constant.Constants;
import com.amazon.admin.evaluate.service.EvaluateService;
import com.amazon.author.account.entity.AuthorUserEntity;
import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.flow.service.PromotOrderEvaluateFlowService;
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
import org.framework.core.utils.RegularExpressionUtils;
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
import java.util.List;

/**
 * Created by User on 2017/8/3.
 */
@Scope("prototype")
@Controller
@RequestMapping("/author/evaluateController")
public class AuthorEvaluateController extends BaseController {

    private static Logger logger = LogManager.getLogger(AuthorEvaluateController.class.getName());

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private GlobalService globalService;

    @Autowired
    private PromotOrderEvaluateFlowService promotOrderEvaluateFlowService;


    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        if (authorUserEntity == null) {
            return;
        }
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEvaluateFlowEntity.class, dataGrid, request.getParameterMap());
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("authorId", authorUserEntity.getId()));
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
        }
        DataGridReturn dataGridReturn = evaluateService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEvaluateFlowEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }


    @RequestMapping(params = "doAddNewReview")
    @ResponseBody
    public AjaxJson doAddNewReview(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        logger.info("----/author/evaluateController----doAddNewReview---start----");
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        if (authorUserEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }
        String amzOrderId = promotOrderEvaluateFlowEntity.getAmzOrderId();
        String asinId = promotOrderEvaluateFlowEntity.getAsinId();
        String reviewUrl = promotOrderEvaluateFlowEntity.getReviewUrl();
        promotOrderEvaluateFlowEntity.setAuthorId(authorUserEntity.getId());
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
                            Constant.AMAZON_URL_PRODUCT_COMMENT.replace(
                                    "#", reviewUrl.substring(reviewUrl.lastIndexOf("/") + 1,
                                            reviewUrl.lastIndexOf("?"))
                            )
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
            e.printStackTrace();
            logger.error(e);
            return j;
        }
        return j;
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        if (authorUserEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }
        DetachedCriteria promotOrderEvaluateDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateDetachedCriteria.add(Restrictions.eq("id", promotOrderEvaluateFlowEntity.getId()));
        promotOrderEvaluateDetachedCriteria.add(Restrictions.eq("authorId", authorUserEntity.getId()));
        List<PromotOrderEvaluateFlowEntity> promotOrderExistFlowEntityList =
                promotOrderEvaluateFlowService.getListByCriteriaQuery(promotOrderEvaluateDetachedCriteria);
        if (CollectionUtils.isEmpty(promotOrderExistFlowEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("评论不存在或者被删除！");
            return j;
        }
        j.setContent(promotOrderExistFlowEntityList.get(0));
        return j;
    }

    @RequestMapping(params = "doOrderNoUpdate")
    @ResponseBody
    public AjaxJson doOrderNoUpdate(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        if (authorUserEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }

        if (StringUtils.isEmpty(promotOrderEvaluateFlowEntity.getAmzOrderId())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("亚马逊订单号不能为空！");
            return j;
        }

        DetachedCriteria promotOrderEvaluateDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateDetachedCriteria.add(Restrictions.eq("id", promotOrderEvaluateFlowEntity.getId()));
        promotOrderEvaluateDetachedCriteria.add(Restrictions.eq("authorId", authorUserEntity.getId()));
        List<PromotOrderEvaluateFlowEntity> promotOrderEvaluateFlowEntityList = promotOrderEvaluateFlowService.getListByCriteriaQuery(promotOrderEvaluateDetachedCriteria);
        if (CollectionUtils.isEmpty(promotOrderEvaluateFlowEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("评价不存在或者已被删除!");
            return j;
        }
        PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowDb = promotOrderEvaluateFlowEntityList.get(0);
        //去重校验
        DetachedCriteria promotOrderEvaluateExistDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
        promotOrderEvaluateExistDetachedCriteria.add(Restrictions.eq("amzOrderId", promotOrderEvaluateFlowEntity.getAmzOrderId()));
        promotOrderEvaluateExistDetachedCriteria.add(Restrictions.eq("asinId", promotOrderEvaluateFlowDb.getAsinId()));
        List<PromotOrderEvaluateFlowEntity> promotOrderExistFlowEntityList = promotOrderEvaluateFlowService.getListByCriteriaQuery(promotOrderEvaluateExistDetachedCriteria);
        if (CollectionUtils.isNotEmpty(promotOrderExistFlowEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("亚马逊订单号和ASIN编号同时相同的评价已存在，勿重复录入！");
            return j;
        }
        try {
            promotOrderEvaluateFlowDb.setAmzOrderId(promotOrderEvaluateFlowEntity.getAmzOrderId());
            evaluateService.saveOrUpdate(promotOrderEvaluateFlowDb);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改亚马逊订单号失败！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "doAddReviewUrl")
    @ResponseBody
    public AjaxJson doAddReviewUrl(PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        logger.info("----/author/evaluateController----doAddReviewUrl---start----");
        AuthorUserEntity authorUserEntity = globalService.getAuthorUserEntityFromSession();
        if (authorUserEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
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
            promotOrderEvaluateFlowDb.setPayPal(promotOrderEvaluateFlowEntity.getPayPal());
            promotOrderEvaluateFlowDb.setWeChat(promotOrderEvaluateFlowEntity.getWeChat());
            promotOrderEvaluateFlowDb.setZfb(promotOrderEvaluateFlowEntity.getZfb());
            j = evaluateService.doAddReviewUrl(promotOrderEvaluateFlowEntity, promotOrderEvaluateFlowDb);
        } catch (Exception e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("追加评论失败！");
            logger.error(e.fillInStackTrace());
            return j;
        }
        return j;
    }


}
