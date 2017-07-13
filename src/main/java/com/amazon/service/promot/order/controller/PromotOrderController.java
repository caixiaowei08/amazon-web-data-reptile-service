package com.amazon.service.promot.order.controller;

import com.amazon.admin.constant.Constants;
import com.amazon.admin.poi.service.PoiPromotService;
import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.flow.service.PromotOrderEvaluateFlowService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.service.promot.order.vo.PromotOrderEvaluateVo;
import com.amazon.service.promot.price.entity.PromotPriceEntity;
import com.amazon.service.promot.price.service.PromotPriceService;
import com.amazon.service.spider.SpiderConstant;
import com.amazon.service.spider.pojo.AmazonPageInfoPojo;
import com.amazon.service.spider.service.SpiderService;
import com.amazon.service.user.controller.UserController;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.service.vip.service.UserMembershipService;
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
import org.framework.core.utils.DateUtils.DateUtils;
import org.framework.core.utils.RegularExpressionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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
import java.util.ArrayList;
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

    @Autowired
    private UserMembershipService userMembershipService;

    @Autowired
    private PoiPromotService poiPromotService;

    @Autowired
    private PromotOrderEvaluateFlowService promotOrderEvaluateFlowService;


    @RequestMapping(params = "goNewPromotOne")
    public String goNewPromotOne(HttpServletRequest request, HttpServletResponse response) {
        if (userMembershipService.isMemberShipVip()) {
            return "pages/promot/newPromotStepOne";
        } else {
            return "pages/fund/memberShip";
        }
    }

    @RequestMapping(params = "goNewPromotTwo")
    public String goNewPromotOneTwo(HttpServletRequest request, HttpServletResponse response) {
        if (userMembershipService.isMemberShipVip()) {
            return "pages/promot/newPromotStepTwo";
        } else {
            return "pages/fund/memberShip";
        }
    }

    @RequestMapping(params = "downEvaluateExcel")
    public void downEvaluateExcel(HttpServletRequest request, HttpServletResponse response) {
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            return;
        }
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, null, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
        }
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("sellerId", userEntity.getId()));
        criteriaQuery.getDetachedCriteria().addOrder(Order.desc("id"));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(criteriaQuery.getDetachedCriteria());
        List<PromotOrderEvaluateVo> promotOrderEvaluateVoList = new ArrayList<PromotOrderEvaluateVo>();
        if (CollectionUtils.isNotEmpty(promotOrderEntityList)) {
            for (PromotOrderEntity promotOrderEntity : promotOrderEntityList) {
                DetachedCriteria evaluateDetachedCriteria = DetachedCriteria.forClass(PromotOrderEvaluateFlowEntity.class);
                evaluateDetachedCriteria.add(Restrictions.eq("sellerId", promotOrderEntity.getSellerId()));
                evaluateDetachedCriteria.add(Restrictions.eq("promotId", promotOrderEntity.getId()));
                evaluateDetachedCriteria.addOrder(Order.desc("createTime"));
                List<PromotOrderEvaluateFlowEntity> promotOrderEvaluateFlowEntityList = promotOrderEvaluateFlowService.getListByCriteriaQuery(evaluateDetachedCriteria);
                if (CollectionUtils.isNotEmpty(promotOrderEvaluateFlowEntityList)) {
                    for (PromotOrderEvaluateFlowEntity promotOrderEvaluateFlowEntity : promotOrderEvaluateFlowEntityList) {
                        PromotOrderEvaluateVo promotOrderEvaluateVo = new PromotOrderEvaluateVo();
                        promotOrderEvaluateVo.setPromotId(promotOrderEvaluateFlowEntity.getPromotId());
                        promotOrderEvaluateVo.setAmzOrderId(promotOrderEvaluateFlowEntity.getAmzOrderId());
                        promotOrderEvaluateVo.setUpdateTime(promotOrderEvaluateFlowEntity.getUpdateTime());
                        promotOrderEvaluateVo.setAsin(promotOrderEvaluateFlowEntity.getAsinId());
                        if(promotOrderEvaluateFlowEntity.getState().equals(Constants.EVALUATE_STATE_REVIEW)){
                            promotOrderEvaluateVo.setCashback(promotOrderEntity.getCashback());
                            promotOrderEvaluateVo.setReviewPrice(promotOrderEntity.getReviewPrice());
                        }
                        promotOrderEvaluateVo.setIsComment(promotOrderEvaluateFlowEntity.getState());
                        promotOrderEvaluateVo.setRemark(promotOrderEntity.getRemark());
                        promotOrderEvaluateVoList.add(promotOrderEvaluateVo);
                    }
                }
            }
        }
        String excelFileNameHeader = "订单评论表" + DateUtils.getDate(new Date());
        try {
            poiPromotService.downEvaluateExcel(promotOrderEvaluateVoList, response, excelFileNameHeader);
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
        }
    }

    @RequestMapping(params = "downPromotOrderExcel")
    public void downPromotOrderExcel(HttpServletRequest request, HttpServletResponse response) {
        UserEntity userEntity = globalService.getUserEntityFromSession();
        if (userEntity == null) {
            return;
        }
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, null, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.error(e.fillInStackTrace());
        }
        criteriaQuery.getDetachedCriteria().addOrder(Order.desc("addDate"));
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("sellerId", userEntity.getId()));
        List<PromotOrderEntity> promotOrderEntityList = promotOrderService.getListByCriteriaQuery(criteriaQuery.getDetachedCriteria());
        String excelFileNameHeader = "平台订单表" + DateUtils.getDate(new Date());
        try {
            poiPromotService.downPromotOrderExcel(promotOrderEntityList, response, excelFileNameHeader);
        } catch (Exception e) {
            logger.error(e);
        }
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
        String url = "";
        if (RegularExpressionUtils.isHttpOrHttps(asinOrUrl)) {
            url = asinOrUrl;
        } else {
            String baseUrl = Constant.AMAZON_URL_PRODUCT;
            url = baseUrl.replace("#", asinOrUrl);
        }
        //校验价格设置
        DetachedCriteria promotOrderDetachedCriteria = DetachedCriteria.forClass(PromotPriceEntity.class);
        List<PromotPriceEntity> promotPriceEntityList = promotPriceService.getListByCriteriaQuery(promotOrderDetachedCriteria);
        if (CollectionUtils.isEmpty(promotPriceEntityList)) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未设置评价单价！");
            return j;
        }
        spiderService.spiderAmazonPageInfoSaveToHttpSession(url, 4);
        AmazonPageInfoPojo amazonPageInfoPojo = (AmazonPageInfoPojo) ContextHolderUtils.getSession().getAttribute(SpiderConstant.AMAZON_PAGE_INFO_POJO);
        if (amazonPageInfoPojo == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("解析网页失败，请检查输入ASIN或者URL是否准确！");
            return j;
        }
        if (StringUtils.isEmpty(amazonPageInfoPojo.getProductTitle())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能获取网页中的商品标题，请确认ASIN或者URL是否准确！");
            return j;
        }
        if (StringUtils.isEmpty(amazonPageInfoPojo.getPageUrl())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能获取商品网页链接，请确认ASIN或者URL是否准确！");
            return j;
        }
        if (StringUtils.isEmpty(amazonPageInfoPojo.getAsin())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能获取商品ASIN编号，请确认ASIN或者URL是否准确！");
            return j;
        }

        if (StringUtils.isEmpty(amazonPageInfoPojo.getPriceblockSaleprice())) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("未能获取商品价格，请联系客服！");
            return j;
        }
        amazonPageInfoPojo.setReviewPrice("$" + promotPriceEntityList.get(0).getReviewPrice().toString());
        ContextHolderUtils.getSession().setAttribute(SpiderConstant.AMAZON_PAGE_INFO_POJO, amazonPageInfoPojo);
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
            j.setMsg("登录超时，请重新建推广活动订单！");
            return j;
        }
        try {
            j = promotOrderService.doAddNewPromot(userEntity, amazonPageInfoPojo, promotOrderEntity);
        }catch (Exception e){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("订单提交错误，联系客服！");
            logger.error(e.fillInStackTrace());
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
