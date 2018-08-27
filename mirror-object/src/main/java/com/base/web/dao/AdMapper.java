package com.base.web.dao;

import com.base.web.bean.Ad;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 9:51  2018/3/28
 */
public interface AdMapper extends GenericMapper<Ad, Long> {
//    更改广告状态
    int updateStatusByUId(@Param("uId") Long uId, @Param("status") Integer status);

    //查询全部推送广告,按创建时间排序
    List<Map> queryAllAd(QueryParam param);

    
    Integer queryAllAdTotal(QueryParam param);


    
}
