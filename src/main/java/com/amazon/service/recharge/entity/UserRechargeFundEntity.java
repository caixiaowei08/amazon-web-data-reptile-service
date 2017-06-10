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
     * 充值交易号
     */
    private String dealNo;

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
     * 充值状态  0-待核实 1-成功  2-失败
     */
    private Integer state;

    /**
     * 交易返回码
     */
    private String  returnCode;

    /**
     * 充值类型 1- 会员月租 2-充值保证金
     */
    private Integer chargeType;

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

    @Column(name = "dealNo", nullable = true, length = 100)
    public String getDealNo() {
        return dealNo;
    }

    public void setDealNo(String dealNo) {
        this.dealNo = dealNo;
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

    @Column(name = "returnCode", nullable = true)
    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
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
}
