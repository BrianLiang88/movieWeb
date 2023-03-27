package javaWeb.Brian.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
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
