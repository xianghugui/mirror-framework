package com.base.web.service.impl;

import com.base.web.bean.ShopAddGoods;
import com.base.web.bean.ShopGoodsPush;
import com.base.web.bean.ShopGoodsSpecification;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.common.UpdateParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.ShopAddGoodsMapper;
import com.base.web.dao.ShopGoodsSpecificationMapper;
import com.base.web.dao.ShopMapper;
import com.base.web.dao.resource.FileRefMapper;
import com.base.web.service.ShopAddGoodsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("shopAddGoodsService")
public class ShopAddGoodsServiceImpl extends AbstractServiceImpl<ShopAddGoods, Long> implements ShopAddGoodsService {
    @Resource
    private ShopAddGoodsMapper shopAddGoodsMapper;

    @Resource
    private FileRefMapper fileRefMapper;

    @Resource
    private ShopMapper shopMapper;
    @Resource
    private ShopGoodsSpecificationMapper shopGoodsSpecificationMapper;

    @Override
    protected ShopAddGoodsMapper getMapper() {
        return this.shopAddGoodsMapper;
    }

    @Override
    @Transactional
    public int insertShopAddGoods(ShopAddGoods data) {
        data.setId(GenericPo.createUID());
        data.setCreateTime(new Date());
        if (data.getImageId() == null) {
            data.setImageId(GenericPo.createUID());
        }
        data.setCreateTime(new Date());

        data.setShopId(shopMapper.queryShopIdByUserId(WebUtil.getLoginUser().getId()));
        data.setStatus(0);
        tryValidPo(data);
        insert(data);

        // 插入 t_file_rel 中间表（商品图片）
        insertFileRef(data);

        // 插入 商品规格表
        ShopGoodsSpecification shopGoodsSpecification = new ShopGoodsSpecification();
        shopGoodsSpecification.setId(GenericPo.createUID());
        shopGoodsSpecification.setGoodsId(data.getId());
        shopGoodsSpecification.setSize(getSpecification(data.getSizes()).toString());
        shopGoodsSpecification.setColor(getSpecification(data.getColor()).toString());
        shopGoodsSpecificationMapper.insert(InsertParam.build(shopGoodsSpecification));
        return 1;
    }

    @Override
    @Transactional
    public int editShopAddGoods(ShopAddGoods data) {

        shopAddGoodsMapper.editShopGoodsById(data);
        if(data.getDelImageId() != null){
            QueryParam param = new QueryParam();
            param.getParam().put("refId",data.getImageId());
            param.getParam().put("dataType",10);
            for (int i = 0; i < data.getDelImageId().length; i++) {
                param.getParam().put("resourceId",data.getDelImageId()[i]);
                fileRefMapper.deleteByRefIdResourceIdDateType(param);
            }
        }
        insertFileRef(data);
        // 更新 商品规格表
        ShopGoodsSpecification shopGoodsSpecification = new ShopGoodsSpecification();
        shopGoodsSpecification.setId(data.getSpecId());
        shopGoodsSpecification.setSize(getSpecification(data.getSizes()).toString());
        shopGoodsSpecification.setColor(getSpecification(data.getColor()).toString());
        shopGoodsSpecificationMapper.updateSpec(shopGoodsSpecification);
        return 1;
    }

    private StringBuffer getSpecification(String[] str) {
        StringBuffer specification = new StringBuffer("");
        for (int i = 0; i < str.length; i++) {
            specification.append(str[i]);
            specification.append(",");
        }
        specification.delete(specification.length()-1,specification.length());
        return specification;
    }

    private void insertFileRef(ShopAddGoods data) {
        // 插入 t_file_rel 中间表（商品图片）
        if (data.getImgIds() != null && data.getImageId() != 0L) {
            for (int i = 0; i < data.getImgIds().length; i++) {
                FileRef fileRef = new FileRef();
                fileRef.setId(GenericPo.createUID());
                fileRef.setRefId(data.getImageId());
                fileRef.setResourceId(data.getImgIds()[i]);
                fileRef.setDataType(10);
                fileRefMapper.insert(InsertParam.build(fileRef));
            }
        }
    }

    @Override
    public PagerResult<Map> pagerShopGoods(QueryParam param) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        param.setPaging(false);
        Integer total = getMapper().totalShopGoods(param);

        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(getMapper().pagerShopGoods(param));
        }
        return pagerResult;
    }

    @Override
    public Map selectShopGoodsById(Long uId) {
        return getMapper().selectShopGoodsById(uId);
    }

    @Override
    public Map queryById(Long goodsId) {
        return getMapper().queryById(goodsId);
    }

    @Override
    public List<Map> selectClassIdByShopId(Long shopId) {
        return getMapper().selectClassIdByShopId(shopId);
    }

    @Override
    public List<Map> selectApplicationIdByShopId(Long shopId) {
        return getMapper().selectApplicationIdByShopId(shopId);
    }

    @Override
    public int modifyStatus(Long id, Integer status) {
        return getMapper().modifyStatus(id, status);
    }

    @Override
    public List<Map> selectByShopGoodsPush(ShopGoodsPush shopGoodsPush) {
        return getMapper().selectByShopGoodsPush(shopGoodsPush);
    }

    @Override
    public List<Map> queryEventGoods(Long shopId) {
        return getMapper().queryEventGoods(shopId);
    }
}
