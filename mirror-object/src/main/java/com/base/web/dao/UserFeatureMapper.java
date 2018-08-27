package com.base.web.dao;

import com.base.web.bean.UserFeature;

import java.util.List;
import java.util.Map;


/**
 * @Author: Geek、
 * @Date: Created in 13:59  2018/3/27
 */
public interface UserFeatureMapper extends GenericMapper<UserFeature,Long>{
    //>>查询用户身材信息
    Map queryUserFeature(Long userId);

}
