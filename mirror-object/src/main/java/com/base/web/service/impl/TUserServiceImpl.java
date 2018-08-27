package com.base.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.web.bean.OrderProfit;
import com.base.web.bean.TUser;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.TUserMapper;
import com.base.web.service.OrderProfitService;
import com.base.web.service.TUserService;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hsweb.expands.request.SimpleRequestBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 10:45  2018/3/29
 */
@Service("TUserService")
public class TUserServiceImpl extends AbstractServiceImpl<TUser, Long> implements TUserService {

    @Resource
    private TUserMapper tUserMapper;

    @Resource
    private OrderProfitService orderProfitService;

    @Override
    protected TUserMapper getMapper() {
        return this.tUserMapper;
    }

    @Override
    public List<Map> addshopquerytuser(String shopId) {
        return getMapper().addshopquerytuser(shopId);
    }

    @Override
    public List<Map> queryTuser() {
        return getMapper().queryTuser();
    }

    @Override
    public int changeStatus(String status, String tuserId) {
        return getMapper().changeStatus(status, tuserId);
    }

    @Override
    public Long returnroleId(String roleName) {
        return getMapper().returnroleId(roleName);
    }

    @Override
    public Map userHome(Long userId) {
        return getMapper().userHome(userId);
    }

    @Override
    public Map queryTUserById(Object uId) {
        return getMapper().queryTUserById(uId);
    }

    // >>查询用户试穿次数
    @Override
    public Integer queryResidueDegree(Long uId) {
        return getMapper().queryResidueDegree(uId);
    }

    // >>修改用户试穿次数
    @Override
    public int updateResidueDegree(Integer degree, Long uId) {
        return getMapper().updateResidueDegree(degree, uId);
    }

    @Override
    public int confirmPassword(String userId, String password) {
        return getMapper().confirmPassword(userId, password);
    }

    @Override
    public int changePassword(String userId, String password) {
        return getMapper().changePassword(userId, password);
    }

    //>>根据（父级会员）查询用户
    @Override
    public List<Map> queryUserByPId(Long parentId) {
        return getMapper().queryUserByPId(parentId);
    }

    //>>查询用户余额
    @Override
    public BigDecimal queryEarn(Long uId) {
        return getMapper().queryEarn(uId);
    }

    /**
     * 获取openid
     * 参数：临时登录凭证（code）
     *
     * @param code
     * @return
     */
    public JSONObject registered(String code) {
        String result = null;
        try {
            result = new SimpleRequestBuilder()
                    .http("https://api.weixin.qq.com/sns/jscode2session?appid=" + TUser.getAPPID()
                            + "&secret=" + TUser.getSECRET() + "&grant_type=authorization_code&js_code=" + code)
                    .resultAsJsonString() //  ->  header("Accept", "application/json");
                    .get()
                    .asString();
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        if(result == null){
            return null;
        }
        return JSON.parseObject(result);
    }

    @Override
    public Boolean payByEarn(BigDecimal price) {
        TUser tUser = getMapper().selectByPk(WebUtil.getLoginUser().getId());
        //查询余额是否大于订单总价
        if (tUser.getEarn() == null || tUser.getEarn().compareTo(price) == -1) {
            return false;
        }
        //negate取相反数，余额减去订单价格
        updateEarn(price.negate(), tUser.getId());
        return true;
    }

    @Override
    public int updateEarn(BigDecimal earn, Long userId) {
        return getMapper().updateEarn(earn, userId);
    }


}
