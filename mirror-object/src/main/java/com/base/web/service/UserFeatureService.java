package com.base.web.service;

import com.base.web.bean.UserFeature;

import java.util.Map;


/**
 * @Author: Geek、
 * @Date: Created in 14:07  2018/3/27
 */
public interface UserFeatureService extends GenericService<UserFeature, Long> {
    //>>查询用户身材信息
    Map queryUserFeature();

}
