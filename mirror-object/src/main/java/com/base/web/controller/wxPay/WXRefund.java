package com.base.web.controller.wxPay;

import com.base.web.bean.wxPay.Constant;
import com.base.web.bean.wxPay.wxUtil.WXPayUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.Map;

@Controller
public class WXRefund {
    private static final String REFUNDURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    private static final String TRANSFERSURL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    /**
     * 微信退款 第三种方式，也测试过 可以成功退款
     *
     * @param out_trade_no  商户订单号
     * @param out_refund_no 商户退款订单号
     * @param total_fee     订单总金额
     * @param refund_fee    退款金额
     * @return
     * @throws Exception
     */
    public static String doRefund(Long out_trade_no, Long out_refund_no, BigDecimal total_fee, BigDecimal refund_fee) throws Exception {

        //自定义金额倍率转换 微信小程序金额已分为单位，所以需要将金额转换为分且只能为整数
        BigDecimal bigDecimal = new BigDecimal("100");
        Integer totalFee = total_fee.multiply(bigDecimal).intValue();
        Integer refundFee = refund_fee.multiply(bigDecimal).intValue();

        String xmlstring = WXPayUtil.wxPayRefund(out_trade_no.toString(), out_refund_no.toString(), String.valueOf(totalFee), String.valueOf(refundFee));
        return wxPayMethod(REFUNDURL, xmlstring);
    }

    /**
     * 微信支付，企业向个人用户付款到零钱
     * @param partner_trade_no 商家订单号
     * @param openid 用户微信ID
     * @param amount 金额
     * @param desc 描述信息
     * @param ip_address ip地址
     * @return
     * @throws Exception
     */
    public static String doTransfers(Long partner_trade_no, String openid,BigDecimal amount, String desc, String ip_address) throws Exception {

        //自定义金额倍率转换 微信小程序金额已分为单位，所以需要将金额转换为分且只能为整数
        BigDecimal bigDecimal = new BigDecimal("100");
        Integer price = amount.multiply(bigDecimal).intValue();

        String xmlstring = WXPayUtil.wxPayTransfers(partner_trade_no.toString(), openid, String.valueOf(price), desc, ip_address);
        return wxPayMethod(TRANSFERSURL, xmlstring);
    }

    /**
     * 微信支付
     *
     * @param xmlstring 请求所需的xml字符串
     * @return
     * @throws Exception
     */
    public static String wxPayMethod(String url, String xmlstring) throws Exception {

        InputStream instream = null;
        KeyStore keyStore = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = null;
        StringBuilder text = new StringBuilder();
        String key = Constant.MCH_ID;

        try {
            /**
             * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
             */
            keyStore = KeyStore.getInstance("PKCS12");
            instream = new FileInputStream(new File(System.getProperty("catalina.home") + File.separator + "apiclient_cert.p12"));


            /**
             * 此处要改
             */
            keyStore.load(instream, key.toCharArray());// 这里写密码..默认是MCHID

            /**
             * 此处要改
             */
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, key.toCharArray())// 这里也是写密码的
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            //=======================证书配置完成========================


            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new StringEntity(xmlstring));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            response = httpclient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));

                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    text.append(str);
                }
                bufferedReader.close();
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {

        } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(text);
        Map<String, String> map = WXPayUtil.xmlToMap(text.toString());
        String return_msg = map.get("return_msg");
        if ("OK".equals(return_msg) && "SUCCESS".equals(map.get("return_code"))) {
            return return_msg;
        } else {
            return return_msg;
        }
    }
}