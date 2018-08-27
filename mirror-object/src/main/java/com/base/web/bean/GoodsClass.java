package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

public class GoodsClass extends GenericPo<Integer> {
    //商品类别
    private Integer level;
    // 类别名称
    private String className;
    //父级id
    private Integer parentId;

    //资源ID
    private Long resourceId;

    private Integer status;

    private  Integer idPrefix;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(Integer idPrefix) {
        this.idPrefix = idPrefix;
    }

    public interface Property extends GenericPo.Property {
        String level="level";
        String className="className";
        String parentId="parentId";
        String resourceId="resourceId";
        String status="status";
        String idPrefix ="idPrefix";
    }
}
