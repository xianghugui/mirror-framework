package com.base.web.bean;

import com.base.web.bean.po.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Geek、
 * @Date: Created in 10:32  2018/3/28、
 * 提现记录表
 */

@ApiModel
public class GetMoney extends GenericPo<Long> {

    //用户id
    private Long userId;

    //提现金额
    @ApiModelProperty(value="提现金额", required = true)
    private BigDecimal money;

    //提现申请时间
    private java.util.Date createTime;

   //提现状态 0:申请提现, 1:提现中 2；提现成功
    private Integer status;

    //银行
    @ApiModelProperty(value="银行", required = true)
    private String bank;

    //手续费
    private BigDecimal fees;
    //提现申请时间
    private java.util.Date expectTime;

    //处理请求ID
    private Long dealUserId;

    //处理时间
    private java.util.Date dealTime;

    public Long getDealUserId() {
        return dealUserId;
    }

    public void setDealUserId(Long dealUserId) {
        this.dealUserId = dealUserId;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public Date getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    public interface Property extends GenericPo.Property{
        String userId = "userId";
        String money = "money";
        String createTime = "createTime";
        String status = "status";
        String bank = "bank";
        String fees = "fees";
        String expectTime = "expectTime";
        String dealUserId = "dealUserId";
        String dealTime = "dealTime";
    }
}
