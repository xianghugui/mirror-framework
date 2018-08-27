package com.base.web.dao;

import com.base.web.bean.ShopGoodsSpecification;

public interface ShopGoodsSpecificationMapper extends GenericMapper<ShopGoodsSpecification, Long> {
    int updateSpec(ShopGoodsSpecification shopGoodsSpecification);
}
