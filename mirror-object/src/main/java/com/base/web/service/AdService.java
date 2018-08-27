package com.base.web.service;

import com.base.web.bean.Ad;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 10:01  2018/3/28
 */
public interface AdService extends GenericService<Ad,Long> {

    //更改广告状态 0 未发布，1 已发布
    int updateStatusByUId(Long uId,Integer status);

    //查询全部推送广告,按创建时间排序
    PagerResult<Map> queryAllAd(QueryParam param);

}
