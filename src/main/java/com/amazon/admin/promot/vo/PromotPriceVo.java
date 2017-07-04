package com.amazon.admin.promot.vo;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by User on 2017/7/4.
 */
public class PromotPriceVo implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 月租价格
     */
    private String monthRent;

    /**
     * 评价扣现
     */
    private String reviewPrice;

    /**
     * 美元汇率
     */
    private String exchangeRate;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMonthRent() {
        return monthRent;
    }

    public void setMonthRent(String monthRent) {
        this.monthRent = monthRent;
    }

    public String getReviewPrice() {
        return reviewPrice;
    }

    public void setReviewPrice(String reviewPrice) {
        this.reviewPrice = reviewPrice;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
