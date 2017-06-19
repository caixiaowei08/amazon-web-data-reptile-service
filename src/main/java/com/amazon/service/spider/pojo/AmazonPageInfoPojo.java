package com.amazon.service.spider.pojo;

import java.io.Serializable;

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
}
