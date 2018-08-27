package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;


public class GoodsComment extends GenericPo<Long> {
    //评价用户id
    private Long userId;
    //评价星级（1-5）
    private Integer star;
    //品论图片id
    private Long imageId;
    //品论时间
    private Date createTime;
    //商品规格id
    private Long specId;
    //商品Id
    private Long goodsId;
    //评价内容
    private String content;

    //订单详情ID
    private Long orderId;

    //订单类别 0：orderDetailID，1：TryOrderDealID, 2:VideoOrderID
    private Integer orderType;

    //上传图片文件ID数组
    private Long[] files;

    public Long[] getFiles() {
        return files;
    }

    public void setFiles(Long[] files) {
        this.files = files;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public interface Property extends GenericPo.Property{
        String userId = "userId";
        String star = "star";
        String imageId = "imageId";
        String createTime = "createTime";
        String specId = "specId";
        String orderId = "orderId";
        String orderType = "orderType";
    }
}
