package murach.vnpay;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPayConfig {

    public static final String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String VNP_API_URL = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    // Thay mã TmnCode và HashSecret của bạn vào đây hoặc cấu hình qua Environment Variable (trên Render)
    public static String DEFAULT_TMN_CODE = "CGXZLS0Z"; // Hoặc mã do VNPay cấp cho bạn
    public static String DEFAULT_HASH_SECRET = "XNBCJFAKAZQSGTARRLKXAARGRFUVNMUA"; // Hoặc mã bí mật do VNPay cấp

    public static String getTmnCode() {
        String env = System.getenv("VNP_TMN_CODE");
        return (env != null && !env.trim().isEmpty()) ? env.trim() : DEFAULT_TMN_CODE;
    }

    public static String getHashSecret() {
        String env = System.getenv("VNP_HASH_SECRET");
        return (env != null && !env.trim().isEmpty()) ? env.trim() : DEFAULT_HASH_SECRET;
    }

    /**
     * Tự động nhận diện URL trả về (hỗ trợ cả chạy localhost và deploy trên Render)
     */
    public static String getReturnUrl(HttpServletRequest request) {
        String envReturnUrl = System.getenv("VNP_RETURN_URL");
        if (envReturnUrl != null && !envReturnUrl.trim().isEmpty()) {
            return envReturnUrl.trim();
        }

        // Nhận diện protocol qua reverse proxy (Render sử dụng SSL Termination)
        String proto = request.getHeader("x-forwarded-proto");
        if (proto == null || proto.isEmpty()) {
            proto = request.getScheme();
        }

        String host = request.getHeader("x-forwarded-host");
        if (host == null || host.isEmpty()) {
            host = request.getHeader("host");
        }
        if (host == null || host.isEmpty()) {
            host = request.getServerName();
            int port = request.getServerPort();
            if ((proto.equalsIgnoreCase("http") && port != 80) || (proto.equalsIgnoreCase("https") && port != 443)) {
                host += ":" + port;
            }
        }

        String contextPath = request.getContextPath();
        if (contextPath == null) {
            contextPath = "";
        }

        return proto + "://" + host + contextPath + "/vnpay-return";
    }

    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                return "";
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    public static String hashAllFields(Map<String, String> fields, String secretKey) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                try {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return hmacSHA512(secretKey, hashData.toString());
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = request.getRemoteAddr();
            } else if (ipAddress.contains(",")) {
                ipAddress = ipAddress.split(",")[0].trim();
            }
        } catch (Exception e) {
            ipAddress = "127.0.0.1";
        }

        if (ipAddress == null || ipAddress.isEmpty() || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }
        return ipAddress;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
