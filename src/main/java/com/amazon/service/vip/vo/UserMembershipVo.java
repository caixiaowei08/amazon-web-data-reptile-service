package com.amazon.service.vip.vo;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by User on 2017/6/27.
 */
public class UserMembershipVo implements Serializable{

    private String account;

    private BigDecimal totalFund;

    private BigDecimal usableFund;

    private BigDecimal freezeFund;

    private Boolean vip = false;

    private Boolean beforeVip = false;

    private BigDecimal memberShipMonthCost;

    private Integer memberShipMonth;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format="yyyy-MM-dd")
    private Date membershipEndTime;

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

    public BigDecimal getMemberShipMonthCost() {
        return memberShipMonthCost;
    }

    public void setMemberShipMonthCost(BigDecimal memberShipMonthCost) {
        this.memberShipMonthCost = memberShipMonthCost;
    }

    public Integer getMemberShipMonth() {
        return memberShipMonth;
    }

    public void setMemberShipMonth(Integer memberShipMonth) {
        this.memberShipMonth = memberShipMonth;
    }
}
