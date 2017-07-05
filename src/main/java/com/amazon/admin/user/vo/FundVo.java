package com.amazon.admin.user.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by User on 2017/7/4.
 */
public class FundVo implements Serializable {

    private Integer id;

    private Integer sellerId;

    private String account;

    private BigDecimal totalFund;

    private BigDecimal usableFund;

    private BigDecimal freezeFund;

    private String chargeFund;

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

    public String getChargeFund() {
        return chargeFund;
    }

    public void setChargeFund(String chargeFund) {
        this.chargeFund = chargeFund;
    }
}
