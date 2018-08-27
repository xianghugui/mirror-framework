package com.base.web.bean;

import com.base.web.bean.po.GenericPo;


/**
 * @Author: Geek、
 * @Date: Created in 11:46  2018/3/27
 */
public class ShopBrand extends GenericPo<Long> {

    //店铺ID
    private Long shopId;

    //品牌关联ID
    private Integer brandId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public interface Property extends GenericPo.Property{
        String shopId = "shopId";
        String brandId = "brandId";
    }
}
