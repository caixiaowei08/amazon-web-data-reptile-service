package com.amazon.buyer.main.controller;

import com.amazon.buyer.main.vo.PromotOrderVo;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.service.PromotOrderService;
import com.amazon.system.Constant;
import com.amazon.system.system.bootstrap.hibernate.CriteriaQuery;
import com.amazon.system.system.bootstrap.json.DataGrid;
import com.amazon.system.system.bootstrap.json.DataGridReturn;
import com.amazon.system.system.bootstrap.utils.DatagridJsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.framework.core.common.controller.BaseController;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.DateUtils.DateUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/7/9.
 */
@Scope("prototype")
@Controller
@RequestMapping("/mainBuyerController")
public class MainBuyerController extends BaseController {

    private static Logger logger = LogManager.getLogger(MainBuyerController.class.getName());

    @Autowired
    private GlobalService globalService;

    @Autowired
    private PromotOrderService promotOrderService;


    @RequestMapping(params = "dataGrid")
    public void dataGrid(DataGrid dataGrid, HttpServletRequest request, HttpServletResponse response) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(PromotOrderEntity.class, dataGrid, request.getParameterMap());
        try {
            criteriaQuery.installHqlParams();
        } catch (Exception e) {
            logger.info("组装查询出错！", e.fillInStackTrace());
        }
        criteriaQuery.getDetachedCriteria().add(Restrictions.eq("state", Constant.STATE_Y));
        DataGridReturn dataGridReturn = promotOrderService.getDataGridReturn(criteriaQuery);
        DatagridJsonUtils.listToObj(dataGridReturn, PromotOrderEntity.class, dataGrid.getField());
        List<PromotOrderEntity> promotOrderEntityList = dataGridReturn.getRows();
        List<PromotOrderVo> promotOrderVoList = new ArrayList<PromotOrderVo>();
        if (CollectionUtils.isNotEmpty(promotOrderEntityList)) {
            for (PromotOrderEntity promotOrderEntity : promotOrderEntityList) {
                PromotOrderVo promotOrderVo = new PromotOrderVo();
                promotOrderVo.setAsinId(promotOrderEntity.getAsinId());
                promotOrderVo.setThumbnail(promotOrderEntity.getThumbnail());
                promotOrderVo.setProductTitle(promotOrderEntity.getProductTitle());
                promotOrderVo.setSalePrice(promotOrderEntity.getSalePrice());
                promotOrderVo.setBrand(promotOrderEntity.getBrand() == null ? "" : promotOrderEntity.getBrand());
                promotOrderVo.setProductUrl(promotOrderEntity.getProductUrl());
                try {
                    int day = DateUtils.daysBetween(new Date(), promotOrderEntity.getAddDate());
                    int beforeGoodsNum = day * promotOrderEntity.getDayReviewNum();
                    int todayGoodsNum = (day + 1) * promotOrderEntity.getDayReviewNum();
                    if (todayGoodsNum >= promotOrderEntity.getNeedReviewNum() && promotOrderEntity.getNeedReviewNum() > beforeGoodsNum) {
                        promotOrderVo.setStockNum(promotOrderEntity.getNeedReviewNum() - beforeGoodsNum);
                    } else if (todayGoodsNum < promotOrderEntity.getNeedReviewNum()){
                        promotOrderVo.setStockNum(todayGoodsNum - beforeGoodsNum);
                    } else if(promotOrderEntity.getNeedReviewNum() <= beforeGoodsNum){
                        promotOrderVo.setStockNum(0);
                    }
                } catch (ParseException e) {
                    logger.error(e.fillInStackTrace());
                }
                promotOrderVoList.add(promotOrderVo);
            }
        }
        dataGridReturn.setRows(promotOrderVoList);
        DatagridJsonUtils.datagrid(response, dataGridReturn);
    }


}
