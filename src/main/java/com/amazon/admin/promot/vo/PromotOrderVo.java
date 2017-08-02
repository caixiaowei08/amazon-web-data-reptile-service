package com.amazon.admin.promot.vo;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by User on 2017/8/2.
 */
public class PromotOrderVo implements Serializable {

    private Integer id;
    /**
     * 卖家主键
     */
    private Integer sellerId;

    /**
     * 普通会员编号
     */
    private Integer authorId;

    /**
     *
     */
    private String authorAccount;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorAccount() {
        return authorAccount;
    }

    public void setAuthorAccount(String authorAccount) {
        this.authorAccount = authorAccount;
    }

    public String getAsinId() {
        return asinId;
    }

    public void setAsinId(String asinId) {
        this.asinId = asinId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public BigDecimal getGuaranteeFund() {
        return guaranteeFund;
    }

    public void setGuaranteeFund(BigDecimal guaranteeFund) {
        this.guaranteeFund = guaranteeFund;
    }

    public BigDecimal getConsumption() {
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public BigDecimal getCashBackConsumption() {
        return cashBackConsumption;
    }

    public void setCashBackConsumption(BigDecimal cashBackConsumption) {
        this.cashBackConsumption = cashBackConsumption;
    }

    public Integer getNeedReviewNum() {
        return needReviewNum;
    }

    public void setNeedReviewNum(Integer needReviewNum) {
        this.needReviewNum = needReviewNum;
    }

    public Integer getDayReviewNum() {
        return dayReviewNum;
    }

    public void setDayReviewNum(Integer dayReviewNum) {
        this.dayReviewNum = dayReviewNum;
    }

    public Integer getBuyerNum() {
        return buyerNum;
    }

    public void setBuyerNum(Integer buyerNum) {
        this.buyerNum = buyerNum;
    }

    public Integer getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(Integer evaluateNum) {
        this.evaluateNum = evaluateNum;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
