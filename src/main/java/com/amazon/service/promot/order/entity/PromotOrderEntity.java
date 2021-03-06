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
public class PromotOrderEntity extends IdEntity implements Serializable {

    /**
     * 卖家主键
     */
    private Integer sellerId;

    /**
     * 普通会员编号
     */
    private Integer authorId;

    /**
     * 评价商品ID
     */
    private String asinId;

    /**
     * 评价商品url
     */
    private String productUrl;

    /**
     * 商品标题
     */
    private String productTitle;

    /**
     * 商品店家
     */
    private String brand;

    /**
     * 搜索关键字
     */
    private String keyword;

    /**
     * 排序
     */
    private Integer sequence;

    /**
     * 商品缩略图
     */
    private String thumbnail;

    /**
     * 商品价格
     */
    private String salePrice;

    /**
     * 状态 1-开启  2-关闭
     */
    private Integer state;

    /**
     * 下单日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date addDate;

    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
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
     * 返现花费
     */
    private BigDecimal cashBackConsumption;

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
     * 获取评论数
     */
    private Integer evaluateNum;

    /**
     * 评价扣款
     */
    private BigDecimal reviewPrice;

    /**
     * 返现扣款
     */
    private BigDecimal cashback;

    /***
     * 备注
     */
    private String remark;

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

    @Column(name = "authorId", nullable = true, length = 20)
    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
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

    @Column(name = "guaranteeFund", nullable = false, precision = 50, scale = 4)
    public BigDecimal getGuaranteeFund() {
        return guaranteeFund;
    }

    public void setGuaranteeFund(BigDecimal guaranteeFund) {
        this.guaranteeFund = guaranteeFund;
    }

    @Column(name = "consumption", nullable = false, precision = 50, scale = 4)
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

    @Column(name = "brand", nullable = true, length = 100)
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Column(name = "thumbnail", nullable = true)
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Column(name = "salePrice", nullable = true, length = 50)
    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    @Column(name = "productTitle", nullable = false)
    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    @Column(name = "reviewPrice", nullable = false, precision = 50, scale = 4)
    public BigDecimal getReviewPrice() {
        return reviewPrice;
    }

    public void setReviewPrice(BigDecimal reviewPrice) {
        this.reviewPrice = reviewPrice;
    }

    @Column(name = "evaluateNum", nullable = false, length = 11)
    public Integer getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(Integer evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    @Column(name = "cashBackConsumption", nullable = false, precision = 50, scale = 4)
    public BigDecimal getCashBackConsumption() {
        return cashBackConsumption;
    }

    public void setCashBackConsumption(BigDecimal cashBackConsumption) {
        this.cashBackConsumption = cashBackConsumption;
    }

    @Column(name = "cashback", nullable = false, precision = 50, scale = 4)
    public BigDecimal getCashback() {
        return cashback;
    }

    public void setCashback(BigDecimal cashback) {
        this.cashback = cashback;
    }

    @Column(name = "remark", nullable = true)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "keyword", nullable = true, length = 100)
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Column(name = "sequence", nullable = true, length = 11)
    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
