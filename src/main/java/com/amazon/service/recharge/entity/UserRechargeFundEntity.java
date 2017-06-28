package com.amazon.service.recharge.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.framework.core.common.entity.IdEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by User on 2017/6/10.
 */
@Entity
@Table(name = "amazon_user_recharge_fund_pipeline_record_table")
public class UserRechargeFundEntity extends IdEntity implements Serializable {

    /**
     * 卖家ID
     */
    private Integer sellerId;

    /**
     * 平台充值流水
     */
    private String platformOrderNum;

    /**
     * 支付宝充值交易流水
     */
    private String zfbOrderNum;

    /**
     * 充值来源 1- 支付宝 2-微信
     */
    private Integer chargeSource;

    /**
     * 充值账号
     */
    private String chargeAccount;

    /**
     * 充值金额
     */
    private BigDecimal chargeFund;

    /**
     *
     */
    private Integer memberShipMonth;
    /**
     * 充值状态  1-待核实 2-成功  3-失败
     */
    private Integer state;

    /**
     *支付宝回调信息
     */
    private String notifyInfo;

    /**
     * 充值类型 1- 会员月租 2-充值保证金
     */
    private Integer chargeType;

    /**
     * 支付人民币
     */
    private BigDecimal chargeFundRmb;

    /**
     * 支付汇率
     */
    private BigDecimal  exchangeRate;

    /**
     * 支付发起时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     *支付确认时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;

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

    @Column(name = "sellerId", nullable = false, length = 20)
    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }


    @Column(name = "chargeSource", nullable = true, length = 11)
    public Integer getChargeSource() {
        return chargeSource;
    }

    public void setChargeSource(Integer chargeSource) {
        this.chargeSource = chargeSource;
    }

    @Column(name = "chargeAccount", nullable = true, length = 100)
    public String getChargeAccount() {
        return chargeAccount;
    }

    public void setChargeAccount(String chargeAccount) {
        this.chargeAccount = chargeAccount;
    }

    @Column(name = "chargeFund", nullable = false,precision = 50, scale = 4)
    public BigDecimal getChargeFund() {
        return chargeFund;
    }

    public void setChargeFund(BigDecimal chargeFund) {
        this.chargeFund = chargeFund;
    }

    @Column(name = "state", nullable = true, length = 11)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name = "chargeType", nullable = true, length = 11)
    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    @Column(name ="createTime",nullable=true,length=20)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name ="updateTime",nullable=true,length=20)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name ="platformOrderNum",nullable=true,length = 100)
    public String getPlatformOrderNum() {
        return platformOrderNum;
    }

    public void setPlatformOrderNum(String platformOrderNum) {
        this.platformOrderNum = platformOrderNum;
    }


    @Column(name ="notifyInfo",nullable=true)
    public String getNotifyInfo() {
        return notifyInfo;
    }

    public void setNotifyInfo(String notifyInfo) {
        this.notifyInfo = notifyInfo;
    }

    @Column(name ="startTime",nullable=true,length=20)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name ="confirmTime",nullable=true,length=20)
    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    @Column(name ="zfbOrderNum",nullable=true,length = 100)
    public String getZfbOrderNum() {
        return zfbOrderNum;
    }

    public void setZfbOrderNum(String zfbOrderNum) {
        this.zfbOrderNum = zfbOrderNum;
    }

    @Column(name = "chargeFundRmb", nullable = false,precision = 50, scale = 4)
    public BigDecimal getChargeFundRmb() {
        return chargeFundRmb;
    }

    public void setChargeFundRmb(BigDecimal chargeFundRmb) {
        this.chargeFundRmb = chargeFundRmb;
    }

    @Column(name = "exchangeRate", nullable = false,precision = 50, scale = 4)
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Column(name ="memberShipMonth",nullable=true,length=11)
    public Integer getMemberShipMonth() {
        return memberShipMonth;
    }

    public void setMemberShipMonth(Integer memberShipMonth) {
        this.memberShipMonth = memberShipMonth;
    }
}
