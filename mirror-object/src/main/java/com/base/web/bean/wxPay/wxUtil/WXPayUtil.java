package com.base.web.bean.wxPay.wxUtil;
import com.base.web.bean.wxPay.Constant;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.*;

/**
 * @Author: HONGLINCHEN
 * @Description:微信支付
 * @Date: 2017-9-7 17:14
 */
public class WXPayUtil {
    public static String PostRequest(String url, String data) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod post=new PostMethod(url);
        String result = "";
        post.addRequestHeader("Content-Type", "text/html; charset=utf-8");
        post.addRequestHeader("content", "text/html; charset=utf-8");
        post.setRequestBody(data);
        try {
            int status=client.executeMethod(post);
            result = post.getResponseBodyAsString();
            result = new String(result.getBytes(post.getResponseCharSet()), "utf-8");
        } catch (IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String createSign(SortedMap<String, String> packageParams, String AppKey) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + AppKey);
        String sign = null;
        try {
            sign = CommonUtil.getMD5(sb.toString()).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }
    /**
     * @Author: HONGLINCHEN
     * @Description:微信支付 统一下单
     * @param out_trade_no
     * @param body
     * @param detail
     * @param total_fee
     * @param ip_address
     * @Date: 2017-9-11 14:35
     * @return:
     */
    public static String unifiedOrder(String out_trade_no, String body, String detail, int total_fee,String ip_address) {
        StringBuffer xml = new StringBuffer();
        String data = null;
        try{
            xml.append("</xml>");
            if (body.length() > 32) {
                body = body.substring(0, 32);
            }
            SortedMap<String, String> parameters = new TreeMap();
            parameters.put("appid", Constant.APP_ID);
            parameters.put("body", body);
            parameters.put("detail", detail);
            parameters.put("mch_id", Constant.MCH_ID);
            parameters.put("nonce_str", genNonceStr());
            parameters.put("notify_url", "http://www.aidongsports.com/wx");
            parameters.put("out_trade_no", out_trade_no);
            parameters.put("fee_type", "CNY");
            parameters.put("spbill_create_ip", ip_address);
            parameters.put("total_fee", String.valueOf(total_fee));
            parameters.put("trade_type", "APP");
            parameters.put("sign", createSign(parameters, Constant.APP_KEY));
            data = PostRequest("https://api.mch.weixin.qq.com/pay/unifiedorder",SortedMaptoXml(parameters));
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
    /**
     * @Author: HONGLINCHEN
     * @Description:微信企业付款给个人用户到零钱
     * @param partner_trade_no 商户订单号
     * @param openid 用户微信ID
     * @param amount 支付金额
     * @param desc 付款描述信息
     * @param ip_address ip地址
     * @Date: 2017-9-11 14:35
     * @return:
     */
    public static String wxPayTransfers(String partner_trade_no, String openid,String amount, String desc, String ip_address) throws UnsupportedEncodingException {
        StringBuffer xml = new StringBuffer();
        String data = null;
        try {
            String nonceStr = genNonceStr();
            xml.append("</xml>");
            SortedMap<String,String> parameters = new TreeMap<String,String>();
            parameters.put("amount", amount);
            parameters.put("mch_appid", Constant.APP_ID);
            parameters.put("check_name", "NO_CHECK");
            parameters.put("desc", desc);
            parameters.put("mchid", Constant.MCH_ID);
            parameters.put("nonce_str", nonceStr);
            parameters.put("openid", openid);
            parameters.put("partner_trade_no", partner_trade_no);
            parameters.put("spbill_create_ip", ip_address);
            parameters.put("sign", createSign(parameters, Constant.APP_KEY));
            data =SortedMaptoXml(parameters);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
        //签名中有中文要转换
        return new String(data.getBytes(), "ISO8859-1");
    }
    /**
     * @Author: HONGLINCHEN
     * @Description:微信退款
     * @param out_trade_no 订单ID
     * @param out_refund_no 退款ID
     * @param total_fee 订单总价
     * @param refund_fee 退款金额
     * @Date: 2017-9-11 14:35
     * @return:
     */
    public static String wxPayRefund(String out_trade_no, String out_refund_no,String total_fee, String refund_fee) {
        StringBuffer xml = new StringBuffer();
        String data = null;
        try {
            String nonceStr = genNonceStr();
            xml.append("</xml>");
            SortedMap<String,String> parameters = new TreeMap<String,String>();
            parameters.put("appid", Constant.APP_ID);
            parameters.put("mch_id", Constant.MCH_ID);
            parameters.put("nonce_str", nonceStr);
            parameters.put("out_trade_no", out_trade_no);
            parameters.put("out_refund_no", out_refund_no);
            parameters.put("fee_type", "CNY");
            parameters.put("total_fee", total_fee);
            parameters.put("refund_fee", refund_fee);
            parameters.put("op_user_id", Constant.MCH_ID);
            parameters.put("sign", createSign(parameters, Constant.APP_KEY));
            data =SortedMaptoXml(parameters);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
        return data;
    }
    /**
     * 证书使用
     * 微信退款
     */
    public static String wxPayBack(String url, String data) throws Exception {
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("D:\\微信商户平台支付证书\\apiclient_cert.p12"));
        String result="";
        try {
            keyStore.load(instream, Constant.MCH_ID.toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, Constant.MCH_ID.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            StringEntity entitys = new StringEntity(data);
            httppost.setEntity((HttpEntity) entitys);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String text="";
                    StringBuffer t= new StringBuffer();
                    while ((text=bufferedReader.readLine()) != null) {
                        t.append(text);
                    }
                    byte[] temp=t.toString().getBytes("gbk");//这里写原编码方式
                    String newStr=new String(temp,"utf-8");//这里写转换后的编码方式
                    result=newStr;
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return result;
    }

    /**
     * XML格式字符串转换为Map
     * 微信支付 解析xml xml转map  获取prepay_id
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            documentBuilderFactory.setXIncludeAware(false);
            documentBuilderFactory.setExpandEntityReferences(false);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            WXPayUtil.getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
            throw ex;
        }

    }

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     * @param data 待处理数据
     * @param key 密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * @Author: HONGLINCHEN
     * @Description:通过prepay_id 生成微信支付参数
     * @param prepay_id
     * @Date: 2017-9-8 10:17
     */
    public static  SortedMap<Object,Object> genPayRequest(String prepay_id) {
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("appid", Constant.APP_ID);
        parameters.put("noncestr", genNonceStr());
        parameters.put("package", "Sign=WXPay");
        parameters.put("partnerid", Constant.MCH_ID);
        parameters.put("prepayid", prepay_id);
        parameters.put("timestamp",getCurrentTimestamp());
        parameters.put("sign", MD5.createSign("utf-8", parameters).toUpperCase());
        return parameters;
    }
    /**
     * @Author: HONGLINCHEN
     * @Description:请求值转换为xml格式 SortedMap转xml
     * @param params
     * @Date: 2017-9-7 17:18
     */
    private static String SortedMaptoXml(SortedMap<String,String> params) {
        StringBuilder sb = new StringBuilder();
        Set es = params.entrySet();
        Iterator it = es.iterator();
        sb.append("<xml>\n");
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            sb.append("<"+k+">");
            sb.append(v);
            sb.append("</"+k+">\n");
        }
        sb.append("</xml>");
        return sb.toString();
    }
    /**
     * 日志
     * @return
     */
    public static Logger getLogger() {
        Logger logger = LoggerFactory.getLogger("wxpay java sdk");
        return logger;
    }

    /**
     * 生成32位随机数字
     */
    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }
    /**
     * 获取当前时间戳，单位秒
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis()/1000;
    }


    /**
     * 生成 uuid， 即用来标识一笔单，也用做 nonce_str
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

}
