package murach.momo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import murach.business.Cart;

@WebServlet(name = "MoMoPaymentServlet", urlPatterns = {"/momo-pay"})
public class MoMoPaymentServlet extends HttpServlet {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart.jsp");
            return;
        }

        // Tỷ giá demo: 25,000 VND / USD
        long amount = Math.round(cart.getTotal() * 25000);
        if (amount < 1000) {
            amount = 1000;
        }

        String partnerCode = MoMoConfig.getPartnerCode();
        String accessKey = MoMoConfig.getAccessKey();
        String secretKey = MoMoConfig.getSecretKey();

        String orderId = "MM" + System.currentTimeMillis();
        String requestId = orderId;
        String orderInfo = "Thanh toan don hang #" + orderId;
        String redirectUrl = MoMoConfig.getRedirectUrl(request);
        String ipnUrl = MoMoConfig.getIpnUrl(request);
        String requestType = request.getParameter("requestType");
        if (requestType == null || requestType.trim().isEmpty()) {
            requestType = "payWithATM"; // Mặc định chuyển sang giao diện nhập thẻ ATM Test trên web
        }
        String extraData = "";

        // Tạo raw signature theo đúng chuẩn MoMo AIO v2 (thứ tự chữ cái A-Z)
        String rawSignature = "accessKey=" + accessKey
                + "&amount=" + amount
                + "&extraData=" + extraData
                + "&ipnUrl=" + ipnUrl
                + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo
                + "&partnerCode=" + partnerCode
                + "&redirectUrl=" + redirectUrl
                + "&requestId=" + requestId
                + "&requestType=" + requestType;

        String signature = MoMoConfig.hmacSHA256(secretKey, rawSignature);

        // Tạo JSON body gửi lên MoMo
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("partnerCode", partnerCode);
        requestBody.addProperty("partnerName", "Murach Cart Store");
        requestBody.addProperty("storeId", "MomoTestStore");
        requestBody.addProperty("requestId", requestId);
        requestBody.addProperty("amount", amount);
        requestBody.addProperty("orderId", orderId);
        requestBody.addProperty("orderInfo", orderInfo);
        requestBody.addProperty("redirectUrl", redirectUrl);
        requestBody.addProperty("ipnUrl", ipnUrl);
        requestBody.addProperty("lang", "vi");
        requestBody.addProperty("extraData", extraData);
        requestBody.addProperty("requestType", requestType);
        requestBody.addProperty("signature", signature);

        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(MoMoConfig.MOMO_ENDPOINT))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .timeout(Duration.ofSeconds(15))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString(), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            JsonObject responseJson = JsonParser.parseString(httpResponse.body()).getAsJsonObject();
            int resultCode = responseJson.has("resultCode") ? responseJson.get("resultCode").getAsInt() : -1;

            if (resultCode == 0 && responseJson.has("payUrl")) {
                String payUrl = responseJson.get("payUrl").getAsString();
                response.sendRedirect(payUrl);
            } else {
                String errorMsg = responseJson.has("message") ? responseJson.get("message").getAsString() : "Lỗi không xác định từ MoMo";
                request.setAttribute("errorMessage", "MoMo Error (" + resultCode + "): " + errorMsg);
                request.getRequestDispatcher("/cart.jsp").forward(request, response);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            response.sendRedirect(request.getContextPath() + "/cart.jsp");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi kết nối cổng MoMo: " + e.getMessage());
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
