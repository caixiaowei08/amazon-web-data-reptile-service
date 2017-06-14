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
     * 产品的标题
     */
    private String productTitle;

    /**
     * 产品的价格
     */
    private String priceblock_saleprice;

    /**
     * 店铺名称
     */
    private String brand;

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

    public String getPriceblock_saleprice() {
        return priceblock_saleprice;
    }

    public void setPriceblock_saleprice(String priceblock_saleprice) {
        this.priceblock_saleprice = priceblock_saleprice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
