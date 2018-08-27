package com.base.web.bean;

import com.base.web.bean.po.GenericPo;


/**
 * @Author: Geek、
 * @Date: Created in 11:58  2018/3/27
 */
public class GoodsShop extends GenericPo<Long> {

    //商品ID
    private Long goodsId;

    //店铺ID
    private Long shopId;
   //状态（0在卖，1下线）
    private  Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public interface Property extends GenericPo.Property{
        String goodsId = "goodsId";
        String shopId = "shopId";
        String status = "status";
    }
}
