package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.math.BigDecimal;
import java.util.Date;

public class Goods  extends GenericPo<Long> {
    //品牌id
    private Long brandId;
    //商品类别id
    private Integer goodsClassId;
    //商品名称
    private String goodsName;
    // 商品状态 默认1 在售，0 下架，2售罄
    private Integer status;
    //推荐状态（0表示推荐，1表示取消推荐）
    private Integer  recommendStatus;
    //商品价格
    private BigDecimal price;
    //用户购买成功之后返现金额
    private BigDecimal cashBach;
    //用户购买成功之后秀用户分佣金额
    private BigDecimal commission;

    //商品图片id
    private Long imageId;
    //轮播图片id
    private Long carouselId;
    //compress id
    private Long compressId;
    //商品详情
    private String describe;
    //商品库存
    private Long num;

    //创建时间
    private Date createTime;

    //图片数组
    private Long[] imgIds;

    //轮播图片数组
    private Long[] CarouselImgUrls;

    //品牌名称
    private String brandName;

    //类别名称
    private String className;

    public Long getCompressId() {
        return compressId;
    }

    public void setCompressId(Long compressId) {
        this.compressId = compressId;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getCashBach() {
        return cashBach;
    }

    public void setCashBach(BigDecimal cashBach) {
        this.cashBach = cashBach;
    }

    public Integer getRecommendStatus() {
        return recommendStatus;
    }

    public void setRecommendStatus(Integer recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long[] getImgIds() {
        return imgIds;
    }

    public void setImgIds(Long[] imgIds) {
        this.imgIds = imgIds;
    }

    public Long[] getCarouselImgUrls() {
        return CarouselImgUrls;
    }

    public void setCarouselImgUrls(Long[] carouselImgUrls) {
        CarouselImgUrls = carouselImgUrls;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Integer getGoodsClassId() {
        return goodsClassId;
    }

    public void setGoodsClassId(Integer goodsClassId) {
        this.goodsClassId = goodsClassId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getCarouselId() {
        return carouselId;
    }

    public void setCarouselId(Long carouselId) {
        this.carouselId = carouselId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public interface Property extends GenericPo.Property {
        //品牌id
        String brandId="brandId";
        String goodsClassId="goodsClassId";
        String goodsName="goodsName";
        String status="status";
        String price="price";
        String imageId="imageId";
        String carouselId="carouselId";
        String describe="describe";
        String num="num";
        String brandName="brandName";
        String createTime = "createTime";
        String className = "className";
        String recommendStatus = "recommendStatus";
        String cashBach = "cashBach";
        String commission = "commission";
        String compressId = "compressId";
    }
}
