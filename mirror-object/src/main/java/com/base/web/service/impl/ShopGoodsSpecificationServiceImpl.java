package com.base.web.service.impl;

import com.base.web.bean.ShopGoodsSpecification;
import com.base.web.dao.ShopGoodsSpecificationMapper;
import com.base.web.service.ShopGoodsSpecificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("ShopGoodsSpecificationService")
public class ShopGoodsSpecificationServiceImpl extends AbstractServiceImpl<ShopGoodsSpecification, Long> implements ShopGoodsSpecificationService {
    @Resource
    private ShopGoodsSpecificationMapper shopGoodsSpecificationMapper;
    @Override
    protected ShopGoodsSpecificationMapper getMapper() {
        return this.shopGoodsSpecificationMapper;
    }
}
