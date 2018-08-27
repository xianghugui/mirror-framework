package com.base.web.service.impl;

import com.base.web.bean.GoodsSpecification;
import com.base.web.dao.GoodsSpecificationMapper;
import com.base.web.service.GoodsSpeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

@Service("GoodsSpeService")
public class GoodsSpeServiceImpl extends AbstractServiceImpl<GoodsSpecification, Long> implements GoodsSpeService{
    @Resource
    private GoodsSpecificationMapper goodsSpecificationMapper;

    @Override
    public GoodsSpecificationMapper getMapper(){
        return this.goodsSpecificationMapper;
    }

    @Override
    public List<GoodsSpecification> queryGoodsSpecification(Long goodsId){
        return getMapper().queryGoodsSpecification(goodsId);
    }

    @Override
    public int deleteSpecById(Long id){
        return getMapper().deleteSpecById(id);
    }

    @Override
    public int updateGoodsSpecQuality(Long id, Integer quality) {
        return getMapper().updateGoodsSpecQuality(id, quality);
    }
}
