package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

public class Brand extends GenericPo<Integer> {
    //品牌名称
    private String name;
    //品牌是否有在品台出售（0没有，1有）
    private Integer status;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public interface Property extends GenericPo.Property {
       //品牌名称
        String name="name";
        //品牌是否有在品台出售（0没有，1有）
        String status = "status";

        String userId = "userId";


    }
}
