package com.base.web.bean;

import com.base.web.bean.po.GenericPo;
import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 14:40  2018/4/9
 */
public class Order extends GenericPo<Long> {
    //用户ID
    private Long userId;

    //商品价格
    private BigDecimal totalPrice;

    //订单状态
    private Integer orderStatus;

    //数量
    private  Integer num;

    //生成时间
    private Date createTime;

    //配送地址ID
    private Long addressId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public interface Property extends GenericPo.Property{
        String userId = "userId";
        String totalPrice = "totalPrice";
        String orderStatus = "orderStatus";
        String num = "num";
        String createTime = "createTime";
        String addressId = "addressId";
    }
}
