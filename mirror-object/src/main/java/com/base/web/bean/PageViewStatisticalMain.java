package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

/**
 * @Author: FQ
 * @Date: Created in 10:38  2018/8/6
 */
public class PageViewStatisticalMain extends GenericPo<Long> {
    //时段
    private Integer timeFrame;
    //排序
    private Integer sort;
    //品牌
    private Long brandId;
    //总销量
    private Long sales;
    //统计时间
    private Date createTime;

    //浏览量
    private Long pageView;

    public Integer getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(Integer timeFrame) {
        this.timeFrame = timeFrame;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
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

    public interface Property extends GenericPo.Property{
        String timeFrame = "timeFrame";
        String sort = "sort";
        String brandId = "brandId";
        String sales = "sales";
        String createTime = "createTime";
        String pageView = "pageView";
    }
}
