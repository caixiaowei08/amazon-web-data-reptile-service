package com.amazon.service.promot.order.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by User on 2017/7/6.
 */
public class PromotOrderEvaluateVo implements Serializable{

    private Integer promotId;

    private Date updateTime;

    private String asin;

    private String amzOrderId;

    private BigDecimal reviewPrice;

    private BigDecimal cashback;

    private Integer isComment;

    private String remark;

    private BigDecimal weChat;

    private BigDecimal zfb;

    private BigDecimal payPal;

    private Date cashBackDate;

    private String accountName;

    public Integer getPromotId() {
        return promotId;
    }

    public void setPromotId(Integer promotId) {
        this.promotId = promotId;
    }

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

    public Integer getIsComment() {
        return isComment;
    }

    public void setIsComment(Integer isComment) {
        this.isComment = isComment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public BigDecimal getWeChat() {
        return weChat;
    }

    public void setWeChat(BigDecimal weChat) {
        this.weChat = weChat;
    }

    public BigDecimal getZfb() {
        return zfb;
    }

    public void setZfb(BigDecimal zfb) {
        this.zfb = zfb;
    }

    public BigDecimal getPayPal() {
        return payPal;
    }

    public void setPayPal(BigDecimal payPal) {
        this.payPal = payPal;
    }

    public Date getCashBackDate() {
        return cashBackDate;
    }

    public void setCashBackDate(Date cashBackDate) {
        this.cashBackDate = cashBackDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
