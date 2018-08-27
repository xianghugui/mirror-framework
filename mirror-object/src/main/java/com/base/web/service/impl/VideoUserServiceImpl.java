package com.base.web.service.impl;


import com.base.web.bean.VideoOrder;
import com.base.web.bean.VideoUser;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.VideoUserMapper;
import com.base.web.service.GoodsService;
import com.base.web.service.VideoOrderService;
import com.base.web.service.VideoUserService;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.ResourceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 10:03  2018/3/28
 */
@Service("VideoUserService")
public class VideoUserServiceImpl extends AbstractServiceImpl<VideoUser, Long> implements VideoUserService{

    @Resource
    private VideoUserMapper VideoUserMapper;

    @Resource
    private VideoOrderService videoOrderService;

    @Resource
    private FileRefService fileRefService;

    @Resource
    private GoodsService goodsService;

    @Override
    protected VideoUserMapper getMapper() {
        return this.VideoUserMapper;
    }

    @Override
    public List<Map> selectNoBelongToVideoInfo() {
        return getMapper().selectNoBelongToVideoInfo();
    }

    @Override
    public List<Map> selectImgUrl(String recordID) {
        return getMapper().selectImgUrl(recordID);
    }

    @Override
    public PagerResult<Map> userVideoList(QueryParam queryParam , HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        PagerResult<Map> pagerResult = new PagerResult<>();
        queryParam.getParam().put("userId", userId);
        int total = getMapper().userVideoListTotal(userId);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            List<Map> mapList = getMapper().userVideoList(queryParam);
            if(mapList.size() > 0){
                String type = ".MP4";
                for(Map map : mapList){
                    Long recordId =  Long.valueOf(map.get("recordId").toString());
                   //视频对应图片地址
                   Map map1 = VideoUserMapper.selectVideoImageUrl(recordId);
                    //视频对应地址
                    Map map2 = VideoUserMapper.selectVideoUrl(recordId);
                    VideoOrder videoOrder = videoOrderService.createQuery().where(VideoOrder.Property.videoId,map.get("videoId")).single();
                    if(videoOrder == null ){
                        map.put("isConsultPrice", true );
                    }else {
                        map.put("isConsultPrice", false );
                    }

                    map.put("videoUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map2.get("resourceId")).trim(), type));
                    map.put("videoImageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map1.get("resourceId")).trim()));
                    //查询关联的商品图片
                    if(map.get("imagePath") != null){
                        queryParam.getParam().put("dataType", 2);
                        queryParam.getParam().put("recordId", map.get("imagePath"));
                        List<Map> imgs = fileRefService.queryResourceByRecordId(queryParam, req);
                        map.put("imagePath", imgs);
                    }
                }
            }
            pagerResult.setData(mapList);
        }
        return pagerResult;
    }

    @Override
    public Map shareVideo(Long videoId, HttpServletRequest req) {
        Map shareVideo = getMapper().shareVideo(videoId);
        //视频对应图片地址
        String type = ".MP4";
        Map map1 = VideoUserMapper.selectVideoImageUrl((Long) shareVideo.get("recordId"));
        //视频对应地址
        Map map2 = VideoUserMapper.selectVideoUrl((Long) shareVideo.get("recordId"));
        shareVideo.put("videoUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map1.get("md5")).trim(), type));
        shareVideo.put("videoImageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map2.get("md5")).trim()));
        return shareVideo;
    }

    @Override
    public PagerResult<Map> userVideoShowList(QueryParam queryParam, HttpServletRequest req) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        queryParam.getParam().put("userId", WebUtil.getLoginUser().getId());
        Integer total = getMapper().userVideoShowListTotal(WebUtil.getLoginUser().getId());
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            List<Map> mapList = getMapper().userVideoShowList(queryParam);
            if(mapList.size() > 0){
                String type = ".MP4";
                for(Map map : mapList){
                    Long recordId =  Long.valueOf(map.get("recordId").toString());
                    //视频对应图片地址
                    FileRef fileRef = fileRefService.createQuery()
                            .where(FileRef.Property.refId,map.get("imageId").toString())
                            .and(FileRef.Property.dataType,2)
                            .single();

                    if(fileRef !=null){
                        map.put("goodsImageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(fileRef.getResourceId()).trim()));
                    }else {
                    FileRef fileRef1 = fileRefService.createQuery()
                            .where(FileRef.Property.refId,map.get("imageId").toString())
                            .and(FileRef.Property.dataType,3)
                            .single();
                        map.put("goodsImageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(fileRef1.getResourceId()).trim()));
                    }
                    Map map1 = VideoUserMapper.selectVideoImageUrl(recordId);
                    //视频对应地址
                    Map map2 = VideoUserMapper.selectVideoUrl(recordId);
                    map.put("videoUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map2.get("resourceId")).trim(), type));
                    map.put("videoImageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map1.get("resourceId")).trim()));
                }
            }
            pagerResult.setData(mapList);
        }
        return pagerResult;
    }

    @Override
    public PagerResult<Map> allVideoShowList(QueryParam queryParam, HttpServletRequest req) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        Integer total = getMapper().allVideoShowListTotal(queryParam);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            List<Map> mapList = getMapper().allVideoShowList(queryParam);
            if(mapList.size() > 0){
                String type = ".MP4";
                for(Map map : mapList){
                    Long recordId =  Long.valueOf(map.get("recordId").toString());
                    //视频对应地址
                    Map map2 = VideoUserMapper.selectVideoUrl(recordId);
                    //视频对应图片地址
                    Map map1 = VideoUserMapper.selectVideoImageUrl(recordId);
                    map.put("videoImageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map1.get("resourceId")).trim()));
                    map.put("videoUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map2.get("resourceId")).trim(), type));

                    //商品图片
                    Map goodsImage = goodsService.queryGoodsImgSrcById(map.get("goodsId").toString());
                    map.put("goodsImageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(goodsImage.get("image")).trim()));

                }
            }
            pagerResult.setData(mapList);
        }
        return pagerResult;
    }

    @Override
    public PagerResult<Map> goodsVideoShowList(QueryParam queryParam, HttpServletRequest req) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        Integer total = getMapper().goodsVideoShowListTotal(queryParam);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            List<Map> mapList = getMapper().goodsVideoShowList(queryParam);
            if(mapList.size() > 0){
                String type = ".MP4";
                for(Map map : mapList){
                    Long recordId =  Long.valueOf(map.get("recordId").toString());
                    //视频对应图片地址
                    Map map1 = VideoUserMapper.selectVideoImageUrl(recordId);
                    map.put("videoImageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map1.get("resourceId")).trim()));
                    //视频对应地址
                    Map map2 = VideoUserMapper.selectVideoUrl(recordId);
                    map.put("videoUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map2.get("resourceId")).trim(), type));

                }
            }
            pagerResult.setData(mapList);
        }
        return pagerResult;
    }

    @Override
    public PagerResult<Map> tradeRecordPagerList(QueryParam queryParam, Integer type) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        //购买返现总记录数和总的购买返现金额
        Map totalCashBash = getMapper().myBuyCashBashRecordTotalAndTotalCashBash(queryParam);
        //视频秀成功交易数量及当前用户总的秀分佣金额
        Map totalCommission = getMapper().myVideoShowBuyRecordTotalAndTotalCommission(queryParam);
        BigDecimal totalCommission1 = new BigDecimal(0.00);
        BigDecimal totalCashBash1 = new BigDecimal(0.00);
        if(totalCashBash != null) {
            totalCashBash1 = new BigDecimal(totalCashBash.get("totalCashBach").toString());
        }
        if(totalCommission != null) {
            totalCommission1 = new BigDecimal(totalCommission.get("totalCommission").toString());
        }
        BigDecimal totalRecord = totalCashBash1.add(totalCommission1);
        if(type == 1){//视频秀分佣列表及总收益
            pagerResult.setTotal(Integer.parseInt(totalCommission.get("total").toString()));

            //数据为空时,只返回总收益
            if (Integer.parseInt(totalCommission.get("total").toString()) == 0) {
                pagerResult.setData(this.totalRecord(totalRecord));
            } else {
                List<Map> mapList = getMapper().myVideoShowBuyRecordByPager(queryParam);
                mapList.get(0).put("totalRecord",totalRecord);
                pagerResult.setData(mapList);
            }
        }else {//购买记录返现列表及总收益
            pagerResult.setTotal(Integer.parseInt(totalCashBash.get("total").toString()));
            if (Integer.parseInt(totalCashBash.get("total").toString()) == 0) {
                pagerResult.setData(this.totalRecord(totalRecord));
            } else {
                List<Map> mapList = getMapper().myBuyCashBashRecordByPager(queryParam);
                mapList.get(0).put("totalRecord",totalRecord);
                pagerResult.setData(mapList);
            }
        }
        return pagerResult;
    }

    @Override
    public int resetGoodsId(Long id, Long userId) {
        return getMapper().resetGoodsId(id, userId);
    }

    public ArrayList totalRecord(BigDecimal totalRecord){
        Map list = new HashMap();
        list.put("totalRecord",totalRecord);
        ArrayList array = new ArrayList<>();
        array.add(list);
        return array;
    }
}
