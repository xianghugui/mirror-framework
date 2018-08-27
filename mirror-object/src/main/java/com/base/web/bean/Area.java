package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

/**
 * @Author: Geek、
 * @Date: Created in 10:41  2018/3/27
 */
public class Area extends GenericPo<String> {

    //区域ID
    private Integer uId;

    //区域名称
    private String areaName;

    //父级ID
    private Integer parentId;

    //区域等级
    private Integer level;

    //是否显示
    private Integer status;

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public interface Property extends GenericPo.Property {
        String uId = "uId";
        String areaName = "areaName";
        String parentId = "parentId";
        String level = "level";
        String status = "status";
    }
}
