package com.base.web.controller;

import com.base.web.bean.Brand;
import com.base.web.bean.Goods;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.BrandService;
import com.base.web.service.GoodsService;
import com.base.web.service.ShopBrandService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/brand")
@AccessLogger("区域分配")
public class BrandController extends GenericController<Brand, Integer>{

    @Resource
    private BrandService brandService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private ShopBrandService shopBrandService;

    @Override
    public BrandService getService(){
        return this.brandService;
    }

//    查询全部品牌
    @RequestMapping(value = "/querybrand", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryAllBrand(){
        return ResponseMessage.ok(brandService.queryAllBrand());
    }

    //添加区域
    @RequestMapping(value = "/{uId}/delete", method = RequestMethod.PUT)
    @AccessLogger("删除品牌")
    @Authorize(action = "U")
    public ResponseMessage delete(@PathVariable("uId") Integer uId){
        if(goodsService.createQuery().where(Goods.Property.brandId,uId).single()!=null){
            return ResponseMessage.error("该品牌存在服装信息");
        }
        Brand brand = brandService.selectByPk(uId);
        brand.setStatus(0);
        return ResponseMessage.ok(brandService.update(brand));
    }

    @RequestMapping(value = "/queryShopBrand", method = RequestMethod.GET)
    @Authorize(action = "R")
    @AccessLogger("查询关联店铺的品牌")
    public ResponseMessage queryShopBrand(){
        if(WebUtil.getLoginUser().getUserRoles().get(0).getRoleId() != 10011){
            return ResponseMessage.ok(brandService.createQuery()
                    .where(Brand.Property.userId,WebUtil.getLoginUser().getId()).single());
        }
        return ResponseMessage.ok(shopBrandService.queryShopBrand());
    }
}
