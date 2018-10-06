package com.base.web.service.impl;

import com.base.web.bean.PageViewGoods;
import com.base.web.bean.PageViewStatisticalMain;
import com.base.web.bean.po.GenericPo;
import com.base.web.dao.PageViewGoodsMapper;
import com.base.web.service.PageViewGoodsService;
import com.base.web.service.PageViewStatisticalMainService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: FQ
 * @Date: Created in 11:18  2018/8/6
 */
@Configuration
@EnableScheduling
@EnableAsync
@Service("PageViewGoodsService")
public class PageViewGoodsServiceImpl extends AbstractServiceImpl<PageViewGoods, Long> implements PageViewGoodsService {

    @Resource
    private PageViewGoodsMapper pageViewGoodsMapper;

    @Resource
    private PageViewStatisticalMainService pageViewStatisticalMainService;

    @Override
    protected PageViewGoodsMapper getMapper() {
        return this.pageViewGoodsMapper;
    }


    /**
     * 按周统计试衣秀视频服装的浏览量和销量
     * 执行时间每周的星期六
     */
    @Scheduled(cron = "0 29 23 ? * SAT")
    @Async
    public void addWeekJob() {
        int i = 0;
        Date nowTime = new Date();
        List<Map> queryList = pageViewGoodsMapper.queryWeeklyPageView();
        if (queryList != null) {
            for (Map item : queryList) {
                item.put("id", GenericPo.createUID()+i);
                item.put("timeFrame", 0);
                item.put("nowTime",nowTime);
                i++;
            }
            pageViewGoodsMapper.insertPageViewForGoods(queryList);
            pageViewStatisticalMainService.addWeekJob();
        }
        System.out.println("按周统计服装的浏览量");
    }

    /**
     * 按月统计
     * 执行时间每月的最后一日
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @Async
    public void addMonthJob() {
        int i = 0;
        Date nowTime = getNowTime();
        List<Map> queryList = pageViewGoodsMapper.queryMonthlyPageView();
        if (queryList != null) {
            for (Map item : queryList) {
                item.put("id", GenericPo.createUID()+i);
                item.put("timeFrame", 1);
                item.put("nowTime",nowTime);
                i++;
            }
            pageViewGoodsMapper.insertPageViewForGoods(queryList);
        }
        System.out.println("按月统计服装的浏览量");
    }

    /**
     * 按季度统计
     * 每季度的最后一天
     */
    @Scheduled(cron = "0 0 0 1 4,7,10,1 ?")
    @Async
    public void addQuarterJob() {
        int i = 0;
        Date nowTime = getNowTime();
        List<Map> queryList = pageViewGoodsMapper.queryQuarterlyPageView();
        if (queryList != null) {
            for (Map item : queryList) {
                item.put("id", GenericPo.createUID()+i);
                item.put("timeFrame", 2);
                item.put("nowTime",nowTime);
                i++;
            }
            pageViewGoodsMapper.insertPageViewForGoods(queryList);
        }
        System.out.println("按季统计服装的浏览量");
    }

    /**
     * 按年统计
     * 执行时间每年的12月最后一天
     */
    @Scheduled(cron = "0 0 0 1 1 ?")
    @Async
    public void addYearJob() {
        int i = 0;
        Date nowTime = getNowTime();
        List<Map> queryList = pageViewGoodsMapper.queryYearlyPageView();
        if (queryList != null) {
            for (Map item : queryList) {
                item.put("id", GenericPo.createUID()+i);
                item.put("timeFrame", 3);
                item.put("nowTime",nowTime);
                i++;
            }
            pageViewGoodsMapper.insertPageViewForGoods(queryList);
        }
        System.out.println("按年统计服装的浏览量");
    }

    /**
     * 获取上一个月的时间
     * @return
     */
    public Date getNowTime(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date nowTime = c.getTime();
        return nowTime;
    }


}
