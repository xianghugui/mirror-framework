package com.base.web.bean;

import com.base.web.bean.po.GenericPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
//@ApiModel(value = "ShopAddGoods", description = "商品信息")
public class ShopAddGoods extends GenericPo<Long> {

    //商品名称
//    @ApiModelProperty(value = "商品名称")
    private String name;
    //商品价格
//    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;
    //商品图片关联id
//    @ApiModelProperty(value = "商品图片关联id")
    private Long imageId;
    //类别id
//    @ApiModelProperty(value = "类别id")
    private Integer classId;
    //店铺id
//    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    // 商品适合年龄段
//    @ApiModelProperty(value = "商品适合年龄段")
    private Integer ageGrade;
    // 商品状态 默认1 在售，2下架
//    @ApiModelProperty(value = "商品状态 默认1 在售，2下架")
    private Integer status;
    //品牌id
//    @ApiModelProperty(value = "品牌id")
    private Integer brandId;
    //创建时间
//    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    // 适合性别
//    @ApiModelProperty(value = "适合性别")
    private String sex;
    // 适合场合id
//    @ApiModelProperty(value = "适合场合id")
    private Integer occasionId;

    private Long videoId; //视频ID

    private String videoSrc; //视频路径

    private Integer event; //0：不是活动商品，1：是活动商品
    private String imageUrl;
    //图片数组
//    @ApiModelProperty(value = "图片数组")
    private Long[] imgIds;



    //店铺商品规格表ID
    private Long specId;

    //删除图片ID数组
    private Long[] delImageId;


    //尺码数组
//    @ApiModelProperty(value = "尺码数组")
    private String[] sizes;

    private String[] color;

    public String getVideoSrc() {
        return videoSrc;
    }

    public void setVideoSrc(String videoSrc) {
        this.videoSrc = videoSrc;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long[] getDelImageId() {
        return delImageId;
    }

    public void setDelImageId(Long[] delImageId) {
        this.delImageId = delImageId;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public String[] getColor() {
        return color;
    }

    public void setColor(String[] color) {
        this.color = color;
    }

    public String[] getSizes() {
        return sizes;
    }

    public void setSizes(String[] sizes) {
        this.sizes = sizes;
    }

    public Long[] getImgIds() {
        return imgIds;
    }

    public void setImgIds(Long[] imgIds) {
        this.imgIds = imgIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getAgeGrade() {
        return ageGrade;
    }

    public void setAgeGrade(Integer ageGrade) {
        this.ageGrade = ageGrade;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getOccasionId() {
        return occasionId;
    }

    public void setOccasionId(Integer occasionId) {
        this.occasionId = occasionId;
    }

    public interface Property extends GenericPo.Property {
        //品牌id
        String name="name";
        String price="price";
        String imageId="imageId";
        String classId="classId";
        String shopId="shopId";
        String ageGrade="ageGrade";
        String status="status";
        String brandId="brandId";
        String createTime="createTime";
        String sex="sex";
        String occasionId = "occasionId";
        String event = "event";
        String videoId = "videoId";
    }
}
