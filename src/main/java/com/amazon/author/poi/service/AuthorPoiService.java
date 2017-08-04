package com.amazon.author.poi.service;

import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.vo.PromotOrderEvaluateVo;
import org.framework.core.common.service.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by User on 2017/8/3.
 */
public interface AuthorPoiService extends BaseService {

    //下载订单Excel文档
    public void downLoadPromoteOrderExcel(List<PromotOrderEntity> promotOrderEntityList, HttpServletResponse response, String excelFileName) throws FileNotFoundException,IOException;

    public void downEvaluateExcel(List<PromotOrderEvaluateVo> promotOrderEvaluateVoList, HttpServletResponse response, String excelFileName) throws FileNotFoundException,IOException;

}
