package com.base.web.service;


import com.base.web.bean.GoodsSpecification;


import java.util.List;

public interface GoodsSpeService extends GenericService<GoodsSpecification,Long>{
    List<GoodsSpecification> queryGoodsSpecification(Long goodsId);
    int deleteSpecById(Long id);

    /**
     * 修改数量 quality为正则添加多少，为负则减少多少
     * @param id
     * @param quality
     * @return
     */
    int updateGoodsSpecQuality(Long id, Integer quality);
}
