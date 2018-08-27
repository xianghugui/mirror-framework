package com.base.web.service.impl;

import com.base.web.bean.GoodsClass;
import com.base.web.bean.common.QueryParam;
import com.base.web.dao.GoodsClassMapper;
import com.base.web.service.GoodsClassService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@Service("goodsClassService")
public class GoodsClassServiceImpl extends AbstractServiceImpl<GoodsClass,Integer> implements GoodsClassService{

    @Resource
    private GoodsClassMapper goodsClassMapper;

    @Override
    protected GoodsClassMapper getMapper(){
        return this.goodsClassMapper;
    }

    // >>查询商品类别树通过父id and level
    @Override
    public List<Map> queryGoodsClassByParentId(Integer parentId, Integer level){
        return getMapper().queryGoodsClassByParentId(parentId, level);
    }

    @Override
    public List<Map> queryGoodsAllNodeAndAllChildNodeTree(){
        return getMapper().queryGoodsAllNodeAndAllChildNodeTree();
    }

    @Override
    public List<Map> queryByLevel(Integer level) {
        return getMapper().queryByLevel(level);
    }

    @Override
    public Integer queryGoodsClassIdByLevel() {
        return getMapper().queryGoodsClassIdByLevel();
    }

    @Override
    public Integer queryGoodsClassIdByLevelAndParent(Integer level,Integer idPrefix) {
        return getMapper().queryGoodsClassIdByLevelAndParent(level,idPrefix);
    }

}
