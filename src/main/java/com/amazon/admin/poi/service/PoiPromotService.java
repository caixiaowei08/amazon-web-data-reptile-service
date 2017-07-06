package com.amazon.admin.poi.service;

import com.amazon.service.promot.flow.entity.PromotOrderEvaluateFlowEntity;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.vo.PromotOrderEvaluateVo;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by User on 2017/6/24.
 */
public interface PoiPromotService {

    public void downPromotOrderExcel(List<PromotOrderEntity> promotOrderEntityList, HttpServletResponse response, String excelFileName) throws FileNotFoundException,IOException;

    public void downEvaluateExcel(List<PromotOrderEvaluateVo> promotOrderEvaluateVoList, HttpServletResponse response, String excelFileName) throws FileNotFoundException,IOException;

}
