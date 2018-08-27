package com.base.web.bean;

import com.base.web.bean.po.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Author: Geek、
 * @Date: Created in 11:12  2018/3/27
 */
@ApiModel(value = "平台服装代理参数")
public class UserAgent extends GenericPo<Long> {

    //代理用户ID
    @ApiModelProperty("代理用户ID")
    private Long userId;

    //商品ID
    @ApiModelProperty("商品ID")
    private Long goodsId;

    //店铺状态（ 代理商品状态（0，代卖，1，取消代卖））
    @ApiModelProperty("店铺状态（ 代理商品状态（0，代卖，1，取消代卖）")
    private Integer status;

    //操作时间
    @ApiModelProperty("操作时间")
    private Date dealTime;


    private  Integer  agentStatus;

    public Integer getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(Integer agentStatus) {
        this.agentStatus = agentStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public interface Property extends GenericPo.Property{
        String userId = "userId";
        String goodsId = "goodsId";
        String status = "status";
        String dealTime = "dealTime";
    }
}
