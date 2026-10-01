package murach.momo;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MoMoConfig {

    public static final String MOMO_ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create";

    // Cặp khóa thử nghiệm chuẩn của MoMo Sandbox (hoạt động 100% không cần đăng ký phức tạp)
    public static final String DEFAULT_PARTNER_CODE = "MOMO5RGX20191128";
    public static final String DEFAULT_ACCESS_KEY = "M8brj9K6E22vXoDB";
    public static final String DEFAULT_SECRET_KEY = "nqQiVSgDMy809JoPF6OzP5OdBUB550Y4";

    public static String getPartnerCode() {
        String env = System.getenv("MOMO_PARTNER_CODE");
        return (env != null && !env.trim().isEmpty()) ? env.trim() : DEFAULT_PARTNER_CODE;
    }

    public static String getAccessKey() {
        String env = System.getenv("MOMO_ACCESS_KEY");
        return (env != null && !env.trim().isEmpty()) ? env.trim() : DEFAULT_ACCESS_KEY;
    }

    public static String getSecretKey() {
        String env = System.getenv("MOMO_SECRET_KEY");
        return (env != null && !env.trim().isEmpty()) ? env.trim() : DEFAULT_SECRET_KEY;
    }

    /**
     * Tự động lấy URL trả về (hỗ trợ cả chạy localhost và HTTPS trên Render)
     */
    public static String getRedirectUrl(HttpServletRequest request) {
        String env = System.getenv("MOMO_REDIRECT_URL");
        if (env != null && !env.trim().isEmpty()) {
            return env.trim();
        }
        return getBaseUrl(request) + "/momo-return";
    }

    public static String getIpnUrl(HttpServletRequest request) {
        String env = System.getenv("MOMO_IPN_URL");
        if (env != null && !env.trim().isEmpty()) {
            return env.trim();
        }
        return getBaseUrl(request) + "/momo-ipn";
    }

    private static String getBaseUrl(HttpServletRequest request) {
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

        return proto + "://" + host + contextPath;
    }

    public static String hmacSHA256(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(secretKeySpec);
            byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            return "";
        }
    }
}
