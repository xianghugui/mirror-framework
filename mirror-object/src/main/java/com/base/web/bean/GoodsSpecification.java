package com.base.web.bean;

import com.base.web.bean.po.GenericPo;


public class GoodsSpecification extends GenericPo<Long> {
    //商品id
    private Long goodsId;
    //颜色
    private String color;
    //尺寸
    private String size;
    //数量
    private Long quality;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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

    public Long getQuality() {
        return quality;
    }

    public void setQuality(Long quality) {
        this.quality = quality;
    }
    public interface Property extends GenericPo.Property{
        String goodsId="goodsId";
        String color="color";
        String size="size";
        String quality="quality";
    }
}
