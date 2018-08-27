package com.base.web.bean;

import com.base.web.bean.po.GenericPo;


public class ShopGoodsSpecification extends GenericPo<Long> {
    //商品id
    private Long shopGoodsId;
    //颜色
    private String color;
    //尺寸
    private String size;

    public Long getGoodsId() {
        return shopGoodsId;
    }

    public void setGoodsId(Long shopGoodsId) {
        this.shopGoodsId = shopGoodsId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public interface Property extends GenericPo.Property{
        String shopGoodsId="shopGoodsId";
        String color="color";
        String size="size";

    }
}
