package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

/**
 * @Author: Geek、
 * @Date: Created in 11:30  2018/3/27
 */
public class ShopDevice extends GenericPo<Long> {

    //店铺ID
    private Long shopId;

    //设备ID
    private Long deviceId;

    //设备状态（0：正常，1：不正常）
    private Integer status;

    //店铺设备关联时间
    private Date createTime;

    //设备所属区域ID
    private Integer areaId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public interface Property extends GenericPo.Property{
        String shopId = "shopId";
        String deviceId = "deviceId";
        String status = "status";
        String createTime = "createTime";
        String areaId = "areaId";
    }
}
