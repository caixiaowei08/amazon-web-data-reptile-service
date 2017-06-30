package com.amazon.service.promot.order.controller;

import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.service.promot.price.entity.PromotPriceEntity;
import com.amazon.service.promot.price.service.PromotPriceService;
import com.amazon.service.spider.SpiderConstant;
import com.amazon.service.spider.pojo.AmazonPageInfoPojo;
import com.amazon.service.spider.service.SpiderService;
import com.amazon.service.user.controller.UserController;
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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created by User on 2017/6/10.
 */
@Scope("prototype")
@Controller
@RequestMapping("/promotOrderController")
public class PromotOrderController extends BaseController {

    private static Logger logger = LogManager.getLogger(PromotOrderController.class.getName());

    @Autowired
    private PromotOrderService promotOrderService;

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private GlobalService globalService;

    @Autowired
    private PromotPriceService promotPriceService;

    @RequestMapping(params = "goNewPromotOne")
    public String goNewPromotOne(HttpServletRequest request, HttpServletResponse response) {
        return "pages/promot/newPromotStepOne";
    }

    @RequestMapping(params = "goNewPromotTwo")
    public String goNewPromotOneTwo(HttpServletRequest request, HttpServletResponse response) {
        return "pages/promot/newPromotStepTwo";
    }

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            return;
        }
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, dataGrid, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.info("组装查询出错！", e);
        }
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("sellerId", userEntity.getId()));//加上用户
        DataGridReturn dataGridReturn = promotOrderService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doGetPromotOrderTemp")
    @ResponseBody
    public AjaxJson doGetPromotOrderTemp(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }
        AmazonPageInfoPojo amazonPageInfoPojo = (AmazonPageInfoPojo) ContextHolderUtils.getSession().getAttribute(SpiderConstant.AMAZON_PAGE_INFO_POJO);
        if (amazonPageInfoPojo == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请回到新建订单第一步，重新进行！");
            return j;
        }
        j.setSuccess(AjaxJson.CODE_SUCCESS);
        j.setContent(amazonPageInfoPojo);
        return j;
    }


    @RequestMapping(params = "doDealAsinOrUrl")
    @ResponseBody
    public AjaxJson doDealAsinOrUrl(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();

        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }

        String asinOrUrl = request.getParameter("asinOrUrl");
        if (!StringUtils.hasText(asinOrUrl)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请输入ASIN或者亚马逊商品主页链接！");
            return j;
        }
        String asin = "";
        String url = "";
        if (RegularExpressionUtils.isHttpOrHttps(asinOrUrl)) {
            int start = asinOrUrl.indexOf("/dp/") + 4;
            int end = asinOrUrl.indexOf("/ref=");
            asin = asinOrUrl.substring(start, end);
            url = asinOrUrl;
        } else {
            asin = asinOrUrl;
            String baseUrl = Constant.AMAZON_URL_PRODUCT;
            url = baseUrl.replace("#", asinOrUrl);
        }
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotPriceEntity.class);
        List<PromotPriceEntity> promotPriceEntityList = promotPriceService.getListByCriteriaQuery(promotOrderDetachedCriteria);

        if (CollectionUtils.isEmpty(promotPriceEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未设置评价单价！");
            return j;
        }

        boolean flag = spiderService.spiderAmazonPageInfoSaveToHttpSession(url, 2);
        AmazonPageInfoPojo amazonPageInfoPojo = (AmazonPageInfoPojo) ContextHolderUtils.getSession().getAttribute(SpiderConstant.AMAZON_PAGE_INFO_POJO);
        if (amazonPageInfoPojo != null
                && StringUtils.hasText(amazonPageInfoPojo.getProductTitle())
                && StringUtils.hasText(amazonPageInfoPojo.getPriceblockSaleprice())
                && StringUtils.hasText(amazonPageInfoPojo.getPageUrl())
                ) {
            amazonPageInfoPojo.setAsin(asin);
            amazonPageInfoPojo.setReviewPrice("$" + promotPriceEntityList.get(0).getReviewPrice().toString());
            ContextHolderUtils.getSession().setAttribute(SpiderConstant.AMAZON_PAGE_INFO_POJO, amazonPageInfoPojo);
        } else {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("ASIN或者亚马逊商品主页链接有误，请确认之后，再尝试！");
            return j;
        }
        return j;
    }

    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(PromotOrderEntity promotOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            j.setSuccess(AjaxJson.RELOGIN);
            j.setMsg("请重新登录！");
            return j;
        }

        AmazonPageInfoPojo amazonPageInfoPojo = (AmazonPageInfoPojo) ContextHolderUtils.getSession().getAttribute(SpiderConstant.AMAZON_PAGE_INFO_POJO);
        if (amazonPageInfoPojo == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请重新建推广活动订单！");
            return j;
        }

        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteria.add(Restrictions.eq("asinId", amazonPageInfoPojo.getAsin()));
        promotOrderDetachedCriteria.add(Restrictions.eq("state", Constant.STATE_Y));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(promotOrderDetachedCriteria);
        if (CollectionUtils.isNotEmpty(promotOrderEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("不能重复建立ASIN编号相同且处于开启状态的活动！");
            return j;
        }

        j = promotOrderService.doAddNewPromot(userEntity, amazonPageInfoPojo, promotOrderEntity);

        return j;
    }

    @RequestMapping(params = "doCloseById")
    @ResponseBody
    public AjaxJson doCloseById(PromotOrderEntity promotOrderEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        //安全校验
        UserEntity userEntity = (UserEntity) ContextHolderUtils.getSession().getAttribute(Constant.USER_SESSION_CONSTANTS);
        if (userEntity == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请重新登录！");
            return j;
        }
        try {
            DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
            promotOrderDetachedCriteria.add(Restrictions.eq("sellerId", userEntity.getId()));
            promotOrderDetachedCriteria.add(Restrictions.eq("id", promotOrderEntity.getId()));
            List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(promotOrderDetachedCriteria);
            if (CollectionUtils.isNotEmpty(promotOrderEntityList)) { //归还资金
               j = promotOrderService.doClosedPromotOrderById(promotOrderEntityList.get(0));
            }
        } catch (Exception e) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("关闭失败！");
        }
        return j;
    }

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(PromotOrderEntity promotOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = promotOrderEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择你需要查看详情的数据！");
            return j;
        }

        UserEntity userEntity = globalService.getUserEntityFromSession();
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotOrderEntity.class);
        promotOrderDetachedCriteria.add(Restrictions.eq("sellerId", userEntity.getId()));
        promotOrderDetachedCriteria.add(Restrictions.eq("id", promotOrderEntity.getId()));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(promotOrderDetachedCriteria);

        if (CollectionUtils.isEmpty(promotOrderEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("数据不存在！");
            return j;
        }
        j.setContent(promotOrderEntityList.get(0));
        return j;
    }
}
