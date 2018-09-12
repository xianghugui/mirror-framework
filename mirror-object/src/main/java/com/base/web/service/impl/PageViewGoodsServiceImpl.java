package com.base.web.service.impl;

import com.base.web.bean.PageViewGoods;
import com.base.web.bean.po.GenericPo;
import com.base.web.dao.PageViewGoodsMapper;
import com.base.web.service.PageViewGoodsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    protected PageViewGoodsMapper getMapper() {
        return this.pageViewGoodsMapper;
    }


    /**
     * 按周统计试衣秀视频服装的浏览量和销量
     * 执行时间每周的星期六
     */
    @Scheduled(cron = "0 29 23 ? * SAT")
//    @Scheduled(cron = "0 10 19,20,21,22 * * ?")
    @Async
    public void addWeekJob() {
        List<Map> queryList = pageViewGoodsMapper.queryWeeklyPageView();
        for (Map item : queryList) {
            item.put("id", GenericPo.createUID());
            item.put("timeFrame", 0);
        }
        pageViewGoodsMapper.insertPageViewForGoods(queryList);
        System.out.println("定时按周统计服装的浏览量");
    }

    /**
     * 按月统计
     * 执行时间每月的最后一日
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @Async
    public void addMonthJob() {
        List<Map> queryList = pageViewGoodsMapper.queryMonthlyPageView();
        for (Map item : queryList) {
            item.put("id", GenericPo.createUID());
            item.put("timeFrame", 1);
        }
        pageViewGoodsMapper.insertPageViewForGoods(queryList);
        System.out.println("定时按月统计服装的浏览量");
    }

    /**
     * 按季度统计
     * 每季度的最后一天
     */
    @Scheduled(cron = "0 0 0 1 3,6,9,12 ?")
    @Async
    public void addQuarterJob() {
        List<Map> queryList = pageViewGoodsMapper.queryQuarterlyPageView();
        for (Map item : queryList) {
            item.put("id", GenericPo.createUID());
            item.put("timeFrame", 2);
        }
        pageViewGoodsMapper.insertPageViewForGoods(queryList);
        System.out.println("定时按季统计服装的浏览量");
    }

    /**
     * 按年统计
     * 执行时间每年的12月最后一天
     */
    @Scheduled(cron = "0 0 0 1 1 ?")
    @Async
    public void addYearJob() {
        List<Map> queryList = pageViewGoodsMapper.queryYearlyPageView();
        for (Map item : queryList) {
            item.put("id", GenericPo.createUID());
            item.put("timeFrame", 3);
        }
        pageViewGoodsMapper.insertPageViewForGoods(queryList);
        System.out.println("定时按年统计服装的浏览量");
    }


}
