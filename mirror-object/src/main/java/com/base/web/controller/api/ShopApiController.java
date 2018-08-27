package com.base.web.controller.api;


import com.base.web.bean.Shop;
import com.base.web.bean.TUser;
import com.base.web.bean.po.user.User;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.session.HttpSessionManager;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.ShopService;
import com.base.web.service.TUserService;
import com.base.web.service.VideoOrderService;
import io.swagger.annotations.Api;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Api(value = "ShopApiController", description = "客户端商家操作类接口")
@RequestMapping("/api/shop/")
@RestController
public class ShopApiController {

    /**
     * 授权过程所需缓存
     */
    @Autowired(required = false)
    private CacheManager cacheManager;


    /**
     * 用户服务类
     */
    @Resource
    private TUserService tuserService;

    @Resource
    private VideoOrderService videoOrderService;
    @Resource
    private ShopService shopService;
    /**
     * httpSession管理器
     */
    @Autowired
    private HttpSessionManager httpSessionManager;



    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @AccessLogger("登录")
    public ResponseMessage login(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam("openId") String openId,
                                 HttpServletRequest request) throws Exception {

        if(username == null || username.equals("") ||password == null || password.equals("") ){
            return ResponseMessage.error("请输入用户名密码");
        }
        //判断用户是否多次输入密码错误
        String userIp = WebUtil.getIpAddr(request);
        Cache cache = cacheManager.getCache("login.error");
        String cachePrefix = username.concat("@").concat(userIp);
        String timeCacheKey = cachePrefix.concat("-time");
        String numberCacheKey = cachePrefix.concat("-number");


        TUser user = tuserService.createQuery().where(TUser.Property.roleId, 10012)
                .and(TUser.Property.phone, username)
                .and(TUser.Property.status, 0).single();
        if (user == null || user.getStatus() != 0) {
            return ResponseMessage.error("用户不存在或已注销");
        }
        if (!user.getPassword().equals(password)) {
            return ResponseMessage.error("账号或密码错误");
        }
        Shop shop = shopService.createQuery().where(Shop.Property.userId, user.getId()).single();
        if(shop == null || shop.getStatus() != 1){
            return ResponseMessage.error("店铺不存在或已注销");
        }
        //商家绑定用户端openid用于用户端模板消息推送
        if(user.getOpenId() == null && openId != null && !"null".equals(openId)){
            user.setOpenId(openId);
            tuserService.update(user);
        }
//        //密码错误

        cache.evict(timeCacheKey);
        cache.evict(numberCacheKey);
        user.setPassword("");//去除密码


        User newUser = new User();
        BeanUtilsBean.getInstance().getPropertyUtils()
                .copyProperties(newUser, user);
        httpSessionManager.addUser(newUser, request.getSession());
        Map map = new HashMap();
        map.put("session", request.getSession().getId());
        map.put("userId", user.getId());
        return ResponseMessage.ok(map);
    }



//    @RequestMapping(value = "queryTransactionList/{type}", method = RequestMethod.GET)
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "path", dataType = "int",name = "type",
//                    value = "标签切换值（0，视频订单，1 试衣订单，2 购买订单）", required = true)
//
//    })
//    @ApiOperation(value = "商家端小程序交易中订单查询", notes = "返回data为 list Map｛<br>" +
//            "phone：导购元手机号<br>" +
//            "total：导购人数<br>"+
//            "avatar：用户头像<br>"+
//            "userId：用户ID<br>"+
//            "name：用户名称<br>"+
//            "｝")
//    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
//            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
//            @ApiResponse(code = 401, message = "没有访问权限"),
//            @ApiResponse(code = 403, message = "禁止访问该数据"),
//            @ApiResponse(code = 404, message = "服务不存在"),
//            @ApiResponse(code = 500, message = "服务器内部异常")})
//    public ResponseMessage queryRecommendUserList(@PathVariable Integer type) {
//        // 交易中的视频订单
//        if(type ==0){
//            List<VideoOrder> videoOrderList = videoOrderService.createQuery().where()
//            return ResponseMessage.ok();
//        }
//        // 交易中的试衣订单
//        if(type ==1){
//            return ResponseMessage.ok();
//        }
//        // 交易中的购买订单
//        if(type ==2){
//            return ResponseMessage.ok();
//        }
//        return ResponseMessage.ok();
//    }

}
