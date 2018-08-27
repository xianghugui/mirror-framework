package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

/**
 * @Author: Geek、
 * @Date: Created in 10:32  2018/3/28
 */
public class UserShare extends GenericPo<Long> {

    //会员上线（父级会员）
    private Long parentId;

    //用户id
    private Long userId;


    //分享链接id（链接分视频或者图片）
    private Long goodsId;

    //0 视频， 1 商品
    private Integer type;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public interface Property extends GenericPo.Property{
        String parentId = "parentId";
        String userId = "userId";
        String goodsId = "goodsId";
        String  type = "type";
    }
}
