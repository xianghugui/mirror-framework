package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

/**
 * @Author: Geek、
 * @Date: Created in 11:12  2018/3/27
 */
public class Shop extends GenericPo<Long> {

    //店铺名称
    private String name;

    //店铺坐标
    private String longtitude;

    //店铺坐标
    private String latitude;

    //区域ID
    private Integer areaId;

    //店铺用户ID
    private Long userId;

    //店铺状态（0：待营业，1：在营，2：歇业，3：假删除）
    private Integer status;

    //图片资源关联ID
    private Long businessId;

    //店铺详细地址
    private String address;

    //创建时间
    private java.util.Date createTime;

    //店铺信息
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public interface Property extends GenericPo.Property{
        String name = "name";
        String longtitude = "longtitude";
        String latitude = "latitude";
        String areaId = "areaId";
        String userId = "userId";
        String status = "status";
        String businessId = "businessId";
        String address = "address";
        String createTime = "createTime";
        String content = "content";
    }
}
