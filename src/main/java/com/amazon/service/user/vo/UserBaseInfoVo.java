package com.amazon.service.user.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *登录时 主页显示数据
 * Created by User on 2017/6/20.
 */
public class UserBaseInfoVo implements Serializable{

    /**
     * 该账户下活动状态的推广订单个数
     */
    private Integer activeOrderNum;
    /**
     * 今日获得评论个数
     */
    private Integer todayEvaluateNum;
    /**
     * 开启活动的所有联系的买家数
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
}
