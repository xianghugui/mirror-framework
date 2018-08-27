package com.base.web.bean;

import com.base.web.bean.po.GenericPo;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 13:43  2018/4/8
 */
public class TryInfo extends GenericPo<Long> {

    //试衣订单ID
    private Long tryId;

    //商品ID
    private Long goodsId;

    //商品规格ID
    private Long goodsSpecId;

    //状态,0待派单，1待发货，2试衣中，3购买，4退回，5商家确认退回，6待评价,7完成订单，8取消订单
    private Integer status;

    //创建时间
    private Date createTime;

    //店铺ID
    private Long shopId;

    //数量
    private Integer num;

    //快递公司ID
    private Integer expressId;

    //运单号
    private String expressNumber;

    //快递名称
    private String expressName;

    //价格
    private BigDecimal price;

    //收货地址ID
    private Long addressId;

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

    //收货时间
    @ApiModelProperty("提醒发货时间")
    private Date remindTime;

    //扩展字段 购物车ID
    private Long shoppingCartId;

    //支付类型：0微信支付，1：余额支付
    private Integer payType;

    public Date getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Date remindTime) {
        this.remindTime = remindTime;
    }

    public Date getFahuoTime() {
        return fahuoTime;
    }

    public void setFahuoTime(Date fahuoTime) {
        this.fahuoTime = fahuoTime;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
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

    public Date getShouhuoTime() {
        return shouhuoTime;
    }

    public void setShouhuoTime(Date shouhuoTime) {
        this.shouhuoTime = shouhuoTime;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getShoppingCartId() {
        return shoppingCartId;
    }


    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getTryId() {
        return tryId;
    }

    public void setTryId(Long tryId) {
        this.tryId = tryId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public interface Propety extends GenericPo.Property{
        String tryId = "tryId";
        String goodsId = "goodsId";
        String goodsSpecId = "goodsSpecId";
        String  status = "status";
        String createTime = "createTime";
        String shopId = "shopId";
        String num = "num";
        String price = "price";
        String expressId = "expressId";
        String expressNumber = "expressNumber";
        String shouhuoTime = "shouhuoTime";
        String buyTime = "buyTime";
        String paidanTime = "paidanTime";
        String fahuoTime = "fahuoTime";
        String remindTime = "remindTime";
    }
}
