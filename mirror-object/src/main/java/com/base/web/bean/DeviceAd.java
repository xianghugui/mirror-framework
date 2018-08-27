package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

/**
 * @Author: Geek、
 * @Date: Created in 11:12  2018/3/27
 */
public class DeviceAd extends GenericPo<Long> {

    //设备广告关联ID
    private Long adDataId;

    //广告状态（0待发布，1发布）
    private Integer status;

    //广告创建时间
    private Date createTime;

    //发布人
    private String userName;

    //广告列表
    private Long[] adList;

    //扩展字段 资源id
    private Long resourceId;
    //类型
    private Integer type;

    //广告开始时间
    private Date startTime;

    //广告结束时间
    private Date endTime;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long[] getAdList() {
        return adList;
    }

    public void setAdList(Long[] adList) {
        this.adList = adList;
    }

    public Long getAdDataId() {
        return adDataId;
    }

    public void setAdDataId(Long adDataId) {
        this.adDataId = adDataId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public interface Property extends GenericPo.Property{
        String adDataId = "adDataId";
        String status = "status";
        String createTime = "createTime";
        String userName = "userName";
        String startTime = "startTime";
        String endTime = "endTime";
    }
}
