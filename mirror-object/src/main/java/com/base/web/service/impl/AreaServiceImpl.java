package com.base.web.service.impl;

import com.base.web.bean.Area;
import com.base.web.dao.AreaMapper;
import com.base.web.dao.ShopMapper;
import com.base.web.service.AreaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Geek、
 * @Date: Created in 14:08  2018/3/27
 */
@Service("AreaService")
public class AreaServiceImpl extends AbstractServiceImpl<Area, String> implements AreaService {

    @Resource
    private AreaMapper areaMapper;

    @Resource
    private ShopMapper shopMapper;

    @Override
    public List<Area> queryAllviewTree() {
        return getMapper().queryAllviewTree();
    }

    @Override
    protected AreaMapper getMapper() {
        return this.areaMapper;
    }

    @Override
    public int changestatus(String uId, String status) {
        return getMapper().changestatus(uId, status);
    }


    @Override
    public List<Area> queryProvince() {
        return getMapper().queryProvince();
    }

    @Override
    public List<Area> queryCity(String uId) {
        return getMapper().queryCity(uId);
    }

    @Override
    public List<Area> queryArea(String uId, Boolean status) {
        return getMapper().queryArea(uId, status);
    }

    @Override
    public List<Area> queryAll(){
        List<Area> list=getMapper().queryAllviewTree();
        List<Area> shopList=shopMapper.queryShopByAreaId();
        for(int i=0;i<shopList.size();i++){
            list.add(shopList.get(i));
        }
        return list;

    }

}
