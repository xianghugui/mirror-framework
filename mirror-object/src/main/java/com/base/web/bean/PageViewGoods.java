package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

/**
 * @Author: FQ
 * @Date: Created in 10:38  2018/8/6
 */
public class PageViewGoods extends GenericPo<Long> {
    //时段
    private Integer timeFrame;
    //商品ID
    private Long goodsId;
    //总销量
    private Long sales;
    //统计时间
    private Date createTime;
    //浏览量
    private Long pageView;
    //店铺ID
    private Long shopId;

    public Integer getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(Integer timeFrame) {
        this.timeFrame = timeFrame;
    }

    public Long getBrandId() {
        return goodsId;
    }

    public void setBrandId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getSales() {
        return sales;
    }

    public void setSales(Long sales) {
        this.sales = sales;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getPageView() {
        return pageView;
    }

    public void setPageView(Long pageView) {
        this.pageView = pageView;
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
        String timeFrame = "timeFrame";
        String goodsId = "goodsId";
        String sales = "sales";
        String createTime = "createTime";
        String pageView = "pageView";
        String shopId = "shopId";
    }
}
