package com.amazon.author.poi.service.impl;

import com.amazon.admin.constant.Constants;
import com.amazon.author.poi.service.AuthorPoiService;
import com.amazon.service.promot.order.entity.PromotOrderEntity;
import com.amazon.service.promot.order.vo.PromotOrderEvaluateVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.utils.DateUtils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by User on 2017/8/3.
 */
@Service("authorPoiService")
@Transactional
public class AuthorPoiServiceImpl extends BaseServiceImpl implements AuthorPoiService {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public void downLoadPromoteOrderExcel(List<PromotOrderEntity> promotOrderEntityList, HttpServletResponse response, String excelFileName) throws FileNotFoundException, IOException {
        XSSFWorkbook workBook = new XSSFWorkbook();//一个excel文档对象
        XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
        String[] columns = {
                "订单编号", "客户账号", "ASIN", "商品亚马逊链接",
                "商品标题", "商品店家", "亚马逊价格", "订单状态",
                "下单日期", "结束日期", "订单目标好评", "每日目标好评",
                "已下订单", "已获取的好评数", "订单备注", "搜索关键字", "排序页"
        };
        int[] columnsColumnWidth = {
                4000, 4000, 4000, 4500,
                4000, 4000, 4000, 4000,
                4000, 4000, 4000, 4000,
                4000, 4000, 8000, 8000,
                4000
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
        Row row = sheet.createRow(0);
        for (int i = 0; i < columnsColumnWidth.length; i++) {
            sheet.setColumnWidth(i, columnsColumnWidth[i]);
            cell = row.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headStyle);
        }
        //填充数据
        if (CollectionUtils.isNotEmpty(promotOrderEntityList)) {
            for (int i = 0; i < promotOrderEntityList.size(); i++) {
                PromotOrderEntity promotOrderEntity = promotOrderEntityList.get(i);
                row = sheet.createRow(i + 1);
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
                cell.setCellValue(promotOrderEntity.getState().equals(1) ? "开启" : "关闭");
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
                cell = row.createCell(14);
                cell.setCellValue(promotOrderEntity.getRemark());
                cell = row.createCell(15);
                cell.setCellValue(promotOrderEntity.getKeyword());
                cell = row.createCell(16);
                cell.setCellValue(promotOrderEntity.getSequence() == null ? "" : promotOrderEntity.getSequence().toString());
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

    public void downEvaluateExcel(List<PromotOrderEvaluateVo> promotOrderEvaluateVoList, HttpServletResponse response, String excelFileName) throws FileNotFoundException, IOException {
        XSSFWorkbook workBook = new XSSFWorkbook();//一个excel文档对象
        XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
        String[] columns = {
                "推广订单编号", "日期", "ASIN", "亚马逊订单号",
                "刷单单价(USD)", "刷单佣金(USD)", "是否已上评", "备注",
                "微信", "支付宝", "Paypal", "返现日期"
        };
        int[] columnsColumnWidth = {
                4000, 4000, 4000, 8000,
                4500, 4000, 4000, 8000,
                4500, 4000, 4000, 4000
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
        Row row = sheet.createRow(0);
        for (int i = 0; i < columnsColumnWidth.length; i++) {
            sheet.setColumnWidth(i, columnsColumnWidth[i]);
            cell = row.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headStyle);
        }
        //填充数据
        if (CollectionUtils.isNotEmpty(promotOrderEvaluateVoList)) {
            BigDecimal totalCashback = new BigDecimal("0.00");
            BigDecimal totalReviewPrice = new BigDecimal("0.00");
            BigDecimal totalWechat = new BigDecimal("0.00");
            BigDecimal totalZfb = new BigDecimal("0.00");
            BigDecimal totalPaypal = new BigDecimal("0.00");
            for (int i = 0; i < promotOrderEvaluateVoList.size(); i++) {
                PromotOrderEvaluateVo promotOrderEvaluateVo = promotOrderEvaluateVoList.get(i);
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellValue(promotOrderEvaluateVo.getPromotId());
                cell = row.createCell(1);
                cell.setCellValue(simpleDateFormat.format(promotOrderEvaluateVo.getUpdateTime()));
                cell = row.createCell(2);
                cell.setCellValue(promotOrderEvaluateVo.getAsin());
                cell = row.createCell(3);
                cell.setCellValue(promotOrderEvaluateVo.getAmzOrderId().trim());
                cell = row.createCell(4);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(promotOrderEvaluateVo.getCashback() == null ? 0 : Double.parseDouble(promotOrderEvaluateVo.getCashback().toString()));
                cell = row.createCell(5);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(promotOrderEvaluateVo.getReviewPrice() == null ? 0 : Double.parseDouble(promotOrderEvaluateVo.getReviewPrice().toString()));
                cell = row.createCell(6);
                cell.setCellValue(promotOrderEvaluateVo.getIsComment().equals(Constants.EVALUATE_STATE_REVIEW) ? "是" : "否");
                cell = row.createCell(7);
                cell.setCellValue(promotOrderEvaluateVo.getRemark());
                cell = row.createCell(8);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(promotOrderEvaluateVo.getWeChat() == null ? 0.00 : Double.parseDouble(promotOrderEvaluateVo.getWeChat().toString()));
                cell = row.createCell(9);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(promotOrderEvaluateVo.getZfb() == null ? 0.00 : Double.parseDouble(promotOrderEvaluateVo.getZfb().toString()));
                cell.setCellType(CellType.NUMERIC);
                cell = row.createCell(10);
                cell.setCellValue(promotOrderEvaluateVo.getPayPal() == null ? 0.00 : Double.parseDouble(promotOrderEvaluateVo.getPayPal().toString()));
                cell = row.createCell(11);
                cell.setCellValue(promotOrderEvaluateVo.getCashBackDate() == null ? "" : simpleDateFormat.format(promotOrderEvaluateVo.getCashBackDate()));

                totalCashback = totalCashback.add(promotOrderEvaluateVo.getCashback());
                totalReviewPrice = totalReviewPrice.add(promotOrderEvaluateVo.getReviewPrice());
                if (promotOrderEvaluateVo.getWeChat() != null) {
                    totalWechat = totalWechat.add(promotOrderEvaluateVo.getWeChat());
                }
                if (promotOrderEvaluateVo.getZfb() != null) {
                    totalZfb = totalZfb.add(promotOrderEvaluateVo.getZfb());
                }
                if (promotOrderEvaluateVo.getPayPal() != null) {
                    totalPaypal = totalPaypal.add(promotOrderEvaluateVo.getPayPal());
                }
            }
            row = sheet.createRow(promotOrderEvaluateVoList.size() + 1);
            cell = row.createCell(4);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Double.parseDouble(totalCashback.toString()));
            cell = row.createCell(5);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Double.parseDouble(totalReviewPrice.toString()));
            cell = row.createCell(8);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Double.parseDouble(totalWechat.toString()));
            cell = row.createCell(9);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Double.parseDouble(totalZfb.toString()));
            cell = row.createCell(10);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Double.parseDouble(totalPaypal.toString()));
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
