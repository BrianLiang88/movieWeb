package javaWeb.Brian.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class PaymentUtil {

    // 支付网关
    private static final String GATEWAY = "https://www.example.com/pay";
    // 商户编号
    private static final String MERCHANT_ID = "123456";
    // 商户密钥
    private static final String MERCHANT_KEY = "abcdefg";

    /**
     * 生成支付请求的表单数据
     * @param orderId 订单号
     * @param amount 金额
     * @return 表单数据
     */
    public static String generatePaymentForm(String orderId, double amount) {
        // 生成签名
        String sign = generateSign(orderId, amount);
        // 构造表单数据
        StringBuilder sb = new StringBuilder();
        sb.append("<form action=\"" + GATEWAY + "\" method=\"POST\">");
        sb.append("<input type=\"hidden\" name=\"orderId\" value=\"" + orderId + "\"/>");
        sb.append("<input type=\"hidden\" name=\"amount\" value=\"" + amount + "\"/>");
        sb.append("<input type=\"hidden\" name=\"sign\" value=\"" + sign + "\"/>");
        sb.append("<input type=\"submit\" value=\"立即支付\"/>");
        sb.append("</form>");
        return sb.toString();
    }



    /**
     * 生成签名
     * @param orderId 订单号
     * @param amount 金额
     * @return 签名
     */
    private static String generateSign(String orderId, double amount) {
        Map<String, String> params = new TreeMap<>();
        params.put("merchantId", MERCHANT_ID);
        params.put("orderId", orderId);
        params.put("amount", String.format("%.2f", amount));
        // 计算签名
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            sb.append(key).append("=").append(params.get(key)).append("&");
        }
        sb.append("key=").append(MERCHANT_KEY);
        return md5(sb.toString());
    }

    public static String pay(String orderId, String body, String subject, String totalFee) {
        // 构造请求参数
        SortedMap<String, String> paramMap = new TreeMap<>();
        paramMap.put("out_trade_no", orderId);
        paramMap.put("body", body);
        paramMap.put("subject", subject);
        paramMap.put("total_fee", totalFee);
        paramMap.put("spbill_create_ip", "127.0.0.1");
        paramMap.put("notify_url", "http://www.example.com/notify");
        paramMap.put("trade_type", "NATIVE");
        paramMap.put("nonce_str", getNonceStr());
        paramMap.put("appid", "your_app_id");
        paramMap.put("mch_id", "your_mch_id");

        // 签名
        String sign = sign(paramMap, "your_key");

        // 构造 XML 请求
        String requestXml = "<xml>"
                + "<appid>" + paramMap.get("appid") + "</appid>"
                + "<body>" + paramMap.get("body") + "</body>"
                + "<mch_id>" + paramMap.get("mch_id") + "</mch_id>"
                + "<nonce_str>" + paramMap.get("nonce_str") + "</nonce_str>"
                + "<notify_url>" + paramMap.get("notify_url") + "</notify_url>"
                + "<out_trade_no>" + paramMap.get("out_trade_no") + "</out_trade_no>"
                + "<spbill_create_ip>" + paramMap.get("spbill_create_ip") + "</spbill_create_ip>"
                + "<subject>" + paramMap.get("subject") + "</subject>"
                + "<total_fee>" + paramMap.get("total_fee") + "</total_fee>"
                + "<trade_type>" + paramMap.get("trade_type") + "</trade_type>"
                + "<sign>" + sign + "</sign>"
                + "</xml>";

        // 发送请求并获取响应
        String responseXml = sendRequest(requestXml, "https://api.mch.weixin.qq.com/pay/unifiedorder");

        // 解析响应 XML 并获取支付二维码链接
        String codeUrl = parseCodeUrl(responseXml);

        return codeUrl;
    }

    /**
     * 计算字符串的 MD5 值
     * @param text 字符串
     * @return MD5 值
     */
    private static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
