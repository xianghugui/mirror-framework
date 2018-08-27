package com.base.web.util;

import com.base.web.bean.OrderDetail;
import com.base.web.bean.VideoOrder;
import com.base.web.service.GoodsSpeService;
import com.base.web.service.OrderDetailService;
import com.base.web.service.OrderService;
import com.base.web.service.VideoOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class CancelOrderJob implements Job {
    @Resource
    private OrderDetailService orderDetailService;
    @Resource
    private VideoOrderService videoOrderService;
    @Resource
    private GoodsSpeService goodsSpeService;

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) {
        //获取外部数据的方式
        JobDataMap params = jobExecutionContext.getJobDetail().getJobDataMap();
        Long id = Long.valueOf(params.get("id").toString());
        Integer type = Integer.valueOf(params.get("type").toString());
        if (0 == type) {
            //购物订单
            OrderDetail orderDetail = orderDetailService.selectByPk(id);
            //恢复商品数量
            goodsSpeService.updateGoodsSpecQuality(orderDetail.getGoodsSpecId(), orderDetail.getNum());
            orderDetail.setStatus(7);
            orderDetailService.update(orderDetail);
        } else if (2 == type) {
            //视频订单
            VideoOrder videoOrder = new VideoOrder();
            videoOrder.setId(id);
            videoOrder.setStatus(6);
            videoOrderService.update(videoOrder);
        }
    }
}
