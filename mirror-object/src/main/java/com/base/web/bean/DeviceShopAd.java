package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

/**
 * @Author: Geek、
 * @Date: Created in 11:12  2018/3/27
 */
public class DeviceShopAd extends GenericPo<Long> {

    //设备ID
    private Long deviceId;

    //广告ID
    private Long deviceAdId;

    //设备状态（0正常，1异常）
    private Integer status;

    //广告创建时间
    private Date createTime;

    //设备ID列表
    private Long[] deviceIdList;

    public Long[] getDeviceIdList() {
        return deviceIdList;
    }

    public void setDeviceIdList(Long[] deviceIdList) {
        this.deviceIdList = deviceIdList;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceAdId() {
        return deviceAdId;
    }

    public void setDeviceAdId(Long deviceAdId) {
        this.deviceAdId = deviceAdId;
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

    public interface Property extends GenericPo.Property{
        String deviceId = "deviceId";
        String deviceAdId = "deviceAdId";
        String createTime = "createTime";
        String status = "status";
    }
}
