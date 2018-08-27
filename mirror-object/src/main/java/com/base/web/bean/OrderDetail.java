package com.base.web.bean;

import com.base.web.bean.po.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 14:47  2018/4/9
 */
@ApiModel
public class OrderDetail extends GenericPo<Long> {

    //订单ID
    @ApiModelProperty("商品id")
    private Long orderId;

    //创建时间
    @ApiModelProperty("创建时间")
    private Date createTime;

    //付款时间
    @ApiModelProperty("付款时间")
    private Date buyTime;

    //派单时间
    @ApiModelProperty("派单时间")
    private Date paidanTime;

    //发货时间
    @ApiModelProperty("发货时间")
    private Date fahuoTime;

    //收货时间
    @ApiModelProperty("收货时间")
    private Date shouhuoTime;

    //提醒发货时间
    @ApiModelProperty("提醒发货时间")
    private Date remindTime;

    //商品ID
    @ApiModelProperty("商品ID")
    private Long goodsId;

    //规格ID
    @ApiModelProperty("规格ID")
    private Long goodsSpecId;

    //价格
    @ApiModelProperty("价格")
    private BigDecimal price;

    //数量
    @ApiModelProperty("数量")
    private Integer num;

    //店铺ID
    @ApiModelProperty("店铺ID")
    private Long shopId;


    //状态，0:待付款， 1 待派单，2：待发货，3：待收货，4：待评价，5：已评价，6：退货，7：订单关闭
    @ApiModelProperty("状态，0:待付款， 1 待派单，2：待发货，3：待收货，4：待评价，5：已评价，6：退货，7：订单关闭")
    private Integer status;

    //收货地址ID
    @ApiModelProperty("收货地址ID")
    private Long addressId;

    //快递公司ID
    @ApiModelProperty("快递公司ID")
    private Integer expressId;

    //运单号
    @ApiModelProperty("运单号")
    private String expressNumber;

    //付款类型0：微信支付，1：余额支付
    private Integer payType;

    //扩展字段 购物车ID
    private Long shoppingCartId;

    private Long userId;

    //快递名称
    private String expressName;
    //订单总价
    private BigDecimal priceSum;
    //    秀用户id
    private Long  showUserId;

    //    秀视频id
    private Long  videoId;

    //分润金额
    private BigDecimal commission;

    public Long getShowUserId() {
        return showUserId;
    }

    public void setShowUserId(Long showUserId) {
        this.showUserId = showUserId;
    }
    public Date getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Date remindTime) {
        this.remindTime = remindTime;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }


    public BigDecimal getPriceSum() {
        return priceSum;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public void setPriceSum(BigDecimal priceSum) {
        this.priceSum = priceSum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getGoodsSpecId() {
        return goodsSpecId;
    }

    public void setGoodsSpecId(Long goodsSpecId) {
        this.goodsSpecId = goodsSpecId;
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public Date getPaidanTime() {
        return paidanTime;
    }

    public void setPaidanTime(Date paidanTime) {
        this.paidanTime = paidanTime;
    }

    public Date getFahuoTime() {
        return fahuoTime;
    }

    public void setFahuoTime(Date fahuoTime) {
        this.fahuoTime = fahuoTime;
    }

    public Date getShouhuoTime() {
        return shouhuoTime;
    }

    public void setShouhuoTime(Date shouhuoTime) {
        this.shouhuoTime = shouhuoTime;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public interface Property extends GenericPo.Property{
        String orderId = "orderId";
        String createTime = "createTime";
        String goodsId = "goodsId";
        String goodsSpecId = "goodsSpecId";
        String price = "price";
        String num = "num";
        String shopId = "shopId";
        String status = "status";
        String addressId = "addressId";
        String expressId = "expressId";
        String expressNumber = "expressNumber";
        String userId = "userId";
        String buyTime = "buyTime";
        String paidanTime = "paidanTime";
        String fahuoTime = "fahuoTime";
        String shouhuoTime = "shouhuoTime";
        String remindTime = "remindTime";
        String showUserId = "showUserId";
        String videoId = "videoId";
        String commission = "commission";
    }
}
