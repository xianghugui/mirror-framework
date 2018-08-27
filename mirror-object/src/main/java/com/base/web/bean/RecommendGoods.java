package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

/**
 * 推荐商品
 */
public class RecommendGoods extends GenericPo<Long> {

    //推荐商品类别id
    private Integer goodsClassId;
    //推荐时间
    private java.util.Date recommendTime;
    //推荐商品id
    private Long goodsId;
    //推荐商品状态（0，推荐，1取消推荐）
    private Integer status;

    public Integer getGoodsClassId() {
        return goodsClassId;
    }

    public void setGoodsClassId(Integer goodsClassId) {
        this.goodsClassId = goodsClassId;
    }

    public Date getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(Date recommendTime) {
        this.recommendTime = recommendTime;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public interface Property extends GenericPo.Property {
        String goodsClassId = "goodsClassId";
        String recommendTime = "recommendTime";
        String goodsId = "goodsId";
        String status = "status";
    }
}
