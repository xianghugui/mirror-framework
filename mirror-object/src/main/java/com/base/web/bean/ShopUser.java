package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.sql.Timestamp;

/**
 * @Author: Geek、
 * @Date: Created in 13:52  2018/3/27
 */
public class ShopUser extends GenericPo<String> {

    //店铺ID
    private Long shopId;

    //用户ID
    private  Long userId;

    //关联时间
    private Timestamp createTime;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public interface Property extends GenericPo.Property {
        String shopId = "shopId";
        String userId = "userId";
        String createTime = "createTime";

    }
}
