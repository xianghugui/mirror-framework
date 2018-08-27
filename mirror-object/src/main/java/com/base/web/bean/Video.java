package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.sql.Blob;
import java.util.Date;

public class Video extends GenericPo<Long> {
    //资源关联id
    private  Long recordId;
    //资源设备id
    private  Long deviceId;
    //资源设备店铺id
    private  Long deviceShopId;

    // 视频状态（0正常，1异常）
    private Integer status;

    //创建时间
    private Date createTime;

    //视频名称
    private String videoName;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceShopId() {
        return deviceShopId;
    }

    public void setDeviceShopId(Long deviceShopId) {
        this.deviceShopId = deviceShopId;
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

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public interface Property extends GenericPo.Property {
        //品牌id
        String recordId="recordId";
        String deviceId="deviceId";
        String deviceShopId="deviceShopId";
        String status="status";
        String createTime="createTime";
        String videoName="videoName";
    }
}
