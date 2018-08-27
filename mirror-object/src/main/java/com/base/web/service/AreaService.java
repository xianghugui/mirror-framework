package com.base.web.service;

import com.base.web.bean.Area;

import java.util.List;

/**
 * @Author: Geek、
 * @Date: Created in 14:07  2018/3/27
 */
public interface AreaService extends GenericService<Area, String> {

    //店铺管理页面区域树的显示
    List<Area> queryAllviewTree();


    //改变显示状态
    int changestatus(String uId,String status);

    //查询省份
    List<Area> queryProvince();

    //查询市
    List<Area> queryCity(String uId);

    //查询区
    //true:查询状态为0的，即还未添加的区域
    //false：查询所有
    List<Area> queryArea(String uId, Boolean status);

    //查询所有区域包括商铺
    List<Area> queryAll();
}
