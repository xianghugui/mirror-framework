package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

public class StatisticalVice extends GenericPo<Long> {
    private Long shopId;
    private Long sales;
    //关联id
    private Long viceId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getSales() {
        return sales;
    }

    public void setSales(Long sales) {
        this.sales = sales;
    }

    public Long getViceId() {
        return viceId;
    }

    public void setViceId(Long viceId) {
        this.viceId = viceId;
    }

    public interface Property extends GenericPo.Property{
        String shopId = "shopId";
        String sales = "sales";
        String viceId = "viceId";
    }
}
