package com.base.web.service.impl;

import com.base.web.bean.Ad;
import com.base.web.bean.Area;
import com.base.web.bean.ShopAd;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.dao.AdMapper;
import com.base.web.dao.AreaMapper;
import com.base.web.dao.ShopAdMapper;
import com.base.web.dao.ShopMapper;
import com.base.web.service.ShopAdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 10:03  2018/3/28
 */
@Service("ShopAdService")
public class ShopAdServiceImpl extends AbstractServiceImpl<ShopAd, Long> implements ShopAdService {

    @Resource
    private ShopAdMapper ShopAdMapper;

    @Resource
    private AreaMapper areaMapper;

    @Resource
    private ShopMapper shopMapper;

    @Resource
    private AdMapper adMapper;

    @Override
    protected ShopAdMapper getMapper() {
        return this.ShopAdMapper;
    }
    @Override
    public Long insertShopAd(ShopAd shopAd) {
        String str = shopAd.getAreaList();
        String[] list = str.split(",");
        Long adDataId = shopAd.getAdId();
        for (int i = 0; i < list.length; i++) {
            areaList(Long.parseLong(list[i]), adDataId);
        }

        //更改广告状态
        adMapper.updateStatusByUId(adDataId, 1);
        return adDataId;
    }

    //遍历区域树
    @Transactional
    public void areaList(Long parentId, Long adDataId) {
        List<Area> idList = areaMapper.queryAreaByParentId(parentId);
        if (idList != null && idList.size() > 0) {
            for (Area i : idList) {
                areaList(Long.valueOf(i.getuId()), adDataId);
            }
        } else {
            List<Long> list = shopMapper.queryShopByParentId(parentId);
            if (list.size() > 0) {
                for (int h = 0; h < list.size(); h++) {
                    insertShopAd(list.get(h), adDataId);
                }
            }
            if (shopMapper.selectByPk(parentId) != null) {
                insertShopAd(parentId, adDataId);
            }

        }
    }

    public void insertShopAd(Long shopId, Long adDataId) {
        //插入用户店铺广告表
        ShopAd data = new ShopAd();
        data.setId(GenericPo.createUID());
        data.setAdId(adDataId);
        data.setShopId(shopId);
        data.setType(1);
        data.setCreateTime(new Date());
        getMapper().insert(InsertParam.build(data));
    }

    @Override
    public int cancelPush(Long adId) {
        adMapper.updateStatusByUId(adId, 0);
        return getMapper().cancelPush(adId);
    }

    @Override
    public PagerResult<Map> queryShopAd(QueryParam param) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        Integer total = getMapper().queryShopAdTotal(param);

        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            //根据实际记录数量重新指定分页参数
            pagerResult.setData(getMapper().queryShopAd(param));
        }
        return pagerResult;
    }

    @Override
    public PagerResult<Map> queryUserAd(QueryParam param) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        //  查询用户端咨询是查询当前用户是否在平台购买过商品
         List<Map> list =  getMapper().queryUserOrdersTotal(param);
        Integer total ;
         if(list.size() > 0){//用户有在平台买过商品
             total = getMapper().queryUserAdTotal(param);
             pagerResult.setTotal(total);
             if (total == 0) {
                 pagerResult.setData(new ArrayList<>());
             } else {
                 //根据实际记录数量重新指定分页参数
                 pagerResult.setData(getMapper().queryUserAd(param));
             }
         }else {
             total = getMapper().queryUserAdNotBuyTotal();
             pagerResult.setTotal(total);
             if (total == 0) {
                 pagerResult.setData(new ArrayList<>());
             } else {
                 //根据实际记录数量重新指定分页参数
                 pagerResult.setData(getMapper().queryUserAdNotBuy( param));
             }
         }

        return pagerResult;
    }

    @Override
    public Map queryShopAdInfo(Long shopAdId){
        return getMapper().queryShopAdInfo(shopAdId);
    }
}
