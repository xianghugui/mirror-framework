package com.base.web.service.impl;

import com.base.web.bean.PageViewStatisticalMain;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.dao.BrandMapper;
import com.base.web.dao.PageViewStatisticalMainMapper;
import com.base.web.dao.PageViewStatisticalViceMapper;
import com.base.web.service.PageViewStatisticalMainService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
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
@Service("PageViewStatisticalMainService")
public class PageViewStatisticalMainServiceImpl extends AbstractServiceImpl<PageViewStatisticalMain, Long> implements PageViewStatisticalMainService {

    @Resource
    private PageViewStatisticalMainMapper pageViewStatisticalMainMapper;

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private PageViewStatisticalViceMapper pageViewStatisticalViceMapper;

    @Override
    protected PageViewStatisticalMainMapper getMapper() {
        return this.pageViewStatisticalMainMapper;
    }


    /**
     * 按周统计品牌销量
     * 执行时间每周的星期日
     */
    @Scheduled(cron = "0 23 9 ? * *")
    public void addWeekJob() {
        PageViewStatisticalMain pageViewStatisticalMain = new PageViewStatisticalMain();
        List<Map> brandList = brandMapper.queryAllBrand();
        List<Map> queryList;
        Long sales = 0L;
        Long pageView = 0L;
        for (Map brand : brandList) {
            queryList = pageViewStatisticalViceMapper.queryPageViewAndSales((Long) brand.get("u_id"));
            if (queryList.size() > 0) {
                pageViewStatisticalMain.setId(GenericPo.createUID());
                //添加各门店的销量
                for (Map item : queryList) {
                    item.put("id", GenericPo.createUID());
                    item.put("viceId", pageViewStatisticalMain.getId());
                    sales +=  Long.valueOf(item.get("sales").toString());
                    pageView += Long.valueOf(item.get("pageView").toString());
                }
                pageViewStatisticalViceMapper.insertPageViewForShop(queryList);
                pageViewStatisticalMain.setSales(sales);
                pageViewStatisticalMain.setPageView(pageView);
                pageViewStatisticalMain.setTimeFrame(0);
                pageViewStatisticalMain.setBrandId((Long) brand.get("u_id"));
                pageViewStatisticalMain.setCreateTime(new Date());
                insert(pageViewStatisticalMain);
            }
        }
        System.out.println("定时按周统计浏览量");
    }

    /**
     * 按月统计品牌销量
     * 执行时间每月的1号
     */
    @Scheduled(cron = "0 24 9 ? * *")
    public void addMonthJob() {
        PageViewStatisticalMain pageViewStatisticalMain = new PageViewStatisticalMain();
        List<Map> queryList = pageViewStatisticalViceMapper.queryMonthPageViewAndSales();
        for (Map item : queryList) {
            pageViewStatisticalMain.setId(GenericPo.createUID());
            pageViewStatisticalMain.setSales((Long) item.get("sales"));
            pageViewStatisticalMain.setPageView((Long) item.get("pageView"));
            pageViewStatisticalMain.setTimeFrame(1);
            pageViewStatisticalMain.setBrandId((Long) item.get("brandId"));
            pageViewStatisticalMain.setCreateTime(new Date());
            insert(pageViewStatisticalMain);
        }
        System.out.println("定时按月统计浏览量");
    }

    /**
     * 按季度统计品牌销量
     * 执行时间每月的1号
     */
    @Scheduled(cron = "0 25 9 ? * * ")
    public void addQuarterJob() {
        PageViewStatisticalMain pageViewStatisticalMain = new PageViewStatisticalMain();
        List<Map> queryList = pageViewStatisticalViceMapper.queryQuarterPageViewAndSales();
        for (Map item : queryList) {
            pageViewStatisticalMain.setId(GenericPo.createUID());
            pageViewStatisticalMain.setSales((Long) item.get("sales"));
            pageViewStatisticalMain.setPageView((Long) item.get("pageView"));
            pageViewStatisticalMain.setTimeFrame(2);
            pageViewStatisticalMain.setBrandId((Long) item.get("brandId"));
            pageViewStatisticalMain.setCreateTime(new Date());
            insert(pageViewStatisticalMain);
        }
        System.out.println("定时按季统计浏览量");
    }

    /**
     * 按年统计品牌销量
     * 执行时间每年的1号
     */
    @Scheduled(cron = "0 26 9 ? * * ")
    public void addYearJob() {
        PageViewStatisticalMain pageViewStatisticalMain = new PageViewStatisticalMain();
        List<Map> queryList = pageViewStatisticalViceMapper.queryYearPageViewAndSales();
        for (Map item : queryList) {
            pageViewStatisticalMain.setId(GenericPo.createUID());
            pageViewStatisticalMain.setSales((Long) item.get("sales"));
            pageViewStatisticalMain.setPageView((Long) item.get("pageView"));
            pageViewStatisticalMain.setTimeFrame(3);
            pageViewStatisticalMain.setBrandId((Long) item.get("brandId"));
            pageViewStatisticalMain.setCreateTime(new Date());
            insert(pageViewStatisticalMain);
        }
        System.out.println("定时按年统计浏览量");
    }

    /**
     *
     * 根据筛选条件查询数据
     * @param param
     * @return
     */
    @Override
    public List<Map> queryPageView(QueryParam param) {

        List<Map> queryList;
        String selectType = (String) param.getParam().get("selectType");
        //根据选择条件判断统计类型: 0,按周 1,按月 2,按季 3,按年 4,按年显示全部周
        if (selectType.equals("0")) {
            param.getParam().put("showTime", "%Y-%m");
        }
        if (selectType.equals("1") || selectType.equals("2") || selectType.equals("4")) {
            param.getParam().put("showTime", "%Y");
        }
        if (param.getParam().get("brandId").equals("")) {
            queryList = getMapper().queryWeek(param);
        } else {
            if (param.getParam().get("shopId").equals("")) {
                //查询某品牌下某服装某类别销量
                queryList = getMapper().queryPageViewForShop(param);
            } else {
                queryList = getMapper().queryPageViewForGoods(param);
            }
        }
        return addForNull(queryList, (String) param.getParam().get("selectTime"), selectType);
    }

    public List<Map> addForNull(List<Map> list, String selectTimeStr, String selectType) {
        if (!selectType.equals("3")) {
            //x轴上显示时间个数
            int weekLength = 0;
            Date nowTime = new Date();
            Date selectTime = null;
            Calendar selectTimeCal = Calendar.getInstance();
            Calendar nowTimeCal = Calendar.getInstance();
            String timeType = "yyyy";

            if (selectType.equals("0")) {
                timeType = "yyyy-MM";
            }
            DateFormat format = new SimpleDateFormat(timeType);

            try {
                selectTime = format.parse(selectTimeStr);
                selectTimeCal.setTime(selectTime);
                nowTimeCal.setTime(nowTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //获取当前时间是该月的第几周
            if (selectType.equals("0")) {
                //统计显示全部周数
                weekLength = selectTimeCal.getActualMaximum(Calendar.WEEK_OF_MONTH);

                //统计仅显示到当前周数
                if (nowTimeCal.get(Calendar.YEAR) == selectTimeCal.get(Calendar.YEAR)
                        && nowTimeCal.get(Calendar.MONTH) == selectTimeCal.get(Calendar.MONTH)) {
                    weekLength = nowTimeCal.getActualMaximum(Calendar.WEEK_OF_MONTH);
                }
            }

            //获取当前时间的月份
            if (selectType.equals("1")) {
                weekLength = nowTimeCal.get(Calendar.MONTH) + 1;
                if (nowTimeCal.get(Calendar.YEAR) > selectTimeCal.get(Calendar.YEAR)) {
                    weekLength = 12;
                }
            }

            //获取当前时间的季度
            if (selectType.equals("2")) {
                weekLength = getSeason(nowTime);
                if (nowTimeCal.get(Calendar.YEAR) > selectTimeCal.get(Calendar.YEAR)) {
                    weekLength = 4;
                }
            }

            //获取当前时间是一年中的第几周
            if (selectType.equals("4")) {
                weekLength = nowTimeCal.get(Calendar.WEEK_OF_YEAR);
                if (nowTimeCal.get(Calendar.YEAR) > selectTimeCal.get(Calendar.YEAR)) {
                    weekLength = 52;
                }
            }

            String[] timeArray;
            String[] salesArray;
            String[] pageViewArray;
            int weekNum;
            int selectItemIndex;
            String newTimeArray;
            String newSales;
            String newPageView;

            for (Map selectItem : list) {
                newTimeArray = "";
                newSales = "";
                newPageView = "";
                selectItemIndex = 0;

                //读取查询数据
                timeArray = selectItem.get("createTime").toString().split(",");
                salesArray = selectItem.get("sales").toString().split(",");
                pageViewArray = selectItem.get("pageView").toString().split(",");

                for (weekNum = 1; weekNum <= weekLength; weekNum++) {

                    //拼接显示时间
                    if (selectType.equals("0") || selectType.equals("4")) {
                        newTimeArray += "第" + weekNum + "周,";
                    }
                    if (selectType.equals("1")) {
                        newTimeArray += +weekNum + "月,";
                    }
                    if (selectType.equals("2")) {
                        newTimeArray += "第" + weekNum + "季,";
                    }

                    // 拼接每周的销量和浏览量,空的补零
                    if (selectItemIndex < timeArray.length && timeArray[selectItemIndex]
                            .equals(String.valueOf(weekNum))) {

                        newSales += salesArray[selectItemIndex] + ",";
                        newPageView += pageViewArray[selectItemIndex] + ",";
                        selectItemIndex++;

                    } else {
                        newPageView += "0,";
                        newSales += "0,";
                    }
                }
                selectItem.put("sales", newSales);
                selectItem.put("pageView", newPageView);
                selectItem.put("createTime", newTimeArray);
            }
        }
        return list;
    }

    /**
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @param date
     * @return
     */
    public static int getSeason(Date date) {

        int season = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }


}
