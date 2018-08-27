package com.base.web.bean;

import com.base.web.bean.po.GenericPo;


public class Logistics extends GenericPo<Integer> {

    //发布人
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public interface Property extends GenericPo.Property {

        String name="name";
    }
}
