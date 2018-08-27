package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

public class PageViewStatisticalVice extends GenericPo<Long> {
    private Long shopId;
    private Long sales;
    //关联id
    private Long viceId;

    //浏览量
    private Long pageView;

    public Long getPageView() {
        return pageView;
    }

    public void setPageView(Long pageView) {
        this.pageView = pageView;
    }

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
        String pageView = "pageView";
    }
}
