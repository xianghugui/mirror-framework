package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;

public class VideoUser extends GenericPo<Long> {
    //视频id
    private  Long videoId;
    //视频用户id
    private  Long userId;
    //资源店铺id
    private  Long shopId;

    // 是否询价（0，待询价，1已报价，2完成购买，3结束）
    private Integer status;
    //创建时间
    private Date createTime;

    //视频秀关联商品id（该商品是视频上传商家在平台有售的商品id）
    private  Long goodsId;

    // 点赞数
    private Integer likeNum;

    private Date associatedTime; //关联服装时间

    //浏览量
    private Long pageView;


    public Date getAssociatedTime() {
        return associatedTime;
    }

    public void setAssociatedTime(Date associatedTime) {
        this.associatedTime = associatedTime;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getPageView() {
        return pageView;
    }

    public void setPageView(Long pageView) {
        this.pageView = pageView;
    }

    public interface Property extends GenericPo.Property {
        String videoId="videoId";
        String userId="userId";
        String shopId="shopId";
        String status="status";
        String createTime="createTime";
        String goodsId="goodsId";
        String likeNum="likeNum";
        String associatedTime="associatedTime";
        String pageView = "pageView";
    }
}
