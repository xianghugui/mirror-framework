package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

/**
 * @Author: Geek、
 * @Date: Created in 11:12  2018/3/27
 */
public class ShopAd extends GenericPo<Long> {

    //店铺ID
    private Long shopId;

    //广告ID
    private Long adId;
    

    //广告创建时间
    private Date createTime;
    //1 店铺广告 2 平台广告
    private Integer type;
    // 扩展字段 区域id列表
    private String areaList;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAreaList() {
        return areaList;
    }

    public void setAreaList(String areaList) {
        this.areaList = areaList;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public interface Property extends GenericPo.Property{
        String shopId = "shopId";
        String adId = "adId";
        String createTime = "createTime";
        String type ="type";
    }
}
