package com.amazon.service.promot.order.controller;

import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.service.spider.SpiderConstant;
import com.amazon.service.spider.pojo.AmazonPageInfoPojo;
import com.amazon.service.spider.service.SpiderService;
import com.amazon.service.user.entity.UserEntity;
import com.amazon.system.Constant;
import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGrid;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import com.amazon.system.system.bootstrap.utils.DatagridJsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.ContextHolderUtils;
import org.framework.core.utils.RegularExpressionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by User on 2017/6/10.
 */
@Scope("prototype")
@Controller
@RequestMapping("/promotOrderController")
public class PromotOrderController extends BaseController {

    @Autowired
    private PromotOrderService promotOrderService;

    @Autowired
    private SpiderService spiderService;

    @RequestMapping(params = "goNewPromotOne")
    public String goNewPromotOne(HttpServletRequest request, HttpServletResponse response) {
        return "pages/promot/newPromotStepOne";
    }

    @RequestMapping(params = "goNewPromotTwo")
    public String goNewPromotOneTwo(HttpServletRequest request, HttpServletResponse response) {
        return "pages/promot/newPromotStepTwo";
    }

    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid,HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, dataGrid, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        }catch (Exception e){

        }
        DataGridReturn dataGridReturn = promotOrderService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }

    @RequestMapping(params = "doGetPromotOrderTemp")
    @ResponseBody
    public AjaxJson doGetPromotOrderTemp(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        AmazonPageInfoPojo amazonPageInfoPojo = (AmazonPageInfoPojo) ContextHolderUtils.getSession().getAttribute(SpiderConstant.AMAZON_PAGE_INFO_POJO);
        if(amazonPageInfoPojo == null){
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

        boolean flag = spiderService.spiderAmazonPageInfoSaveToHttpSession(url, 2);
        AmazonPageInfoPojo amazonPageInfoPojo = (AmazonPageInfoPojo) ContextHolderUtils.getSession().getAttribute(SpiderConstant.AMAZON_PAGE_INFO_POJO);
        if (amazonPageInfoPojo != null
                && StringUtils.hasText(amazonPageInfoPojo.getProductTitle())
                && StringUtils.hasText(amazonPageInfoPojo.getPriceblockSaleprice())
                && StringUtils.hasText(amazonPageInfoPojo.getPageUrl())
                ) {
            amazonPageInfoPojo.setAsin(asin);
            amazonPageInfoPojo.setReviewPrice("$3");
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
    public AjaxJson doAdd(PromotOrderEntity promotOrderEntity,HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        AmazonPageInfoPojo amazonPageInfoPojo = (AmazonPageInfoPojo) ContextHolderUtils.getSession().getAttribute(SpiderConstant.AMAZON_PAGE_INFO_POJO);
        if(amazonPageInfoPojo == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请重新建推广活动订单！");
            return j;
        }
        UserEntity userEntity = (UserEntity )ContextHolderUtils.getSession().getAttribute(Constant.USER_SESSION_CONSTANTS);
        if(userEntity == null){
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请重新登录！");
            return j;
        }
        promotOrderEntity.setSellerId(userEntity.getId());
        promotOrderEntity.setAsinId(amazonPageInfoPojo.getAsin());
        promotOrderEntity.setProductUrl(amazonPageInfoPojo.getPageUrl());
        promotOrderEntity.setProductTitle(amazonPageInfoPojo.getProductTitle());
        promotOrderEntity.setBrand(amazonPageInfoPojo.getBrand());
        promotOrderEntity.setThumbnail(amazonPageInfoPojo.getLandingImage());
        promotOrderEntity.setState(Constant.STATE_Y);
        promotOrderEntity.setSalePrice(amazonPageInfoPojo.getPriceblockSaleprice());
        promotOrderEntity.setAddDate(new Date());
        promotOrderEntity.setFinishDate(promotOrderEntity.getFinishDate());
        promotOrderEntity.setReviewPrice(new BigDecimal("0.00"));
        promotOrderEntity.setGuaranteeFund(new BigDecimal("0.00"));
        promotOrderEntity.setConsumption(new BigDecimal("0.00"));
        promotOrderEntity.setNeedReviewNum(promotOrderEntity.getNeedReviewNum());
        promotOrderEntity.setDayReviewNum(promotOrderEntity.getDayReviewNum());
        promotOrderEntity.setBuyerNum(0);
        promotOrderEntity.setCreateTime(new Date());
        promotOrderEntity.setUpdateTime(new Date());
        promotOrderService.save(promotOrderEntity);
        return j;
    }

    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(PromotOrderEntity promotOrderEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String ids = request.getParameter("ids");
        try {
            if (StringUtils.hasText(ids)) {
                String[] id_array = ids.split(",");
                for (int i = 0; i < id_array.length; i++) {
                    promotOrderEntity = promotOrderService.find(PromotOrderEntity.class, Integer.parseInt(id_array[i]));
                    promotOrderService.delete(promotOrderEntity);
                }
            } else {
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
    public AjaxJson doGet(PromotOrderEntity promotOrderEntity, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        Integer id = promotOrderEntity.getId();
        if (id == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("请选择你需要查看详情的数据！");
            return j;
        }
        PromotOrderEntity promotOrderEntityDb = promotOrderService.find(PromotOrderEntity.class, id);
        if (promotOrderEntityDb == null) {
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("数据不存在！");
            return j;
        }
        j.setContent(promotOrderEntityDb);
        return j;
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(PromotOrderEntity promotOrderEntity, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        PromotOrderEntity t = promotOrderService.find(PromotOrderEntity.class, promotOrderEntity.getId());
        try {
            promotOrderEntity.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(promotOrderEntity, t);
            promotOrderService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("更新失败！");
        }
        return j;
    }
}
