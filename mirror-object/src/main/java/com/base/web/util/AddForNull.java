package com.base.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 统计数据补空
 */
public class AddForNull {

    public List<Map> addNull(List<Map> list, String selectTimeStr, String selectType) {
        if (!"3".equals(selectType) && list.size() > 0) {
            //x轴上显示时间个数
            int weekLength = 0;
            Date nowTime = new Date();
            Date selectTime = null;
            Calendar selectTimeCal = Calendar.getInstance();
            Calendar nowTimeCal = Calendar.getInstance();
            String timeType = "yyyy";
            Integer dayForSaturday = 0;
            Integer dayForMonth = 0;

            if ("0".equals(selectType)) {
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
            if ("0".equals(selectType)) {
                //统计显示全部周数
                weekLength = selectTimeCal.getActualMaximum(Calendar.WEEK_OF_MONTH) - 1;

                //统计仅显示到当前周数
                if (nowTimeCal.get(Calendar.YEAR) == selectTimeCal.get(Calendar.YEAR)
                        && nowTimeCal.get(Calendar.MONTH) == selectTimeCal.get(Calendar.MONTH)) {
                    weekLength = nowTimeCal.get(Calendar.WEEK_OF_MONTH)-1;
                }
                //判断该月的第一个星期六是几号
                while(selectTimeCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                    selectTimeCal.add(Calendar.DATE, 1);
                }
                dayForMonth = selectTimeCal.get(Calendar.MONTH) + 1;
                dayForSaturday = selectTimeCal.get(Calendar.DAY_OF_MONTH);
            }

            //获取当前时间的月份
            else if ("1".equals(selectType)) {
                weekLength = nowTimeCal.get(Calendar.MONTH) + 1;
                if (nowTimeCal.get(Calendar.YEAR) > selectTimeCal.get(Calendar.YEAR)) {
                    weekLength = 12;
                }
            }

            //获取当前时间的季度
            else if ("2".equals(selectType)) {
                weekLength = getSeason(nowTime);
                if (nowTimeCal.get(Calendar.YEAR) > selectTimeCal.get(Calendar.YEAR)) {
                    weekLength = 4;
                }
            }

            //获取当前时间是一年中的第几周
            else if ("4".equals(selectType)) {
                weekLength = nowTimeCal.get(Calendar.WEEK_OF_YEAR) - 1;
                if (nowTimeCal.get(Calendar.YEAR) > selectTimeCal.get(Calendar.YEAR)) {
                    weekLength = 52;
                }
            }

            String[] timeArray;
            String[] salesArray;
            String[] pageViewArray = new String[]{};
            int weekNum;
            int selectItemIndex;
            StringBuffer newTimeArray;
            StringBuffer newSales;
            StringBuffer newPageView = null;

            for (Map selectItem : list) {
                newTimeArray = new StringBuffer();
                newSales = new StringBuffer();
                selectItemIndex = 0;
                //读取查询数据
                timeArray = selectItem.get("createTime").toString().split(",");
                salesArray = selectItem.get("sales").toString().split(",");
                if (selectItem.get("pageView") != null) {
                    newPageView = new StringBuffer();
                    pageViewArray = selectItem.get("pageView").toString().split(",");
                }

                for (weekNum = 1; weekNum <= weekLength; weekNum++) {

                    //拼接显示时间
                    if ("0".equals(selectType)) {
                        newTimeArray.append(dayForMonth+"-"+dayForSaturday + ",");
                        dayForSaturday = dayForSaturday + 7;
                    }
                    else if ("1".equals(selectType)) {
                        newTimeArray.append(weekNum + "月,");
                    }
                    else if ("2".equals(selectType)) {
                        newTimeArray.append(weekNum + "季,");
                    }
                    else if ("4".equals(selectType)) {
                        newTimeArray.append(weekNum + "周,");
                    }

                    // 拼接每周的销量和浏览量,空的补零
                    if (selectItemIndex < timeArray.length && timeArray[selectItemIndex]
                            .equals(String.valueOf(weekNum))) {

                        newSales.append(salesArray[selectItemIndex] + ",");
                        if (selectItem.get("pageView") != null) {
                            newPageView.append(pageViewArray[selectItemIndex] + ",");
                        }
                        selectItemIndex++;

                    } else {
                        newSales.append("0,");
                        if (selectItem.get("pageView") != null) {
                            newPageView.append("0,");
                        }
                    }
                }
                selectItem.put("sales", newSales);
                if (selectItem.get("pageView") != null) {
                    selectItem.put("pageView", newPageView);
                }
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
