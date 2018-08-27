package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.math.BigDecimal;
import java.util.Date;


public class VideoOrder extends GenericPo<Long> {
    //视频ID
    private Long videoId;

    //店铺ID
    private Long shopId;

    //创建日期
    private Date createTime;

    //用户ID
    private Long userId;

    //价格
    private BigDecimal price;

    //数量
    private Integer num;

    //尺寸
    private String size;

    //地址ID
    private Long addressId;

    //状态 0,待报价.1待付款，2待发货，3待收货，4退货，5订单完成，6取消订单，7缺货
    private Integer status;

    //更新用户ID
    private Long updateUserId;

    //报价时间
    private Date updateTime;

    //快递公司ID
    private Integer expressId;

    //运单号
    private String expressNumber;

    //缺货原因
    private String lackReason;

    //付款类型
    private Integer payType;

    //付款时间
    private Date paymentTime;

    //发货时间
    private Date theDeliveryTime;

    //发货时间
    private Date remindTime;

    //快递公司名
    private String expressName;

    //确认收货时间
    private Date receiptTime;

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public Date getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Date remindTime) {
        this.remindTime = remindTime;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getExpressId() {
        return expressId;
    }

    public void setExpressId(Integer expressId) {
        this.expressId = expressId;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getLackReason() {
        return lackReason;
    }

    public void setLackReason(String lackReason) {
        this.lackReason = lackReason;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getTheDeliveryTime() {
        return theDeliveryTime;
    }

    public void setTheDeliveryTime(Date theDeliveryTime) {
        this.theDeliveryTime = theDeliveryTime;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public interface Property extends GenericPo.Property{
        String videoId = "videoId";
        String shopId = "shopId";
        String createTime = "createTime";
        String userId = "userId";
        String price = "price";
        String num = "num";
        String addressId = "addressId";
        String status = "status";
        String updateUserId = "updateUserId";
        String updateTime = "updateTime";
        String expressId = "expressId";
        String expressNumber = "expressNumber";
        String lackReason = "lackReason";
        String size = "size";
        String paymentTime = "paymentTime";
        String theDeliveryTime = "theDeliveryTime";
        String remindTime = "remindTime";
        String receiptTime = "receiptTime";
    }
}
