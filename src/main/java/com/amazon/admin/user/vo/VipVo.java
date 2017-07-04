package com.amazon.admin.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/7/4.
 */
public class VipVo implements Serializable {

    private Integer id;

    private Integer sellerId;

    private String account;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format="yyyy-MM-dd")
    private Date membershipEndTime;

    private Integer memberShipMonth;


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

    public Date getMembershipEndTime() {
        return membershipEndTime;
    }

    public void setMembershipEndTime(Date membershipEndTime) {
        this.membershipEndTime = membershipEndTime;
    }

    public Integer getMemberShipMonth() {
        return memberShipMonth;
    }

    public void setMemberShipMonth(Integer memberShipMonth) {
        this.memberShipMonth = memberShipMonth;
    }
}
