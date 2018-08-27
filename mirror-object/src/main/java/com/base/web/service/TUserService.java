package com.base.web.service;

import com.alibaba.fastjson.JSONObject;
import com.base.web.bean.OrderProfit;
import com.base.web.bean.TUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 10:42  2018/3/29
 */
public interface TUserService extends GenericService<TUser, Long> {

    //店铺管理页面，添加店铺模态框查询所有负责人
    List<Map> addshopquerytuser(String shopId);

    //会员管理页面显示所有会员
    List<Map> queryTuser();

    int changeStatus(String status, String tuserId);

    Long returnroleId(String roleName);

    Map userHome(Long userId);

    // >> 根据id查询用户
    Map queryTUserById(Object uId);

    // >>查询用户试穿次数
    Integer queryResidueDegree(Long uId);

    // >>修改用户试穿次数
    int updateResidueDegree(Integer degree,Long uId);

    int confirmPassword(String userId, String password);
    int changePassword(String userId, String password);

    //>>根据（父级会员）查询用户
    List<Map> queryUserByPId(Long parentId);

    //>>查询用户余额
    BigDecimal queryEarn(Long uId);

    /**
     * 获取openid
     * 参数：临时登录凭证（code）
     * @param code
     * @return
     */
    JSONObject registered(String code);

    /**
     * 使用余额支付 true:成功，false：余额不足
     * @param price
     * @return
     */
    Boolean payByEarn(BigDecimal price);

    /**
     * 更新余额,earn:需要增加或减少的余额
     * @param earn
     * @param userId
     * @return
     */
    int updateEarn(BigDecimal earn, Long userId);



}
