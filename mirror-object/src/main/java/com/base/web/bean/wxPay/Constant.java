package com.base.web.bean.wxPay;

/**
 * 支付相关常量文件
 */
public class Constant {

    public static final String DOMAIN = "https:mirror.lmlm.cn";

    public static final String APP_ID = "wxd93e04f3989aad51";

    public static final String APP_SECRET = "67e1884473fa0c9d1d253ccd96ae76a3";

    public static final String APP_KEY = "0528YIMEIOU1425mirrorZXWL2018318";

    public static final String MCH_ID = "1503959021";  //商户号

    public static final String URL_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

//    public static final String URL_NOTIFY = Constant.DOMAIN + "/wxpay/views/payInfo.jsp";
public static final String URL_NOTIFY = Constant.DOMAIN + "/";
    public static final String TIME_FORMAT = "yyyyMMddHHmmss";

    public static final int TIME_EXPIRE = 2;  //单位是day

}
