package com.base.web.dao;

import com.base.web.bean.VideoUser;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 9:51  2018/3/28
 */
public interface VideoUserMapper extends GenericMapper<VideoUser, Long> {

    /**
     * 查询没有关联用户的所有视频
     * @return
     */
    List<Map> selectNoBelongToVideoInfo();

    /**
     * 根据视频图片关联id查询当前资源
     * @param recordID
     * @return
     */
    List<Map> selectImgUrl(String recordID);

    /**
     * 用户分页查询自己的试衣视频（没有删除的）
     * @param queryParam
     * @return
     */
    List<Map> userVideoList(QueryParam queryParam);

    /**
     * 用户分页查询自己的试衣视频总数（没有删除的）
     * @param userId
     * @return
     */
    int userVideoListTotal(Long userId);

    /**
     * 根据关联id查询视频图片
     * @param recordId
     * @return
     */
     Map selectVideoImageUrl (Long recordId);
    /**
     * 根据关联id查询视频
     * @param recordId
     * @return
     */
    Map selectVideoUrl (Long recordId);

    Map shareVideo (Long videoId);

    /**
     * 分页查询用户视频秀
     * @param queryParam
     * @return
     */
    List<Map> userVideoShowList(QueryParam queryParam);

    /**
     * 用户视频秀总数
     * @param userId
     * @return
     */
    Integer userVideoShowListTotal(Long userId);

    /**
     * 分页查询视频秀
     * @param queryParam
     * @return
     */
    List<Map> allVideoShowList(QueryParam queryParam);

    /**
     * 视频秀总数
     * @return
     */
    Integer allVideoShowListTotal(QueryParam queryParam);

    /**
     * 分页查询商品视频秀
     * @param queryParam
     * @return
     */
    List<Map> goodsVideoShowList(QueryParam queryParam);

    /**
     * 商品视频秀总数
     * @return
     */
    Integer goodsVideoShowListTotal(QueryParam queryParam);

    /**
     * 分页查询用户视频秀产生成功后交易分佣记录
     * @param queryParam
     * @return
     */
    List<Map> myVideoShowBuyRecordByPager(QueryParam queryParam);

    /**
     * 视频秀成功交易数量及当前用户总的秀分佣金额
     * @return
     */
    Map myVideoShowBuyRecordTotalAndTotalCommission(QueryParam queryParam);

    /**
     * 购买返现记录分页
     * @param queryParam
     * @return
     */
    List<Map> myBuyCashBashRecordByPager(QueryParam queryParam);

    /**
     * 购买返现总记录数和总的购买返现金额
     * @return
     */
    Map myBuyCashBashRecordTotalAndTotalCashBash(QueryParam queryParam);

    /**
     * 关联商品置空
     * @param id
     * @return
     */
    int resetGoodsId(@Param("id") Long id, @Param("userId") Long userId);
}
