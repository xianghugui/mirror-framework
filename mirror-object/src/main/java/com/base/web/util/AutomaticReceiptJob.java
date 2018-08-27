package com.base.web.util;

import com.base.web.bean.OrderDetail;
import com.base.web.bean.VideoOrder;
import com.base.web.service.OrderDetailService;
import com.base.web.service.VideoOrderService;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Date;

import static org.quartz.DateBuilder.futureDate;

@Component("orderJob")
public class AutomaticReceiptJob implements Job {
    //7天后自动确认收货
    @Resource
    private OrderDetailService orderDetailService;
    @Resource
    private VideoOrderService videoOrderService;
    @Resource
    private SchedulerUtil schedulerUtil;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        //获取外部数据的方式
        JobDataMap params = jobExecutionContext.getJobDetail().getJobDataMap();
        Long id = Long.valueOf(params.get("id").toString());
        Integer type = Integer.valueOf(params.get("type").toString());
        if (0 == type) {
            OrderDetail orderDetail = orderDetailService.selectByPk(id);
            //购物订单
            if(orderDetail.getStatus() == 3){
                orderDetail.setShouhuoTime(new Date());
                orderDetail.setStatus(4);
                orderDetailService.update(orderDetail);
                schedulerUtil.removeJob(id.toString(),SchedulerUtil.ORDER);
                schedulerUtil.addJob(id.toString(), SchedulerUtil.ORDER, params, EvaluationOrderJob.class,
                        futureDate(5, DateBuilder.IntervalUnit.MINUTE));
            }
        } else if (2 == type) {
            VideoOrder videoOrder = videoOrderService.selectByPk(id);
            if(videoOrder.getStatus() == 4){
                videoOrder.setRemindTime(new Date());
                videoOrder.setStatus(5);
                videoOrderService.update(videoOrder);
                schedulerUtil.removeJob(id.toString(),SchedulerUtil.VIDEOORDER);
                schedulerUtil.addJob(id.toString(), SchedulerUtil.VIDEOORDER, params, EvaluationOrderJob.class,
                        futureDate(5, DateBuilder.IntervalUnit.MINUTE));
            }
        }
    }
}
