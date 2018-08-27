package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

public class TryTimeRecord extends GenericPo<Long> {

    //单次试衣价格
    private final static Double UNITPRICE = 0.01;

    public static Double getUNITPRICE() {
        return UNITPRICE;
    }

    //用户ID
    private Long userId;

    //充值试衣次数
    private Integer tryTimes;

    //充值时间
    private Date createTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(Integer tryTimes) {
        this.tryTimes = tryTimes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public interface Property extends GenericPo.Property{
        String userId="userId";
        String tryTimes="tryTimes";
        String createTime="createTime";
    }
}
