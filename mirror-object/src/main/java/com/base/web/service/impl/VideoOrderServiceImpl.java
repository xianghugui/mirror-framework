package com.base.web.service.impl;

import com.base.web.bean.VideoOrder;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.dao.VideoOrderMapper;
import com.base.web.dao.VideoUserMapper;
import com.base.web.service.TUserService;
import com.base.web.service.VideoOrderService;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.ResourceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:22  2018/4/10
 */
@Service("VideoOrderService")
public class VideoOrderServiceImpl extends AbstractServiceImpl<VideoOrder, Long> implements VideoOrderService {

    @Resource
    private VideoOrderMapper videoOrderMapper;

    @Resource
    private FileRefService fileRefService;

    @Resource
    private TUserService tUserService;

    @Resource
    private VideoUserMapper videoUserMapper;


    @Override
    public List<Map> showVideoOrder(HttpServletRequest req) {
        List<Map> list = getMapper().showVideoOrder();
        for (Map map : list) {
            if (map.get("updateUser") != null) {
                map.put("updateUser", tUserService.selectByPk((Long) map.get("updateUser")).getName());
            }
            if (map.get("videoSrc") != null) {
                Long recordId = Long.valueOf(map.get("videoSrc").toString());
                //视频对应图片地址
                Map map1 = videoUserMapper.selectVideoImageUrl(recordId);
                map.put("videoImageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map1.get("resourceId")).trim()));
                //视频对应地址
                Map map2 = videoUserMapper.selectVideoUrl(recordId);
                map.put("videoUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map2.get("resourceId")).trim(), ".MP4"));
            }
        }
        return getMapper().showVideoOrder();
    }

    @Override
    public PagerResult showVideoOrders(QueryParam param, HttpServletRequest req) {
        List<Map> list = fileRefService.addVideos(getMapper().showVideoOrders(param), req);
        int total = getMapper().queryVideoOrdersTotal(param);
        return new PagerResult(total, list);
    }

    @Override
    public Map showVideoOrderInfo(String videoOrderId) {
        return getMapper().showVideoOrderInfo(videoOrderId);
    }

    @Override
    public Map showAddress(String videoOrderId) {
        return getMapper().showAddress(videoOrderId);
    }

    @Override
    public int delivery(String videoOrderId, String expressId, String expressNumber) {
        return getMapper().delivery(videoOrderId, expressId, expressNumber);
    }

    @Override
    public int offer(String videoOrderId, String price) {
        return getMapper().offer(videoOrderId, price);
    }

    @Override
    public int lack(String videoOrderId, String lackReason) {
        return getMapper().lack(videoOrderId, lackReason);
    }

    @Override
    public int changeStatus(Long videoOrderId, Integer status) {
        return getMapper().changeStatus(videoOrderId, status);
    }

    @Override
    protected VideoOrderMapper getMapper() {
        return this.videoOrderMapper;
    }

    @Override
    public PagerResult showTrading(QueryParam param) {
        return new PagerResult(getMapper().showTradingTotal(param), getMapper().showTrading(param));
    }

    @Override
    public PagerResult clientShowOrders(QueryParam param, HttpServletRequest req) {
        List<Map> list = getMapper().clientShowOrders(param);
        return new PagerResult(getMapper().clientShowOrdersTotal(param), fileRefService.addVideos(list, req));
    }

}
