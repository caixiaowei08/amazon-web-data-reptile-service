package com.amazon.service.fund.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by User on 2017/6/26.
 */
public class UserFundVo implements Serializable {

    private String account;

    private BigDecimal totalFund;

    private BigDecimal usableFund;

    private BigDecimal freezeFund;

    private BigDecimal exchangeRate;

    private String chargeFund;

    private Integer chargeSource;

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

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getChargeFund() {
        return chargeFund;
    }

    public void setChargeFund(String chargeFund) {
        this.chargeFund = chargeFund;
    }

    public Integer getChargeSource() {
        return chargeSource;
    }

    public void setChargeSource(Integer chargeSource) {
        this.chargeSource = chargeSource;
    }
}
