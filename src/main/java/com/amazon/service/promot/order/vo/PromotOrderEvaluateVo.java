package com.amazon.service.promot.order.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by User on 2017/7/6.
 */
public class PromotOrderEvaluateVo implements Serializable{

    private Date updateTime;

    private String asin;

    private String amzOrderId;

    private BigDecimal reviewPrice;

    private BigDecimal cashback;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getAmzOrderId() {
        return amzOrderId;
    }

    public void setAmzOrderId(String amzOrderId) {
        this.amzOrderId = amzOrderId;
    }

    public BigDecimal getReviewPrice() {
        return reviewPrice;
    }

    public void setReviewPrice(BigDecimal reviewPrice) {
        this.reviewPrice = reviewPrice;
    }

    public BigDecimal getCashback() {
        return cashback;
    }

    public void setCashback(BigDecimal cashback) {
        this.cashback = cashback;
    }
}
