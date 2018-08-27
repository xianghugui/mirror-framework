package com.base.web.dao;

import com.base.web.bean.UserAgent;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 13:59  2018/3/27
 */
public interface UserAgentMapper extends GenericMapper<UserAgent,Long>{


    /**
     * 根据用户id分页查询用户的代理情况
     * @param queryParam
     * @return
     */
    List<Map> userAgentGoodsListPagerByUserId(QueryParam queryParam);

    /**
     * 查询用户代理情况总数
     * @param queryParam
     * @return
     */
   Integer  totalUserAgentGoods(QueryParam queryParam);

    /**
     * 代理商根据商品id推广自己代理商品
     * @param goodsId
     * @return
     */
   Map userAgentGoodsSpread (Long goodsId);

   int updateAgent(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
}
