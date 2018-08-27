package com.base.web.controller.api;


import com.base.web.bean.LikeRecord;
import com.base.web.bean.SearchBean;
import com.base.web.bean.VideoUser;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.GoodsService;
import com.base.web.service.LikeRecordService;
import com.base.web.service.VideoUserService;
import com.base.web.service.resource.FileRefService;
import io.swagger.annotations.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(value = "RevisedApiControllers", description = "改版后客户端接口")
@RequestMapping("/api/revised/")
@RestController
public class RevisedApiControllers {

    /**
     * 用户服务类
     */
    @Resource
    private LikeRecordService likeRecordService;

    @Resource
    private GoodsService goodsService;
    @Resource
    private FileRefService fileRefService;
    @Resource
    private VideoUserService videoUserService;

    @RequestMapping(value = "like/{videoId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "videoId",
                    value = "视频id", required = true)

    })
    @ApiOperation(value = "视频秀点赞或取消点赞")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage queryLikeOrNot(@PathVariable Long videoId) {
        LikeRecord likeRecord = likeRecordService.createQuery()
                .where(LikeRecord.Property.videoGoodsId, videoId)
                .and(LikeRecord.Property.userId, WebUtil.getLoginUser().getId())
                .single();
        VideoUser videoUser = videoUserService.createQuery()
                .where(VideoUser.Property.videoId, videoId)
                .single();
        if (likeRecord != null && videoUser.getLikeNum() > 0) {
            likeRecordService.delete(likeRecord.getId());
            videoUser.setLikeNum(videoUser.getLikeNum() - 1);
            videoUserService.update(videoUser);

        } else {
            LikeRecord like = new LikeRecord();
            like.setUserId(WebUtil.getLoginUser().getId());
            like.setId(GenericPo.createUID());
            like.setVideoGoodsId(videoId);
            if (videoUser.getLikeNum() == null) {
                videoUser.setLikeNum(1);
            } else {
                videoUser.setLikeNum(videoUser.getLikeNum() + 1);
            }
            likeRecordService.insert(like);
            videoUserService.update(videoUser);
        }
        return ResponseMessage.ok();

    }

    @RequestMapping(value = "queryVideoAssociationGoods/{shopId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "shopId",
                    value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "Long", name = "classId",
                    value = "类别id", required = false),
            @ApiImplicitParam(paramType = "form", dataType = "Long", name = "brandId",
                    value = "品牌id", required = false)

    })
    @ApiOperation(value = "视频关联商品分页查询", notes = "返回data为 list Map｛<br>" +
            "goodsName：商品名称<br>" +
            "price：价格<br>" +
            "'imageId'：图片关联id<br>" +
            "cashBach：返现<br>" +
            "goodsId：商品id<br>" +
            "sales：销量<br>" +
            "imagePath：资源路径<br>" +

            "｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage queryVideoAssociationGoods(@PathVariable Long shopId,
                                                      @RequestParam(value = "classId", defaultValue = "0") Long classId,
                                                      @RequestParam(value = "brandId", defaultValue = "0") Long brandId,
                                                      @RequestParam(value = "searchStr", defaultValue = "") String searchStr,
                                                      HttpServletRequest req, QueryParam queryParam) {
        queryParam.getParam().put("shopId", shopId);
        queryParam.getParam().put("classId", classId);
        queryParam.getParam().put("brandId", brandId);
        queryParam.getParam().put("searchStr", searchStr);
        PagerResult<Map> goodsList = goodsService.queryVideoAssociationGoods(queryParam);

        for (Map aGoodsList : goodsList.getData()) {
            queryParam.getParam().put("dataType", 2);
            queryParam.getParam().put("recordId", aGoodsList.get("imageId"));
            List<Map> imageList = fileRefService.queryResourceByRecordId(queryParam, req);
            aGoodsList.put("imagePath", imageList);
        }
        return ResponseMessage.ok(goodsList);
    }

    @RequestMapping(value = "videoAssociationGoods/{videoId}/{goodsId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "videoId",
                    value = "视频id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "goodsId",
                    value = " 关联商品id", required = true)

    })
    @ApiOperation(value = "视频关联商品")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage videoAssociationGoods(@PathVariable Long videoId, @PathVariable Long goodsId) {
        VideoUser videoUser = videoUserService.createQuery()
                .where(VideoUser.Property.videoId, videoId)
                .and(VideoUser.Property.userId, WebUtil.getLoginUser().getId())
                .single();
        if (videoUser != null) {
            videoUser.setGoodsId(goodsId);
            videoUser.setAssociatedTime(new Date());
            videoUserService.update(videoUser);
            return ResponseMessage.ok();
        }
        return ResponseMessage.error("数据操作出现异常");
    }


    @RequestMapping(value = "userVideoShowList", method = RequestMethod.GET)
    @ApiOperation(value = "视频秀分页查询", notes = "返回data为 list Map｛<br>" +
            "videoId：视频id<br>" +
            "recordId：关联id<br>" +
            "'likeNum'：点赞数<br>" +
            "videoUrl：视频路径<br>" +
            "userId：用户Id<br>" +
            "commission：视频秀所得利润<br>" +
            "videoImageUrl：视频图片路径<br>" +
            "shopName：店名<br>" +
            "latitude：维度<br>" +
            "longtitude：精度<br>" +
            "goodsId：商品id<br>" +
            "goodsName：商品名<br>" +
            "｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage userVideoShowList(HttpServletRequest req, QueryParam queryParam) {
        queryParam.getParam().put("userId", WebUtil.getLoginUser().getId());
        PagerResult<Map> goodsList = videoUserService.userVideoShowList(queryParam, req);
        return ResponseMessage.ok(goodsList);
    }

    @RequestMapping(value = "allVideoShowList", method = RequestMethod.GET)
    @ApiOperation(value = "视频秀分页查询", notes = "返回data为 list Map｛<br>" +
            "videoId：视频id<br>" +
            "recordId：关联id<br>" +
            "'likeNum'：点赞数<br>" +
            "videoUrl：视频路径<br>" +
            "commission：视频秀所得利润<br>" +
            "videoImageUrl：视频图片路径<br>" +
            "avatar：头像<br>" +
            "userName：用户名<br>" +
            "userId：用户Id<br>" +
            "goodsName：商品名<br>" +
            "latitude：维度<br>" +
            "longtitude：精度<br>" +
            "shopName：店名<br>" +
            "goodId：商品id<br>" +

            "｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage allVideoShowList(HttpServletRequest req, QueryParam queryParam, SearchBean searchBean) {
        queryParam.getParam().put("userId", WebUtil.getLoginUser().getId());
        queryParam.getParam().put("searchStr", searchBean.getSearchStr());
//        服装类别
        if (searchBean.getLevel() == 1) {//查全部  classId === 1
            queryParam.getParam().put("classId", 1);
        } else {
            queryParam.getParam().put("classId", searchBean.getClassId().toString().substring(0, searchBean.getLevel() * 2));
        }
//        品牌
        if (searchBean.getBrandId() == 1) {//查全部  brandId === 1
            queryParam.getParam().put("brandId", 1);
        } else {
            queryParam.getParam().put("brandId", searchBean.getBrandId());
        }
//        价格
        if (searchBean.getStartPrice() != null && searchBean.getEndPrice() != null && searchBean.getEndPrice().compareTo(searchBean.getStartPrice()) != -1) {
            queryParam.getParam().put("startPrice", searchBean.getStartPrice());

            queryParam.getParam().put("endPrice", searchBean.getEndPrice());
        } else {
            if (searchBean.getStartPrice() == null) {
                searchBean.setStartPrice(BigDecimal.valueOf(0));
            }
            queryParam.getParam().put("startPrice", searchBean.getStartPrice());

            queryParam.getParam().put("endPrice", -1);
        }
        PagerResult<Map> goodsList = videoUserService.allVideoShowList(queryParam, req);
        return ResponseMessage.ok(goodsList);
    }

    @RequestMapping(value = "goodsVideoShowList/{goodsId}", method = RequestMethod.GET)
    @ApiOperation(value = "单个商品视频秀分页查询", notes = "返回data为 list Map｛<br>" +
            "videoId：视频id<br>" +
            "recordId：关联id<br>" +
            "'likeNum'：点赞数<br>" +
            "videoUrl：视频路径<br>" +
            "commission：视频秀所得利润<br>" +
            "videoImageUrl：视频图片路径<br>" +
            "shopName：店名<br>" +
            "userId：用户id<br>" +
            "latitude：维度<br>" +
            "longtitude：精度<br>" +
            "goodsId：商品id<br>" +
            "goodsName：商品名<br>" +
            "｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage goodsVideoShowList(@PathVariable Long goodsId, HttpServletRequest req, QueryParam queryParam) {
        queryParam.getParam().put("goodsId", goodsId);
        queryParam.getParam().put("userId", WebUtil.getLoginUser().getId());
        PagerResult<Map> goodsList = videoUserService.goodsVideoShowList(queryParam, req);
        return ResponseMessage.ok(goodsList);
    }

    @RequestMapping(value = "tradeRecordPagerList/{type}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "type",
                    value = " 切换选项卡 （1 试衣秀收益， 2 购物返现）", required = true)

    })
    @ApiOperation(value = "分页查询当前登陆用户收益记录，总收益，返现分页", notes = "返回data为 list Map｛<br>" +
            "goodsName：商品名称<br>" +
            "createTime：创建时间<br>" +
            "'cashBach'：返现<br>" +
            "commission：分佣<br>" +
            "totalRecord：总收益<br>" +
            "｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage tradeRecordPagerList(@PathVariable Integer type, QueryParam queryParam) {
        queryParam.getParam().put("userId", WebUtil.getLoginUser().getId());
        PagerResult<Map> goodsList = videoUserService.tradeRecordPagerList(queryParam, type);
        return ResponseMessage.ok(goodsList);
    }

    @RequestMapping(value = "addPageView", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "videoId",
                    value = "视频id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "pageView",
                    value = "浏览量", required = true)

    })
    @ApiOperation(value = "视频浏览量")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage pageView(Long videoId,Long postPageView) {
        VideoUser videoUser = videoUserService.createQuery()
                .where(VideoUser.Property.videoId,videoId).single();
        if(videoUser != null){
            if(videoUser.getPageView() == null){
                videoUser.setPageView(0L);
            }
            Long pageView = videoUser.getPageView() + postPageView;
            videoUser.setPageView(pageView);
            videoUserService.update(videoUser);
        }
        return ResponseMessage.ok();

    }


}
