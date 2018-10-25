package com.base.web.service;

import com.base.web.bean.VideoUser;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 10:01  2018/3/28
 */
public interface VideoUserService extends GenericService<VideoUser, Long> {

    /**
     * 查询没有关联用户的所有视频
     *
     * @return
     */
    List<Map> selectNoBelongToVideoInfo();

    List<Map> selectImgUrl(String recordID);

    /**
     * 用户分页查询自己的试衣视频（没有删除的）
     *
     * @param queryParam
     * @return
     */
    PagerResult<Map> userVideoList(QueryParam queryParam, HttpServletRequest req);

    /**
     * 根据视频id分享用户试衣视频
     *
     * @param videoId
     * @return
     */
    Map shareVideo(Long videoId, HttpServletRequest req);

    PagerResult<Map> userVideoShowList(QueryParam queryParam, HttpServletRequest req);

    PagerResult<Map> allVideoShowList(QueryParam queryParam, HttpServletRequest req);

    PagerResult<Map> goodsVideoShowList(QueryParam queryParam, HttpServletRequest req);

    /**
     * 用户交易列表
     *
     * @param queryParam
     * @return
     */
    PagerResult<Map> tradeRecordPagerList(QueryParam queryParam, Integer type);

    /**
     * 关联商品置空
     *
     * @param id
     * @return
     */
    int resetGoodsId(Long id, Long userId);
}
