package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

public class Message extends GenericPo<Long> {
    //标题
    private String title;
    //内容
    private String content;
    //生成时间
    private Date createTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public interface Property extends GenericPo.Property{
        String title = "title";
        String content = "content";
        String createTime = "createTime";
    }
}
