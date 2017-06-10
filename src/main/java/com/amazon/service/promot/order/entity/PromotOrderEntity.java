package com.amazon.service.promot.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.framework.core.common.entity.IdEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by User on 2017/6/10.
 */
@Entity
@Table(name = "amazon_promot_order_table")
@SuppressWarnings("serial")
public class PromotOrderEntity extends IdEntity implements Serializable {

    /**
     * 卖家主键
     */
    private Integer sellerId;

    /**
     * 评价商品ID
     */
    private String asinId;

    /**
     * 评价商品url
     */
    private String productUrl;

    /**
     * 状态 1-开启  2-关闭
     */
    private Integer state;

    /**
     * 下单日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date addDate;

    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date finishDate;

    /**
     * 保证金
     */
    private BigDecimal guaranteeFund;

    /**
     * 已花费
     */
    private BigDecimal consumption;

    /**
     * 目标好评数
     */
    private Integer needReviewNum;

    /**
     * 每天目标好评数
     */
    private Integer dayReviewNum;

    /**
     * 已联系到的买家数
     */
    private Integer buyerNum;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Column(name = "sellerId", nullable = false, length = 20)
    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    @Column(name = "asinId", nullable = false, length = 50)
    public String getAsinId() {
        return asinId;
    }

    public void setAsinId(String asinId) {
        this.asinId = asinId;
    }

    @Column(name = "productUrl", nullable = false)
    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    @Column(name = "state", nullable = false, length = 11)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name = "addDate", nullable = false, length = 20)
    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    @Column(name = "finishDate", nullable = false, length = 20)
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @Column(name = "guaranteeFund", nullable = false,precision = 50, scale = 4)
    public BigDecimal getGuaranteeFund() {
        return guaranteeFund;
    }

    public void setGuaranteeFund(BigDecimal guaranteeFund) {
        this.guaranteeFund = guaranteeFund;
    }

    @Column(name = "consumption", nullable = false,precision = 50, scale = 4)
    public BigDecimal getConsumption() {
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    @Column(name = "needReviewNum", nullable = false, length = 11)
    public Integer getNeedReviewNum() {
        return needReviewNum;
    }

    public void setNeedReviewNum(Integer needReviewNum) {
        this.needReviewNum = needReviewNum;
    }

    @Column(name = "dayReviewNum", nullable = false, length = 11)
    public Integer getDayReviewNum() {
        return dayReviewNum;
    }

    public void setDayReviewNum(Integer dayReviewNum) {
        this.dayReviewNum = dayReviewNum;
    }

    @Column(name = "buyerNum", nullable = false, length = 11)
    public Integer getBuyerNum() {
        return buyerNum;
    }

    public void setBuyerNum(Integer buyerNum) {
        this.buyerNum = buyerNum;
    }

    @Column(name = "createTime", nullable = true, length = 20)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "updateTime", nullable = true, length = 20)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
