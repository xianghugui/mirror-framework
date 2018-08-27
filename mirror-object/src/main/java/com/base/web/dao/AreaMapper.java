package com.base.web.dao;

import com.base.web.bean.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Geek、
 * @Date: Created in 13:59  2018/3/27
 */
public interface AreaMapper extends GenericMapper<Area,String>{


    List<Area> queryAllviewTree();

    //改变显示状态
    int changestatus(@Param("uId") String uId, @Param("status") String status);


    //查询省份
    List<Area> queryProvince();

    //查询市
    List<Area> queryCity(@Param("uId") String uId);

    //查询区
    List<Area> queryArea(@Param("uId") String uId, @Param("status") Boolean status);

    //根据父id查询节点id
    List<Area> queryAreaByParentId(Long parentId);
}
