package com.base.web.bean;

import com.base.web.bean.po.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Specification", description = "服装规格")
public class Specification extends GenericPo<Integer> {
    //属性名称
    @ApiModelProperty(value = "属性名称")
    private String name;
    //属性类别（0，尺寸，1颜色）
    @ApiModelProperty(value = "属性类别（0，尺寸，1颜色）")
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
    public interface Property extends GenericPo.Property{
        String name="name";
        String type="type";
    }
}
