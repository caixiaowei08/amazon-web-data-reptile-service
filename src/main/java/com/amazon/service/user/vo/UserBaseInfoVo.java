package com.amazon.service.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *登录时 主页显示数据
 * Created by User on 2017/6/20.
 */
public class UserBaseInfoVo implements Serializable{

    private String account;

    private BigDecimal totalFund;

    private BigDecimal usableFund;

    private BigDecimal freezeFund;

    private Boolean vip;

    private Boolean beforeVip;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format="yyyy-MM-dd")
    private Date membershipEndTime;

    /**
     * 该账户下活动状态的推广订单个数
     */
    private Integer activeOrderNum;
    /**
     * 今日获得评论个数
     */
    private Integer todayEvaluateNum;
    /**
     *所有活动计划订单数
     */
    private Integer planBuyerNum;
    /**
     * 所有活动已下订单
     */
    private Integer buyerNum;
    /**
     * 所有的好评数
     */
    private Integer totalEvaluateNum;
    /**
     * 已经完成的和关闭的订单数
     */
    private Integer historyOrderNum;
    /**
     * 所有的评价消费总额
     */
    private BigDecimal totalConsumption;

    public Integer getActiveOrderNum() {
        return activeOrderNum;
    }

    public void setActiveOrderNum(Integer activeOrderNum) {
        this.activeOrderNum = activeOrderNum;
    }

    public Integer getTodayEvaluateNum() {
        return todayEvaluateNum;
    }

    public void setTodayEvaluateNum(Integer todayEvaluateNum) {
        this.todayEvaluateNum = todayEvaluateNum;
    }

    public Integer getBuyerNum() {
        return buyerNum;
    }

    public void setBuyerNum(Integer buyerNum) {
        this.buyerNum = buyerNum;
    }

    public Integer getTotalEvaluateNum() {
        return totalEvaluateNum;
    }

    public void setTotalEvaluateNum(Integer totalEvaluateNum) {
        this.totalEvaluateNum = totalEvaluateNum;
    }

    public Integer getHistoryOrderNum() {
        return historyOrderNum;
    }

    public void setHistoryOrderNum(Integer historyOrderNum) {
        this.historyOrderNum = historyOrderNum;
    }

    public BigDecimal getTotalConsumption() {
        return totalConsumption;
    }

    public void setTotalConsumption(BigDecimal totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getTotalFund() {
        return totalFund;
    }

    public void setTotalFund(BigDecimal totalFund) {
        this.totalFund = totalFund;
    }

    public BigDecimal getUsableFund() {
        return usableFund;
    }

    public void setUsableFund(BigDecimal usableFund) {
        this.usableFund = usableFund;
    }

    public BigDecimal getFreezeFund() {
        return freezeFund;
    }

    public void setFreezeFund(BigDecimal freezeFund) {
        this.freezeFund = freezeFund;
    }

    public Boolean getVip() {
        return vip;
    }

    public void setVip(Boolean vip) {
        this.vip = vip;
    }

    public Boolean getBeforeVip() {
        return beforeVip;
    }

    public void setBeforeVip(Boolean beforeVip) {
        this.beforeVip = beforeVip;
    }

    public Date getMembershipEndTime() {
        return membershipEndTime;
    }

    public void setMembershipEndTime(Date membershipEndTime) {
        this.membershipEndTime = membershipEndTime;
    }

    public Integer getPlanBuyerNum() {
        return planBuyerNum;
    }

    public void setPlanBuyerNum(Integer planBuyerNum) {
        this.planBuyerNum = planBuyerNum;
    }
}
