package com.base.web.bean;

import com.base.web.bean.po.GenericPo;


/**
 * @Author: Geek、
 * @Date: Created in 11:12  2018/3/27
 */
public class UserAddress extends GenericPo<Long> {

    //创建用户ID
    private Long userId;
    //详细地址
    private String address;

    //联系电话
    private String phone;


    //店铺状态（0默认地址）
    private Integer status;

    //区域ID
    private Integer areaId;

    //收货坐标
    private String longtitude;

    //收货坐标
    private String laltitude;


    //收货人姓名
    private String userName;

    private Long goodsId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLaltitude() {
        return laltitude;
    }

    public void setLaltitude(String latitude) {
        this.laltitude = latitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public interface Property extends GenericPo.Property{
        String phone = "phone";
        String longtitude = "longtitude";
        String laltitude = "latitude";
        String areaId = "areaId";
        String userId = "userId";
        String status = "status";
        String address = "address";
        String userName = "userName";
    }
}
