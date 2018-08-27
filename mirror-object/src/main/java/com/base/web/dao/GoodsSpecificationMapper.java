package com.base.web.dao;

import com.base.web.bean.GoodsSpecification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsSpecificationMapper extends GenericMapper<GoodsSpecification,Long>{
    List<GoodsSpecification> queryGoodsSpecification(Long goodsId);
    int deleteSpecById(Long id);

    GoodsSpecification queryGoodsColorAndSize(Long uId);

    int updateGoodsSpecQuality(@Param("id")Long id,@Param("quality")Integer quality);

    int queryGoodsSpecQuality(Long id);
}
