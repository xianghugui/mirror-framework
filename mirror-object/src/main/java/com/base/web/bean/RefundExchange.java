package com.base.web.bean;

import com.base.web.bean.po.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(description= "返回退款信息")
public class RefundExchange extends GenericPo<Long> {

    @ApiModelProperty(value = "父订单ID")
    private Long parentOrderId;

    @ApiModelProperty(value = "子订单ID")
    private Long childOrderId;

    @ApiModelProperty(value = "申请退货人ID")
    private Long userId;

    @ApiModelProperty(value = "最终退款人ID")
    private Long dealUserId;

    @ApiModelProperty(value = "退货单类型，0：平台购买，1：试衣购买，2：视频购买")
    private Integer type;

    @ApiModelProperty(value = "状态0:申请退货退款 1：同意退货 2：用户退货3:商家确认收货 4：平台退款，5：完成退款请求，" +
            "6：退款请求关闭（用户操作）7：商家拒绝退款")
    private Integer status;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "请求原因")
    private String content;

    @ApiModelProperty(value = "申请时间")
    private Date applicationTime;

    @ApiModelProperty(value = "处理时间")
    private Date dealTime;

    @ApiModelProperty(value = "拒绝原因")
    private String refuseReason;

    @ApiModelProperty(value = "退货类型，0：仅退款，1：退货退款")
    private Integer refundType;

    @ApiModelProperty(value = "拒绝退货图片对应资源ID")
    private Long refuseImageId;

    @ApiModelProperty(value = "申请退货图片对应资源ID")
    private Long applyImageId;

    @ApiModelProperty(value = "快递公司ID")
    private Integer expressId;

    @ApiModelProperty(value = "运单号")
    private String expressNumber;

    //提醒发货时间
    @ApiModelProperty("提醒确认退款时间或提醒确认收货时间")
    private Date remindTime;

    private Long[] imageIdArray;

    public Long[] getImageIdArray() {
        return imageIdArray;
    }

    public void setImageIdArray(Long[] imageIdArray) {
        this.imageIdArray = imageIdArray;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(Long parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    public Long getChildOrderId() {
        return childOrderId;
    }

    public void setChildOrderId(Long childOrderId) {
        this.childOrderId = childOrderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDealUserId() {
        return dealUserId;
    }

    public void setDealUserId(Long dealUserId) {
        this.dealUserId = dealUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public String refuseReason() {
        return refuseReason;
    }

    public void refuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public Integer getRefundType() {
        return refundType;
    }

    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
    }

    public Long getRefuseImageId() {
        return refuseImageId;
    }

    public void setRefuseImageId(Long refuseImageId) {
        this.refuseImageId = refuseImageId;
    }

    public Long getApplyImageId() {
        return applyImageId;
    }

    public void setApplyImageId(Long applyImageId) {
        this.applyImageId = applyImageId;
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

    public Date getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Date remindTime) {
        this.remindTime = remindTime;
    }

    public interface Property extends GenericPo.Property{
        String parentOrderId = "parentOrderId";
        String childOrderId = "childOrderId";
        String userId = "userId";
        String dealUserId = "dealUserId";
        String type = "type";
        String status = "status";
        String content = "content";
        String applicationTime = "applicationTime";
        String dealTime = "dealTime";
        String refuseReason = "refuseReason";
        String refuseimageId = "imageId";
        String price = "price";
        String shopId = "shopId";
        String applyImageId = "applyImageId";
        String expressId = "expressId";
        String expressNumber = "expressNumber";
        String refundType = "refundType";
        String remindTime = "remindTime";
    }
    
}
