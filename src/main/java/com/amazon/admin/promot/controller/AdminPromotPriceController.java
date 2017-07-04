package com.amazon.admin.promot.controller;

import com.amazon.admin.account.entity.AdminSystemEntity;
import com.amazon.admin.promot.vo.PromotPriceVo;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.service.promot.price.entity.PromotPriceEntity;
import com.amazon.service.promot.price.service.PromotPriceService;
import org.apache.commons.collections.CollectionUtils;
import org.framework.core.common.controller.BaseController;
import org.framework.core.common.model.json.AjaxJson;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.BeanUtils;
import org.framework.core.utils.RegularExpressionUtils;
import org.hibernate.criterion.DetachedCriteria;
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
import java.util.List;

/**
 * Created by User on 2017/7/4.
 */
@Scope("prototype")
@Controller
@RequestMapping("/adminPromotPriceController")
public class AdminPromotPriceController extends BaseController {

    @Autowired
    private GlobalService globalService;

    @Autowired
    private PromotPriceService promotPriceService;

    @RequestMapping(params = "doGet")
    @ResponseBody
    public AjaxJson doGet(HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        if (globalService.isNotAdminLogin()) {
            j.setContent(AjaxJson.RELOGIN);
            j.setMsg("请登录管理员账号！");
            return j;
        }

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PromotPriceEntity.class);
        List<PromotPriceEntity> promotPriceEntityList = promotPriceService.getListByCriteriaQuery(detachedCriteria);
        if (CollectionUtils.isEmpty(promotPriceEntityList)) {
            j.setContent(AjaxJson.CODE_FAIL);
            j.setMsg("未设置系统价格汇率!");
            return j;
        }
        j.setContent(promotPriceEntityList.get(0));
        return j;
    }

    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(PromotPriceVo promotPriceVo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        if (globalService.isNotAdminLogin()) {
            j.setContent(AjaxJson.RELOGIN);
            j.setMsg("请登录管理员账号！");
            return j;
        }

        PromotPriceEntity promotPriceEntityTremp = new PromotPriceEntity();
        if (promotPriceVo.getId() == null) {
            j.setContent(AjaxJson.CODE_FAIL);
            j.setMsg("请选择修改数据！");
            return j;
        }

        if(StringUtils.hasText(promotPriceVo.getMonthRent())){
            if(RegularExpressionUtils.isFourDecimalNumber(promotPriceVo.getMonthRent())){
                promotPriceEntityTremp.setMonthRent(new BigDecimal(promotPriceVo.getMonthRent()));
            }else{
                j.setContent(AjaxJson.CODE_FAIL);
                j.setMsg("输入月租会员价格错误!");
                return j;
            }
        }

        if(StringUtils.hasText(promotPriceVo.getReviewPrice())){
            if(RegularExpressionUtils.isFourDecimalNumber(promotPriceVo.getReviewPrice())){
                promotPriceEntityTremp.setReviewPrice(new BigDecimal(promotPriceVo.getReviewPrice()));
            }else{
                j.setContent(AjaxJson.CODE_FAIL);
                j.setMsg("输入单个评价价格错误!");
                return j;
            }
        }

        if(StringUtils.hasText(promotPriceVo.getExchangeRate())){
            if(RegularExpressionUtils.isFourDecimalNumber(promotPriceVo.getExchangeRate())){
                promotPriceEntityTremp.setExchangeRate(new BigDecimal(promotPriceVo.getExchangeRate()));
            }else{
                j.setContent(AjaxJson.CODE_FAIL);
                j.setMsg("输入汇率错误！");
                return j;
            }
        }

        PromotPriceEntity promotPriceEntity = promotPriceService.find(PromotPriceEntity.class, promotPriceVo.getId());
        if (promotPriceEntity == null) {
            j.setContent(AjaxJson.CODE_FAIL);
            j.setMsg("请选择修改数据！");
            return j;
        }

        try {
            promotPriceEntityTremp.setUpdateTime(new Date());
            BeanUtils.copyBeanNotNull2Bean(promotPriceEntityTremp, promotPriceEntity);
            promotPriceService.saveOrUpdate(promotPriceEntity);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(AjaxJson.CODE_FAIL);
            j.setMsg("修改失败！");
            return j;
        }
        return j;
    }


}
