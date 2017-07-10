package com.amazon.buyer.main.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/7/10.
 */
public class PromotOrderVo implements Serializable{
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
     *商品标题
     */
    private String productTitle;

    /**
     *商品店家
     */
    private String brand;

    /**
     * 商品缩略图
     */
    private String thumbnail;

    /**
     *商品价格
     */
    private String salePrice;

    /**
     * 目标好评数
     */
    private Integer stockNum;

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
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

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }
}
