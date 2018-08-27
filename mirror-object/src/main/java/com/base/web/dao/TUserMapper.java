package com.base.web.dao;

import com.base.web.bean.TUser;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 10:41  2018/3/29
 */
public interface TUserMapper extends GenericMapper<TUser, Long>{

    List<Map> addshopquerytuser(String shopId);

    List<Map> queryTuser();

    int changeStatus(@Param("status") String status, @Param("tuserId") String tuserId);

    Long returnroleId(String roleName);

    /**
     * 根据用户id查询当前用户的个人主页基本信息
     * @param userId
     * @return
     */
    Map userHome(Long userId);

    Map queryTUserById(Object uId);

    //查询用户试穿次数
    Integer queryResidueDegree(Long uId);

    int updateResidueDegree(@Param("degree") Integer degree,@Param("uId") Long uId);

    /**
     * 更新余额,earn:需要增加或减少的余额
     * @param earn
     * @param userId
     * @return
     */
    int updateEarn(@Param("earn") BigDecimal earn,@Param("userId") Long userId);

    int confirmPassword(@Param("userId")String userId, @Param("password")String password);
    int changePassword(@Param("userId")String userId, @Param("password")String password);

    //>>根据（父级会员）查询用户
    List<Map> queryUserByPId(Long parentId);

    //>>查询用户余额
    BigDecimal queryEarn(Long uId);


}
