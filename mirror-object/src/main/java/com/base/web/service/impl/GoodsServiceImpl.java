package com.base.web.service.impl;

import com.base.web.bean.Goods;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.core.exception.NotFoundException;
import com.base.web.dao.GoodsMapper;
import com.base.web.dao.resource.FileRefMapper;
import com.base.web.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("goodsService")
public class GoodsServiceImpl extends AbstractServiceImpl<Goods,Long> implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private FileRefMapper fileRefMapper;

    @Override
    protected GoodsMapper getMapper(){
        return this.goodsMapper;
    }

    @Override
    public PagerResult<Goods> queryGoodsByClassId(QueryParam param){
        PagerResult<Goods> pagerResult = new PagerResult<>();
//        param.setPaging(false);
        Integer total = getMapper().queryGoodsTotalByClassId(param);

        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            //根据实际记录数量重新指定分页参数
//            param.rePaging(total.size());
            pagerResult.setData(getMapper().queryGoodsByClassId(param));
        }
        return pagerResult;
    }

    @Override
    public Goods queryGoodsById(Long id){
        return getMapper().queryGoodsById(id);
    }

    @Override
    public void enableUser(Long id) {
        Goods goodsInfo = selectByPk(id);
        if (goodsInfo == null) throw new NotFoundException("商品不存在!");
        goodsInfo.setStatus(1);
        createUpdate(goodsInfo).includes(Goods.Property.status).where(Goods.Property.id, id).exec();
    }

    @Override
    public void disableUser(Long id) {
        Goods goodsInfo = selectByPk(id);
        if (goodsInfo == null) throw new NotFoundException("商品不存在!");
        goodsInfo.setStatus(0);
        createUpdate(goodsInfo).includes(Goods.Property.status).where(Goods.Property.id, id).exec();
    }

    @Override
    public PagerResult<Map> allGoods(QueryParam param) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        param.setPaging(false);
        int total = getMapper().total(param);

        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            //根据实际记录数量重新指定分页参数
            param.rePaging(total);
            pagerResult.setData(getMapper().allGoods(param));
        }
        return pagerResult;
    }

    @Override
    public PagerResult<Map> queryGoods(QueryParam param){
        PagerResult<Map> pagerResult = new PagerResult<>();
        Integer total = null;
        List<Map> list = null;
        if(param.getParam().get("statusId").equals("1")){
            total = getMapper().queryfittingShowTotal(param);
            list = getMapper().queryfittingShow(param);
        }
        else {
            total = getMapper().queryAllGoodsByGoodsClassId(param);
            list = getMapper().queryGoods(param);
        }
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(list);
        }
        return pagerResult;
    }

    @Override
    public Map queryGoodsAndSales(Long uId,Long userId){
        Map goods = getMapper().queryGoodsAndSales(uId);
        goods.put("agentStatus",getMapper().queryAgent(uId,userId));
        return goods;
    }

    @Override
    public PagerResult<Map> queryVideoAssociationGoods(QueryParam param) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        Integer total = getMapper().queryVideoAssociationGoodsTotal(param);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(getMapper().queryVideoAssociationGoods(param));
        }
        return pagerResult;
    }

    @Override
    public Map queryGoodsImgSrcById(String goodsId) {
        return getMapper().queryGoodsImgSrcById(goodsId);
    }



    /**
     * 插入商品信息，同时往 店铺商品关联表 插入关联关系
     * @param data 要添加的数据
     * @return
     */
    @Override
    @Transactional
    public int insertGoods(Goods data) {
        int flag = 0;
        if (data.getId() == null) {
            data.setId(GenericPo.createUID());
        }
        if (data.getImageId() == null) {
            data.setImageId(GenericPo.createUID());
        }
        if (data.getCarouselId() == null) {
            data.setCarouselId(GenericPo.createUID());
        }
        data.setCreateTime(new Date());
        data.setStatus(1);
        tryValidPo(data);
        getInsertMapper().insert(InsertParam.build(data));


        // 插入 t_metadata_rel 中间表（商品详情图）
        if (data.getImgIds() != null && data.getImageId() != 0L) {
            flag = this.insertImg(data.getImgIds(), data.getImageId());
            if (flag == 0){
                return 0;
            }
        }

        // 插入 t_metadata_rel 中间表（轮播图）
        if (data.getCarouselImgUrls() != null && data.getCarouselId() != 0L) {
            flag = this.insertCarouseImg(data.getCarouselImgUrls(), data.getCarouselId());
            if (flag == 0){
                return 0;
            }
        }

        return 1;
    }

    /**
     * 插入 媒体资源 关联表 商品图片
     * @param imgs
     * @param recordId
     * @return
     */
    private int insertImg(Long[] imgs, Long recordId) {
        int flag = 0;
            if (imgs.length > 0) {
                for(int i=0;i<imgs.length;i++) {
                    FileRef fileRef = new FileRef();
                    fileRef.setId(GenericPo.createUID());
                    fileRef.setRefId(recordId);
                    fileRef.setResourceId(imgs[i]);
                    fileRef.setDataType(2);
                    fileRef.setType(0);
                    flag = fileRefMapper.insert(InsertParam.build(fileRef));
                }
            }

        return flag;
    }

    /**
     * 插入 媒体资源 关联表 商品图片
     * @param imgs
     * @param recordId
     * @return
     */
    private int insertCarouseImg(Long[] imgs, Long recordId) {
        int flag = 0;

        if (imgs.length > 0) {
            for(int i=0;i<imgs.length;i++) {
                FileRef fileRef = new FileRef();
                fileRef.setId(GenericPo.createUID());
                fileRef.setRefId(recordId);
                fileRef.setResourceId(imgs[i]);
                fileRef.setDataType(3);
                fileRef.setType(0);
                flag = fileRefMapper.insert(InsertParam.build(fileRef));
            }
        }

        return flag;
    }

    @Override
    @Transactional
    public int updateGoods(Goods data) {
        int flag ;
        tryValidPo(data);
        createUpdate().fromBean(data).where(GenericPo.Property.id).exec();
        //查询当前需要更新的商品的商品关联id和轮播关联id
        Goods currentUpdatingGoodsInfo = getMapper().selectByPk(data.getId());
        Long goodsImageRecordID = currentUpdatingGoodsInfo.getImageId();
        Long goodsCarouselRecordID = currentUpdatingGoodsInfo.getCarouselId();
        // 插入 t_metadata_rel 中间表（商品详情图）
        if (data.getImgIds() != null && goodsImageRecordID != 0L) {
            flag = this.updateGoodsImage(data.getImgIds(), goodsImageRecordID);
            if(flag == 0){
                return 0;
            }
        }

        // 插入 t_metadata_rel 中间表（轮播图）
        if (data.getCarouselImgUrls() != null && goodsCarouselRecordID != 0L) {
            flag = this.updateGoodsCarouselImage(data.getCarouselImgUrls(), goodsCarouselRecordID);
            if(flag == 0){
                return 0;
            }
        }
        return 1;
    }

    //更新商品操作中，更新商品的图片, 返回t_metadata_rel中的recordId
    private int updateGoodsImage(Long[] newResourcesIdList, Long oldRecordId){
         deleteFileRefById(oldRecordId);
        return insertImg(newResourcesIdList,oldRecordId);
    }

    //更新商品操作中，更新商品的图片, 返回t_metadata_rel中的recordId
    private int updateGoodsCarouselImage(Long[] newResourcesIdList, Long oldRecordId){
        deleteFileRefById(oldRecordId);
        return insertCarouseImg(newResourcesIdList,oldRecordId);
    }

    private void deleteFileRefById(Long oldRecordId){
        FileRef fileRef=new FileRef();
        fileRef.setRefId(oldRecordId);
        Long[] list = fileRefMapper.queryFileRefByRefId(fileRef);
        for(int i=0;i<list.length;i++){
            fileRefMapper.deleteResourceByRefId(list[i]);
        }
    }

}
