package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 17:22  2018/5/3
 */
public class Property extends GenericPo<Long> {

    //属性名称
    private String name;

    //1 年龄段 ， 2 服装适合场合
    private Integer type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
