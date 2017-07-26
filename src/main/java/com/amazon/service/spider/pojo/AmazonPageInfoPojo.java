package com.amazon.service.spider.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/6/14.
 */
public class AmazonPageInfoPojo implements Serializable{

    /**
     * 当前爬虫访问页面的URL
     */
    private String pageUrl;

    /**
     * ASIN
     */
    private String asin;

    /**
     * 产品的标题
     */
    private String productTitle;

    /**
     * 产品的价格
     */
    private String priceblockSaleprice;

    /**
     * 单价 美元
     */
    private String reviewPrice;

    /**
     * 店铺名称
     */
    private String brand;

    /**
     * 商品主图
     */
    private String landingImage;

    /**
     * 结束时间
     */
    @JSONField(format="yyyy-MM-dd")
    private Date finishDate;

    private Integer needReviewNum = 1;

    private Integer dayReviewNum = 1;

    private String remark;

    private Integer sequence = 1;

    private String keyword;

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getPriceblockSaleprice() {
        return priceblockSaleprice;
    }

    public void setPriceblockSaleprice(String priceblockSaleprice) {
        this.priceblockSaleprice = priceblockSaleprice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getReviewPrice() {
        return reviewPrice;
    }

    public void setReviewPrice(String reviewPrice) {
        this.reviewPrice = reviewPrice;
    }

    public String getLandingImage() {
        return landingImage;
    }

    public void setLandingImage(String landingImage) {
        this.landingImage = landingImage;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
