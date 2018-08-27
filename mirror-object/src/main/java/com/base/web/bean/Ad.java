package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

public class Ad extends GenericPo<Long> {

    //广告标题
    private String title;
    //广告详情
    private String content;

    // 状态（0待发布，1发布）
    private Integer status;

    //发布人
    private String userName;

    //广告创建时间
    private Date createTime;

    //推送用户类型: 推送用户类型:  1 ,店铺广告,2 平台广告
    private Integer pushType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public interface Property extends GenericPo.Property {
        
        //品牌id
        String title="title";
        String content="content";
        String status="status";
        String userName="userName";
        String createTime="createTime";
        String pushType = "pushType";
    }
}
