package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.math.BigDecimal;
import java.util.Date;

public class OrderProfit extends GenericPo<Long> {

    //父订单id
    private Long parentId;
    //子订单id
    private Long childId;
    //用户id
    private Long userId;
    //商品id(询价退款没有商品id，提现没有商品id)
    private Long goodsId;
    //金额
    private BigDecimal price;
    //分润时间
    private Date createTime;
    //0: 付款，1：退款，2：收益，3：返现，4：提现
    private Integer type;


    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public interface Property extends GenericPo.Property {
        String parentId = "parentId";
        String childId = "childId";
        String userId = "userId";
        String goodsId = "goodsId";
        String price = "price";
        String createTime = "createTime";
        String type = "type";
    }
}
