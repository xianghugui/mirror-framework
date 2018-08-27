package com.base.web.bean;

import com.base.web.bean.po.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @Author: Geek、
 * @Date: Created in 11:12  2018/3/27
 */

@ApiModel
public class UserFeature extends GenericPo<Long> {

    //代理用户ID
    @ApiModelProperty("代理用户ID")
    private Long userId;
    //身高
    @ApiModelProperty("身高")
    private  Integer height;
    //体重
    @ApiModelProperty("体重")
    private  Integer weight;

    //胸围
    @ApiModelProperty("胸围")
    private Integer chest;

    //腰围
    @ApiModelProperty("腰围")
    private Integer waist;

    //臀围
    @ApiModelProperty("臀围")
    private Integer hip;

    //默认状态（0））
    @ApiModelProperty("默认状态(0)")
    private Integer status;

    //扩展字段 试穿次数
    @ApiModelProperty("试穿次数")
    private Integer residueDegree;

    //上传图片id数组
    private Long[] imageId;


    public Long[] getImageId() {
        return imageId;
    }

    public void setImageId(Long[] imageId) {
        this.imageId = imageId;
    }


    public Integer getResidueDegree() {
        return residueDegree;
    }

    public void setResidueDegree(Integer residueDegree) {
        this.residueDegree = residueDegree;
    }

    public Integer getChest() {
        return chest;
    }

    public void setChest(Integer chest) {
        this.chest = chest;
    }

    public Integer getWaist() {
        return waist;
    }

    public void setWaist(Integer waist) {
        this.waist = waist;
    }

    public Integer getHip() {
        return hip;
    }

    public void setHip(Integer hip) {
        this.hip = hip;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public interface Property extends GenericPo.Property{
        String userId = "userId";
        String height = "height";
        String status = "status";
        String hip = "hip";
        String waist = "waist";
        String chest = "chest";
        String weight = "weight";
    }
}
