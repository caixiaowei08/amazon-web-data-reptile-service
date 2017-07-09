package com.amazon.buyer.account.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.framework.core.common.entity.IdEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/7/8.
 */
@Entity
@Table(name = "amazon_buyer_user_table")
public class BuyerUserEntity extends IdEntity implements Serializable {
    /**
     * '账号'
     */
    private String account;
    /**
     * '密码'
     */
    private String pwd;
    /**
     * 微信账号
     */
    private String weChatAccount;
    /**
     * 'Paypal账号'
     */
    private String paypalAccount;
    /**
     * '支付宝账号'
     */
    private String zfbAccount;
    /**
     * '默认支付账号'
     */
    private Integer defaultPaymentAccount;
    /**
     * '校验码'
     */
    private String checkCode;
    /**
     * 校验码有效时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date checkCodeCheckTime;
    /**
     *状态 1-有效 2- 禁用
     */
    private Integer state;
    /**
     * 最近登录时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date loginTime;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Column(name = "account", nullable = false, length = 100)
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Column(name = "pwd", nullable = false, length = 50)
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Column(name = "weChatAccount", nullable = true, length = 100)
    public String getWeChatAccount() {
        return weChatAccount;
    }

    public void setWeChatAccount(String weChatAccount) {
        this.weChatAccount = weChatAccount;
    }

    @Column(name = "paypalAccount", nullable = true, length = 100)
    public String getPaypalAccount() {
        return paypalAccount;
    }

    public void setPaypalAccount(String paypalAccount) {
        this.paypalAccount = paypalAccount;
    }

    @Column(name = "zfbAccount", nullable = true, length = 100)
    public String getZfbAccount() {
        return zfbAccount;
    }

    public void setZfbAccount(String zfbAccount) {
        this.zfbAccount = zfbAccount;
    }

    @Column(name = "defaultPaymentAccount", nullable = true, length = 100)
    public Integer getDefaultPaymentAccount() {
        return defaultPaymentAccount;
    }

    public void setDefaultPaymentAccount(Integer defaultPaymentAccount) {
        this.defaultPaymentAccount = defaultPaymentAccount;
    }

    @Column(name = "checkCode", nullable = true, length = 100)
    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Column(name ="checkCodeCheckTime",nullable=true,length=20)
    public Date getCheckCodeCheckTime() {
        return checkCodeCheckTime;
    }

    public void setCheckCodeCheckTime(Date checkCodeCheckTime) {
        this.checkCodeCheckTime = checkCodeCheckTime;
    }

    @Column(name = "state", nullable = false, length = 11)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name ="loginTime",nullable=true,length=20)
    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
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
