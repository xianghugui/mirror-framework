package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.math.BigDecimal;
import java.util.Date;


public class ShoppingCart extends GenericPo<Long> {
    //用户id
    private Long userId;
    //商品id
    private Long goodsId;
    //商品规格id
    private Long goodsSpecId;
    //购买数量
    private Integer num;
//    秀用户id
    private Long  showUserId;

    private Date createTime;

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

    public Long getGoodsSpecId() {
        return goodsSpecId;
    }

    public void setGoodsSpecId(Long goodsSpecId) {
        this.goodsSpecId = goodsSpecId;
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

    public interface Property extends GenericPo.Property {
        String userId = "userId";
        String goodsId = "goodsId";
        String goodsSpecId = "goodsSpecId";
        String num = "num";
        String showUserId = "showUserId";
        String createTime = "createTime";
        String videoId = "videoId";
        String commission = "commission";
    }
}
