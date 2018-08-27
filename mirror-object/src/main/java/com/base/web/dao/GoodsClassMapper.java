package com.base.web.dao;


import com.base.web.bean.GoodsClass;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GoodsClassMapper extends GenericMapper<GoodsClass,Integer>{

    // >>查询商品类别树通过父id
    List<Map> queryGoodsClassByParentId(@Param("parentId") Integer parentId, @Param("level") Integer level);

    List<Map> queryGoodsAllNodeAndAllChildNodeTree();

    /**
     * 根据level插叙类目
     * @param level
     * @return
     */
    List<Map> queryByLevel(Integer level);

    /**
     * 查询商品类别树种主键最大的等级类别主键
     * @return
     */
     Integer queryGoodsClassIdByLevel();

     Integer queryGoodsClassIdByLevelAndParent(@Param("level") Integer level,@Param("idPrefix") Integer idPrefix);
}
