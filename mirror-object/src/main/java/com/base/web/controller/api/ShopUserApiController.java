package com.base.web.controller.api;

import com.base.web.bean.*;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.*;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.ResourceUtil;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(value = "ShopUserApiController", description = "商家端我的页面")
@RequestMapping("/api/shopuser/")
@RestController
public class ShopUserApiController {

    @Resource
    private TUserService tUserService;
    @Resource
    private ShopGoodsPushService shopGoodsPushService;

    @Resource
    private ShopAddGoodsService shopAddGoodsService;
    @Resource
    private FileRefService fileRefService;
    @Resource
    private ShopService shopService;
    @Resource
    private PropertyService propertyService;
    @Resource
    private GoodsClassService goodsClassService;

    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "旧密码",
                    required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "newPassword", value = "新密码",
                    required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "confirmPassword", value = "确认密码",
                    required = true, dataType = "String", paramType = "form"),
    })
    @ApiOperation(value = "商家修改密码")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage changePassword( @RequestParam("oldPassword")String oldPassword,
                    @RequestParam("newPassword")String newPassword, @RequestParam("confirmPassword")String confirmPassword) {
        if(newPassword == null || newPassword.isEmpty()){
            return ResponseMessage.error("请输入新密码");
        }
        String userId = WebUtil.getLoginUser().getId().toString();
        if(tUserService.confirmPassword(userId, oldPassword) != 1){
            return ResponseMessage.error("旧密码错误");
        }
        if(!newPassword.equals(confirmPassword)){
            return ResponseMessage.error("两次密码不同");
        }
        return ResponseMessage.ok(tUserService.changePassword(userId, newPassword));
    }

    //----------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "showShopUserInfo", method = RequestMethod.GET)
    @ApiOperation(value = "商家信息", notes = "返回data为Map｛<br>" +
            "name：店铺名称<br>" +
            "address：店铺地址" +
            "logo：头像资源路径｝")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "需要查询头像、名称的用户id",
                    dataType = "Long", paramType = "form"),
    })
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showShopUserInfo(@RequestParam(value = "userId", defaultValue = "-1")Long userId,
                                            HttpServletRequest req) {

        Long shopId = shopService.queryShopIdByUserId(WebUtil.getLoginUser().getId());
        Map map = shopService.showShopUserInfo(shopId, req);
        if(userId != -1){
            TUser tUser = tUserService.selectByPk(userId);
            if(tUser != null){
                map.put("avatar", tUser.getAvatar());
                map.put("userName", tUser.getName());
            }else{
                return ResponseMessage.error("用户已注销");
            }
        }
        return ResponseMessage.ok(map);
    }

    @RequestMapping(value = "queryShopUser", method = RequestMethod.GET)
    @ApiOperation(value = "查询当前登录店铺用户导购情况", notes = "返回data为Map｛<br>" +
            "phone：导购元手机号<br>" +
            "total：导购人数<br>"+
            "｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage queryShopUser() {
        Shop shop = shopService.createQuery().where(Shop.Property.userId,WebUtil.getLoginUser().getId()).single();
        return ResponseMessage.ok(shopGoodsPushService.queryShopUser(shop.getId()));
    }
    @RequestMapping(value = "queryRecommendUserList/{phone}", method = RequestMethod.GET)
    @ApiOperation(value = "查询导购圆推荐用户列表", notes = "返回data为Map｛<br>" +
            "phone：导购元手机号<br>" +
            "total：导购人数<br>"+
            "avatar：用户头像<br>"+
            "userId：用户ID<br>"+
            "name：用户名称<br>"+
            "｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage queryRecommendUserList(@PathVariable Long phone) {
        Shop shop = shopService.createQuery().where(Shop.Property.userId,WebUtil.getLoginUser().getId()).single();
        List<Map> mapList = shopGoodsPushService.queryRecommendUserList(phone,shop.getId());
        return ResponseMessage.ok(mapList);
    }
    @RequestMapping(value = "queryUserRecommendList/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户导购详情列表", notes = "返回data为Map｛<br>" +
            "phone：导购元手机号<br>" +
            "total：导购人数<br>"+
            "avatar：用户头像<br>"+
            "userId：用户ID<br>"+
            "name：用户名称<br>"+
            "｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage queryUserRecommendList(@PathVariable Long userId, HttpServletRequest req) {
        List<ShopGoodsPush> mapList = shopGoodsPushService.createQuery()
                .where(ShopGoodsPush.Property.userId,userId)
                .orderByDesc(ShopGoodsPush.Property.createTime)
                .list();
        if(mapList != null ){
            for(ShopGoodsPush shopGoodsPush : mapList){
                Property property = propertyService.createQuery()
                        .where(Property.Property.id,shopGoodsPush.getAge())
                        .and("type",1)
                        .single();
                shopGoodsPush.setAgestr(property.getName());
                //用户兴趣名称数组
              String[] like = shopGoodsPush.getLike().split(",");
              String [] likeName = new String[like.length];
              if(like.length > 0){
                  for(int  i = 0 ; i < like.length ; i ++){
                      if(like[i] != "" && like[i] != null) {
                          GoodsClass goodsClass  = goodsClassService.createQuery()
                                  .where(GoodsClass.Property.id,like[i])
                                  .and(GoodsClass.Property.status, 0)
                                  .single();
                          likeName[i] = goodsClass.getClassName();
                      }
                  }
              }
                shopGoodsPush.setLikes(likeName);//构建用户兴趣名称数组

                //用户商品名称数组
                String[] goodsId = shopGoodsPush.getGoodsId().split(",");
                //用户商品名称数组
                List< ShopAddGoods> shopAddGoodsList = new ArrayList<>(goodsId.length);
                if(goodsId.length > 0){
                    for(int  i = 0 ; i < goodsId.length ; i ++){
                        if(goodsId[i] != "" && goodsId[i] != null){
                            ShopAddGoods goods = shopAddGoodsService.createQuery()
                                    .where(ShopAddGoods.Property.id,goodsId[i])
                                    .single();
                            FileRef fileRef =  fileRefService.createQuery()
                                    .where(FileRef.Property.refId,goods.getImageId())
                                    .and(FileRef.Property.dataType,10)
                                    .single();
                            String imagePath = ResourceUtil.resourceBuildPath(req, fileRef.getResourceId().toString().trim());
                            goods.setImageUrl(imagePath);
                            shopAddGoodsList.add(i,goods);//构建用户兴趣商品名称数组
                        }
                    }
                }
                shopGoodsPush.setShopAddGoodsList(shopAddGoodsList);
            }
        }
        return ResponseMessage.ok(mapList);
    }

      /**
     * 聊天界面展示头像，商店名称
     * @param userId
     * @return
     */
    @ApiOperation(value="聊天界面显示对方头像、名称")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @RequestMapping(value = "{userId}/messageShowAvatar",method = RequestMethod.GET)
    public ResponseMessage messageShowAvatar(@PathVariable("userId") Long userId, HttpServletRequest req){
        TUser tUser = tUserService.selectByPk(userId);
        Map map = new HashMap();
        //普通用户
        if(tUser.getRoleId() == 10013){
            map.put("imageUrl", tUser.getAvatar());
            map.put("name", tUser.getName());
        }else{
            //商家
            Long shopId = shopService.queryShopIdByUserId(userId);
            map = shopService.showShopUserInfo(shopId, req);
        }
        map.put("avatar", WebUtil.getLoginUser().getAvatar());
        return ResponseMessage.ok(map);
    }
}
