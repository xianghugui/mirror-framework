package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;


/**
 * @Author: Geek、
 * @Date: Created in 11:01  2018/3/27
 */
public class Device extends GenericPo<Long> {

    // 设备标识码
    private String deviceCode;
    // 设备用户名
    private String username;
    // 设备登录密码
    private String password;
    //生产时间
    private Date createTime;

    //设备状态（0：正常，1：检修，2：异常）
    private Integer status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public interface Property extends GenericPo.Property {
        String deviceCode = "deviceCode";
        String createTime = "createTime";
        String status = "status";
        String username = "username";
        String password = "password";
    }
}
