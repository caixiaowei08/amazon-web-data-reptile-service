package com.amazon.service.promot.flow.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.framework.core.common.entity.IdEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/6/10.
 */
@Entity
@Table(name = "amazon_promot_order_evaluate_flow_table")
@SuppressWarnings("serial")
public class PromotOrderEvaluateFlowEntity extends IdEntity implements Serializable {

    /**
     * 推广单号
     */
    private Integer promotId;

    /**
     * 账户id
     */
    private Integer sellerId;

    /**
     *评价位置识别码 防止评价重复录入
     */
    private String reviewCode;

    /**
     * 评价商品ID
     */
    private String asinId;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 买家ID号
     */
    private Integer buyerId;
    /**
     * 亚马逊订单号
     */
    private String amzOrderId;
    /**
     *评价链接
     */
    private String reviewUrl;
    /**
     * 评价内容
     */
    private String reviewContent;
    /**
     * 评级星级
     */
    private Double reviewStar;
    /**
     * 评价日期
     */
    private String reviewDate;
    /**
     * 投诉 0-无投诉 1 2 3 4 5
     */
    private Integer complaint;

    private String remark;
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


    @Column(name = "promotId", nullable = true, length = 20)
    public Integer getPromotId() {
        return promotId;
    }

    public void setPromotId(Integer promotId) {
        this.promotId = promotId;
    }

    @Column(name = "sellerId", nullable = false, length = 20)
    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    @Column(name = "asinId", nullable = true, length = 50)
    public String getAsinId() {
        return asinId;
    }

    public void setAsinId(String asinId) {
        this.asinId = asinId;
    }

    @Column(name = "buyerId", nullable = true, length = 20)
    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    @Column(name = "amzOrderId", nullable = true, length = 100)
    public String getAmzOrderId() {
        return amzOrderId;
    }

    @Column(name = "state", nullable = true, length = 11)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setAmzOrderId(String amzOrderId) {
        this.amzOrderId = amzOrderId;
    }

    @Column(name = "reviewUrl", nullable = true)
    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    @Column(name = "reviewContent", nullable = true)
    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }



    @Column(name = "reviewStar", nullable = true)
    public Double getReviewStar() {
        return reviewStar;
    }

    public void setReviewStar(Double reviewStar) {
        this.reviewStar = reviewStar;
    }

    @Column(name = "reviewDate", nullable = true, length = 50)
    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    @Column(name = "complaint", nullable = true, length = 11)
    public Integer getComplaint() {
        return complaint;
    }

    public void setComplaint(Integer complaint) {
        this.complaint = complaint;
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

    @Column(name = "reviewCode", nullable = true, length = 100)
    public String getReviewCode() {
        return reviewCode;
    }

    public void setReviewCode(String reviewCode) {
        this.reviewCode = reviewCode;
    }

    @Column(name = "remark", nullable = true)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
