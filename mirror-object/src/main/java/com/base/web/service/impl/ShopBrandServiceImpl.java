package com.base.web.service.impl;

import com.base.web.bean.ShopBrand;
import com.base.web.dao.ShopBrandMapper;
import com.base.web.service.ShopBrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 10:23  2018/4/4
 */
@Service("ShopBrandService")
public class ShopBrandServiceImpl extends AbstractServiceImpl<ShopBrand, Long> implements ShopBrandService {

    @Resource
    private ShopBrandMapper shopBrandMapper;

    @Override
    protected ShopBrandMapper getMapper() {
        return this.shopBrandMapper;
    }

    @Override
    public List<Map> queryBrand(String shopId) {
        return getMapper().queryBrand(shopId);
    }

    @Override
    public List<Map> queryAllShopByBrandId(String brandId){
        return getMapper().queryAllShopByBrandId(brandId);
    }

    @Override
    public List<Map> queryShopBrand(){
        return getMapper().queryShopBrand();
    }
}
