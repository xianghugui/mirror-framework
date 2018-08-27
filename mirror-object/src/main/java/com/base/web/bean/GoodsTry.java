package com.base.web.bean;

import com.base.web.bean.po.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 11:10  2018/4/8
 */
@ApiModel
public class GoodsTry extends GenericPo<Long> {

    //用户id
    @ApiModelProperty("用户id")
    private Long userId;

    //地址id
    @ApiModelProperty("地址id")
    private Long addressId;

    //创建时间
    @ApiModelProperty("创建时间")
    private Date createTime;

    //状态0：未完成，1全部试衣商品完成试衣
    @ApiModelProperty("状态0：未完成，1全部试衣商品完成试衣")
    private Integer status;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public interface Property extends GenericPo.Property{
        String userId = "userId";
        String addressId = "addressId";
        String createTime = "createTime";
        String status = "status";
    }
}
