package com.amazon.service.vip.entity;

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
@Table(name = "amazon_user_membership_table")
public class UserMembershipEntity extends IdEntity implements Serializable {

    /**
     * 账户主键
     */
    private Integer sellerId;

    /**
     * 会员开始日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date membershipStartTime;

    /**
     * 会员到期日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date membershipEndTime;

    /**
     * 历史会员费用总额
     */
    private BigDecimal totalMembershipCost;

    /**
     * 最近会员充值时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date chargeTime;

    /**
     * 最近会员金额
     */
    private BigDecimal chargeFund;

    /**
     * 最近会员到期日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date lastMembershipEndTime;

    @Column(name = "sellerId", nullable = false, length = 20)
    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }
    @Column(name = "membershipStartTime", nullable = true, length = 20)
    public Date getMembershipStartTime() {
        return membershipStartTime;
    }

    public void setMembershipStartTime(Date membershipStartTime) {
        this.membershipStartTime = membershipStartTime;
    }

    @Column(name = "membershipEndTime", nullable = true, length = 20)
    public Date getMembershipEndTime() {
        return membershipEndTime;
    }

    public void setMembershipEndTime(Date membershipEndTime) {
        this.membershipEndTime = membershipEndTime;
    }

    @Column(name = "totalMembershipCost", nullable = false,precision = 50, scale = 4)
    public BigDecimal getTotalMembershipCost() {
        return totalMembershipCost;
    }

    public void setTotalMembershipCost(BigDecimal totalMembershipCost) {
        this.totalMembershipCost = totalMembershipCost;
    }

    @Column(name = "chargeTime", nullable = true, length = 20)
    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }

    @Column(name = "chargeFund", nullable = true,precision = 50, scale = 4)
    public BigDecimal getChargeFund() {
        return chargeFund;
    }

    public void setChargeFund(BigDecimal chargeFund) {
        this.chargeFund = chargeFund;
    }

    @Column(name = "lastMembershipEndTime", nullable = true, length = 20)
    public Date getLastMembershipEndTime() {
        return lastMembershipEndTime;
    }

    public void setLastMembershipEndTime(Date lastMembershipEndTime) {
        this.lastMembershipEndTime = lastMembershipEndTime;
    }
}
