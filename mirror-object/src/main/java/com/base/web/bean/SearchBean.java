package com.base.web.bean;

import com.base.web.bean.po.GenericPo;

import java.math.BigDecimal;


public class SearchBean extends GenericPo<Long> {

    //广告标题
    private Long classId;
    //搜索商品等级 0 全部， 1 第一级， 2 第二级
    private Integer level;

    // 状态（0待发布，1发布）
    private Integer brandId;

    //发布人
    private BigDecimal startPrice;

    //广告创建时间
    private BigDecimal endPrice;

    //搜索关键词
    private String searchStr;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(BigDecimal endPrice) {
        this.endPrice = endPrice;
    }

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    public interface Property extends GenericPo.Property {
        
        //品牌id
        String classId="classId";
        String level="level";
        String brandId="brandId";
        String startPrice="startPrice";
        String endPrice="endPrice";
        String searchStr = "searchStr";
    }
}
