package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 13:52  2018/4/10
 */
public class OrderDeal extends GenericPo<Long> {
    //订单id
    private Long orderId;

    //创建者Id
    private Long creatorId;

    //最后处理时间
    private Date dealTime;

    //最后处理者ID
    private Long dealUserId;

    //创建日期
    private Date createTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public Long getDealUserId() {
        return dealUserId;
    }

    public void setDealUserId(Long dealUserId) {
        this.dealUserId = dealUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public interface Property extends GenericPo.Property{
        String orderId = "orderId";
        String creatorId = "creatorId";
        String dealTime = "dealTime";
        String dealUserId = "dealUserId";
        String createTime = "createTime";
    }
}
