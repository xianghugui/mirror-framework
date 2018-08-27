package com.base.web.util;

import com.base.web.service.OrderDetailService;
import com.base.web.service.VideoOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 订单完成一定时间自动跳过评价
 */
public class EvaluationOrderJob implements Job {

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private VideoOrderService videoOrderService;

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) {
        //获取外部数据的方式
        JobDataMap params = jobExecutionContext.getJobDetail().getJobDataMap();
        Long id = Long.valueOf(params.get("id").toString());
        Integer type = Integer.valueOf(params.get("type").toString());
        if (0 == type) {
            //购物订单
            orderDetailService.changeStatus(id, 5);
            orderDetailService.orderProfit(id);
        } else if (2 == type) {
            //视频订单
            videoOrderService.changeStatus(id, 5);
        }
    }
}
