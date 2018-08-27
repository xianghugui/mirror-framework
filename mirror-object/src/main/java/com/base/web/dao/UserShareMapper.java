package com.base.web.dao;

import com.base.web.bean.UserShare;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserShareMapper extends GenericMapper<UserShare, Long>{

    /**
     * 查询用户代言情况会员列表
     * @return
     */
    List<Map> userList(@Param("goodsId") Long goodsId, @Param("parentId") Long parentId);
    List<Map> buyList(@Param("goodsId") Long goodsId, @Param("parentId") Long parentId);

    Map totalMoney(@Param("goodsId") Long goodsId, @Param("parentId") Long parentId);
}
