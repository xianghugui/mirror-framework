package com.base.web.controller.wxPay;

import com.base.web.bean.wxPay.Constant;
import com.base.web.bean.wxPay.PayInfo;
import com.base.web.bean.wxPay.wxUtil.CommonUtil;
import com.base.web.bean.wxPay.wxUtil.HttpUtil;
import com.base.web.bean.wxPay.wxUtil.RandomUtils;
import com.base.web.bean.wxPay.wxUtil.TimeUtils;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public  class  WxPayCommon {


    private static Logger log = Logger.getLogger(WxPayCommon.class);

    public static ResponseMessage wxPay(String out_trade_no, BigDecimal bigDecimalTypePrice, String goodsName, HttpServletRequest request){
        String openId = WebUtil.getLoginUser().getOpenId();
        if(openId == null || "".equals(openId)) {
            return ResponseMessage.error("openID没获取到");
        }
        //自定义金额倍率转换 微信小程序金额已分为单位，所以需要将金额转换为分且只能为整数
        BigDecimal bigDecimal = new BigDecimal("100");
        Integer price = bigDecimalTypePrice.multiply(bigDecimal).intValue();
        //支付之后返回给小程序的信息
        String content = null;
        Map map = new HashMap();
        ObjectMapper mapper = new ObjectMapper();

        boolean result = true;
        String info = "";

        String clientIP = CommonUtil.getClientIp(request);

        log.error("openId: " + openId + ", clientIP: " + clientIP);

        String randomNonceStr = RandomUtils.generateMixString(32);
        //统一下单需传递参数类
        String prepayId = unifiedOrder(openId, clientIP, randomNonceStr, price,out_trade_no, goodsName);

        log.error("prepayId: " + prepayId);

        if(StringUtils.isBlank(prepayId)) {
            result = false;
            info = "出错了，未获取到prepayId";
        } else {
            map.put("prepayId", prepayId);
            map.put("nonceStr", randomNonceStr);
        }
        try {
            map.put("result", result);
            map.put("info", info);
            content = mapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseMessage.ok(content);
    }



    /**
     * 调用微信小程序统一下单接口
     * @param openId
     */
    private static String unifiedOrder(String openId, String clientIP, String randomNonceStr, int price, String out_trade_no, String goodsName) {

        try {
            //微信小程序统一下单接口
            String url = Constant.URL_UNIFIED_ORDER;

            PayInfo payInfo = createPayInfo(openId, clientIP, randomNonceStr,price,out_trade_no, goodsName);
            String md5 = getSign(payInfo);
            payInfo.setSign(md5);

            log.error("md5 value: " + md5);

            String xml = CommonUtil.payInfoToXML(payInfo);
            xml = xml.replace("__", "_").replace("<![CDATA[1]]>", "1");
            log.error(xml);

            StringBuffer buffer = HttpUtil.httpsRequest(url, "POST", xml);
            log.error("unifiedOrder request return body: \n" + buffer.toString());
            Map<String, String> result = CommonUtil.parseXml(buffer.toString());


            String return_code = result.get("return_code");
            if(StringUtils.isNotBlank(return_code) && "SUCCESS".equals(return_code)) {

                String return_msg = result.get("return_msg");
                if(StringUtils.isNotBlank(return_msg) && !"OK".equals(return_msg)) {
                    return "";
                }

                String prepay_Id = result.get("prepay_id");
                return prepay_Id;

            } else {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 创建用户支付订单信息
     *
     * @param openId 下单用户openId
     * @param clientIP 下单用户ip
     * @param randomNonceStr  随机字符串
     * @param price 订单支付金额 单位为：分 不能带小数
     * @param out_trade_no  商户订单号
     * @return
     */
    private static PayInfo createPayInfo(String openId, String clientIP, String randomNonceStr, int price, String out_trade_no, String goodsName) {

        Date date = new Date();
        String timeStart = TimeUtils.getFormatTime(date, Constant.TIME_FORMAT);
        String timeExpire = TimeUtils.getFormatTime(TimeUtils.addDay(date, Constant.TIME_EXPIRE), Constant.TIME_FORMAT);
        PayInfo payInfo = new PayInfo();
        payInfo.setAppid(Constant.APP_ID);
        payInfo.setMch_id(Constant.MCH_ID);
        payInfo.setDevice_info("WEB");
        payInfo.setNonce_str(randomNonceStr);
        payInfo.setSign_type("MD5");  //默认即为MD5
        payInfo.setBody(goodsName);//商品描述
        payInfo.setAttach("支付测试");//附加数据
        payInfo.setOut_trade_no(out_trade_no);
        payInfo.setTotal_fee(price);//单位是 ：分
        payInfo.setSpbill_create_ip(clientIP);
        payInfo.setTime_start(timeStart);
        payInfo.setTime_expire(timeExpire);
        payInfo.setNotify_url(Constant.URL_NOTIFY);
        payInfo.setTrade_type("JSAPI");
        payInfo.setLimit_pay("no_credit");
        payInfo.setOpenid(openId);

        return payInfo;
    }

    /**
     * 根据小程序支付系统规则按照参数名ASCII码从小到大排序（字典序）
     * @param payInfo
     * @return
     * @throws Exception
     */
    private static String getSign(PayInfo payInfo) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("appid=" + payInfo.getAppid())
                .append("&attach=" + payInfo.getAttach())
                .append("&body=" + payInfo.getBody())
                .append("&device_info=" + payInfo.getDevice_info())
                .append("&limit_pay=" + payInfo.getLimit_pay())
                .append("&mch_id=" + payInfo.getMch_id())
                .append("&nonce_str=" + payInfo.getNonce_str())
                .append("&notify_url=" + payInfo.getNotify_url())
                .append("&openid=" + payInfo.getOpenid())
                .append("&out_trade_no=" + payInfo.getOut_trade_no())
                .append("&sign_type=" + payInfo.getSign_type())
                .append("&spbill_create_ip=" + payInfo.getSpbill_create_ip())
                .append("&time_expire=" + payInfo.getTime_expire())
                .append("&time_start=" + payInfo.getTime_start())
                .append("&total_fee=" + payInfo.getTotal_fee())
                .append("&trade_type=" + payInfo.getTrade_type())
                .append("&key=" + Constant.APP_KEY);

        log.error("排序后的拼接参数：" + sb.toString());

        return CommonUtil.getMD5(sb.toString().trim()).toUpperCase();
    }
}
