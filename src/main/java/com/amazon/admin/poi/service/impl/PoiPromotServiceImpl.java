package com.amazon.admin.poi.service.impl;

import com.amazon.admin.poi.service.PoiPromotService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.framework.core.utils.DateUtils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/6/24.
 */
@Service("poiPromotService")
@Transactional
public class PoiPromotServiceImpl implements PoiPromotService {

    public void downPromotOrderExcel(List<PromotOrderEntity> promotOrderEntityList, HttpServletResponse response, String excelFileName) throws FileNotFoundException, IOException {
        XSSFWorkbook workBook = new XSSFWorkbook();//一个excel文档对象
        XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
        String[] columns = {
                "订单编号", "客户账号", "ASIN", "商品亚马逊链接",
                "商品标题", "商品店家", "亚马逊价格", "订单状态",
                "下单日期", "结束日期", "订单目标好评", "每日目标好评",
                "已联系买家数", "已获取的好评数"
        };
        int[] columnsColumnWidth = {
                4000, 4000, 4000, 4500,
                4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000,
                4000, 4000
        };

        CellStyle headStyle = workBook.createCellStyle();
        headStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headStyle.setBorderTop(CellStyle.BORDER_THIN);
        headStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderLeft(CellStyle.BORDER_THIN); // 左边边框
        headStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色
        headStyle.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
        headStyle.setRightBorderColor(IndexedColors.BLACK.getIndex()); // 右边边框颜色
        headStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
        headStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headStyle.setWrapText(true);
        Cell cell = null;
        Row row =  sheet.createRow(0);
        for (int i = 0; i < columnsColumnWidth.length; i++) {
            sheet.setColumnWidth(i, columnsColumnWidth[i]);
            cell = row.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headStyle);
        }
        //填充数据
        if(CollectionUtils.isNotEmpty(promotOrderEntityList)){
            for (int i = 0; i <promotOrderEntityList.size() ; i++) {
                PromotOrderEntity promotOrderEntity = promotOrderEntityList.get(i);
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(promotOrderEntity.getId());
                cell = row.createCell(1);
                cell.setCellValue(promotOrderEntity.getSellerId());
                cell = row.createCell(2);
                cell.setCellValue(promotOrderEntity.getAsinId());
                cell = row.createCell(3);
                cell.setCellValue(promotOrderEntity.getProductUrl());
                cell = row.createCell(4);
                cell.setCellValue(promotOrderEntity.getProductTitle());
                cell = row.createCell(5);
                cell.setCellValue(promotOrderEntity.getBrand());
                cell = row.createCell(6);
                cell.setCellValue(promotOrderEntity.getSalePrice());
                cell = row.createCell(7);
                cell.setCellValue(promotOrderEntity.getState().equals(1)?"开启":"关闭");
                cell = row.createCell(8);
                cell.setCellValue(DateUtils.getDate(promotOrderEntity.getAddDate()));
                cell = row.createCell(9);
                cell.setCellValue(DateUtils.getDate(promotOrderEntity.getFinishDate()));
                cell = row.createCell(10);
                cell.setCellValue(promotOrderEntity.getNeedReviewNum());
                cell = row.createCell(11);
                cell.setCellValue(promotOrderEntity.getDayReviewNum());
                cell = row.createCell(12);
                cell.setCellValue(promotOrderEntity.getBuyerNum());
                cell = row.createCell(13);
                cell.setCellValue(promotOrderEntity.getEvaluateNum());
            }
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            workBook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(excelFileName.getBytes("utf-8"), "iso-8859-1").toString() + ".xlsx");
        } catch (UnsupportedEncodingException e) {

        }
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (Exception e) {

        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {

                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {

                }
            }
        }


    }

}
