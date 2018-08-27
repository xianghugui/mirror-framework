package com.base.web.service;

import com.base.web.bean.GoodsClass;
import com.base.web.bean.common.QueryParam;


import java.util.List;
import java.util.Map;

public interface GoodsClassService extends GenericService<GoodsClass, Integer> {

    // >>查询商品类别树通过父id
    List<Map> queryGoodsClassByParentId(Integer parentId, Integer level);

    List<Map> queryGoodsAllNodeAndAllChildNodeTree();

    /**
     * 根据level插叙类目
     * @param level
     * @return
     */
    List<Map> queryByLevel(Integer level);

    Integer queryGoodsClassIdByLevel();

    Integer queryGoodsClassIdByLevelAndParent(Integer level,Integer idPrefix);
}
