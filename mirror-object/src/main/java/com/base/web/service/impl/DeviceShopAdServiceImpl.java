package com.base.web.service.impl;

import com.base.web.bean.Area;
import com.base.web.bean.DeviceShopAd;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.dao.AreaMapper;
import com.base.web.dao.DeviceAdMapper;
import com.base.web.dao.DeviceShopAdMapper;
import com.base.web.dao.ShopMapper;
import com.base.web.service.DeviceShopAdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @Author: Geek、
 * @Date: Created in 10:03  2018/3/28
 */
@Service("DeviceShopAdService")
public class DeviceShopAdServiceImpl extends AbstractServiceImpl<DeviceShopAd, Long> implements DeviceShopAdService{

    @Resource
    private DeviceShopAdMapper DeviceShopAdMapper;


    @Resource
    private DeviceAdMapper deviceAdMapper;

    @Resource
    private AreaMapper areaMapper;

    @Resource
    private ShopMapper shopMapper;

    @Override
    protected DeviceShopAdMapper getMapper() {
        return this.DeviceShopAdMapper;
    }

    @Override
    public int insertDeviceShopAdList(DeviceShopAd deviceShopAd){

        Long adDataId = deviceShopAd.getDeviceAdId();
        Long[] list = deviceShopAd.getDeviceIdList();
        for(int i=0;i<list.length;i++){
            areaList(new Long(list[i]),adDataId);
        }
        //更改广告状态
        deviceAdMapper.updateStatusByUId(adDataId,1);
        return 1;
    }

    //遍历区域树
    @Transactional
    public void areaList(Long parentId,Long adDataId){
        List<Area> idList=areaMapper.queryAreaByParentId(parentId);
        if(idList.size()>0){
            for(Area i:idList){
                areaList(Long.valueOf(i.getuId()),adDataId);
            }
        }else {
            List<Long> list = shopMapper.queryShopByParentId(parentId);
            if (list.size()>0) {
                for (int h = 0; h < list.size(); h++) {
                    insertDeviceShopAd(list.get(h), adDataId);
                }
            }
            if(shopMapper.selectByPk(parentId) != null){
                insertDeviceShopAd(parentId, adDataId);
            }
        }
    }

    public int insertDeviceShopAd(Long areaId,Long adDataId){
        //插入设备店铺广告表
        DeviceShopAd data=new DeviceShopAd();
        data.setId(GenericPo.createUID());
        data.setDeviceId(areaId);
        data.setDeviceAdId(adDataId);
        data.setCreateTime(new Date());
        data.setStatus(0);
        int i = getMapper().insert(InsertParam.build(data));
        return i;
    }

    @Override
    public int cancelPush(Long adDataId){
        deviceAdMapper.updateStatusByUId(adDataId,0);
        return getMapper().cancelPush(adDataId);
    }


}
