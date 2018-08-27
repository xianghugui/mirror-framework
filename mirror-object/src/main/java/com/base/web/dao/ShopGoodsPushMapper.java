package com.base.web.dao;

import com.base.web.bean.ShopGoodsPush;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ShopGoodsPushMapper extends GenericMapper<ShopGoodsPush, Long> {
    Map selectOrderByTimePickOne(@Param("userId") Long userId, @Param("shopId") Long shopId);
    //查询当前登录店铺导购情况
  List<Map> queryShopUser(@Param("shopId") Long shopId);

    /**
     * 查询挡墙店铺特定手机号导购员导购用户列表
     * @param phone 导购员手机
     * @param shopId 导购员所在店铺ID
     * @return
     */
    List<Map> queryRecommendUserList(@Param("phone") Long phone, @Param("shopId") Long shopId);


}
