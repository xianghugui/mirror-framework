package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.util.Date;
import java.util.List;

public class ShopGoodsPush extends GenericPo<Long> {

    //用户ID
    private Long userId;

    //店铺ID
    private Long shopId;

    //身高
    private Integer height;

    //体重
    private Integer weight;

    //性别
    private String sex;

    //年龄
    private Integer age;
    private String agestr;

    //感兴趣的
    private String like;

    //适应场合
    private String application;

    //感兴趣的数组
    private String[] likes;

    //适应场合数组
    private String[] applications;

    //手机号
    private String phone;

    //创建时间
    private Date createTime;

    //推荐商品列表
    private String goodsId;
    private List< ShopAddGoods> shopAddGoodsList;

    public String getAgestr() {
        return agestr;
    }

    public void setAgestr(String agestr) {
        this.agestr = agestr;
    }

    public List<ShopAddGoods> getShopAddGoodsList() {
        return shopAddGoodsList;
    }

    public void setShopAddGoodsList(List<ShopAddGoods> shopAddGoodsList) {
        this.shopAddGoodsList = shopAddGoodsList;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String[] getLikes() {
        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }

    public String[] getApplications() {
        return applications;
    }

    public void setApplications(String[] applications) {
        this.applications = applications;
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

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public interface Property extends GenericPo.Property{
        String userId = "userId";
        String shopId = "shopId";
        String height = "height";
        String weight = "weight";
        String sex = "sex";
        String age = "age";
        String like = "like";
        String phone = "phone";
        String createTime = "createTime";
        String application = "application";
        String goodsId = "goodsId";
    }
}
