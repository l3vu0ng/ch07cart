package murach.momo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "MoMoReturnServlet", urlPatterns = {"/momo-return"})
public class MoMoReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String partnerCode = request.getParameter("partnerCode");
        String orderId = request.getParameter("orderId");
        String requestId = request.getParameter("requestId");
        String amount = request.getParameter("amount");
        String orderInfo = request.getParameter("orderInfo");
        String orderType = request.getParameter("orderType");
        String transId = request.getParameter("transId");
        String resultCode = request.getParameter("resultCode");
        String message = request.getParameter("message");
        String payType = request.getParameter("payType");
        String responseTime = request.getParameter("responseTime");
        String extraData = request.getParameter("extraData");
        String signature = request.getParameter("signature");

        if (extraData == null) {
            extraData = "";
        }

        String accessKey = MoMoConfig.getAccessKey();
        String secretKey = MoMoConfig.getSecretKey();

        // Xây dựng rawSignature theo chuẩn xác thực Redirect URL của MoMo
        String rawSignature = "accessKey=" + accessKey
                + "&amount=" + (amount != null ? amount : "")
                + "&extraData=" + extraData
                + "&message=" + (message != null ? message : "")
                + "&orderId=" + (orderId != null ? orderId : "")
                + "&orderInfo=" + (orderInfo != null ? orderInfo : "")
                + "&orderType=" + (orderType != null ? orderType : "")
                + "&partnerCode=" + (partnerCode != null ? partnerCode : "")
                + "&payType=" + (payType != null ? payType : "")
                + "&requestId=" + (requestId != null ? requestId : "")
                + "&responseTime=" + (responseTime != null ? responseTime : "")
                + "&resultCode=" + (resultCode != null ? resultCode : "")
                + "&transId=" + (transId != null ? transId : "");

        String expectedSignature = MoMoConfig.hmacSHA256(secretKey, rawSignature);

        boolean isSignatureValid = expectedSignature.equalsIgnoreCase(signature);
        boolean isSuccess = isSignatureValid && "0".equals(resultCode);

        String displayMessage;
        if (!isSignatureValid) {
            displayMessage = "Chữ ký không hợp lệ! Dữ liệu phản hồi có thể đã bị can thiệp.";
        } else if ("0".equals(resultCode)) {
            displayMessage = "Thanh toán qua ví MoMo thành công!";
            // Xóa giỏ hàng
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute("cart");
            }
        } else if ("1006".equals(resultCode) || "49".equals(resultCode)) {
            displayMessage = "Giao dịch đã bị người dùng hủy.";
        } else {
            displayMessage = "Giao dịch thanh toán không thành công. (" + message + ")";
        }

        long displayAmount = 0;
        if (amount != null && !amount.isEmpty()) {
            try {
                displayAmount = Long.parseLong(amount);
            } catch (NumberFormatException ignored) {
            }
        }

        request.setAttribute("isSuccess", isSuccess);
        request.setAttribute("message", displayMessage);
        request.setAttribute("orderId", orderId);
        request.setAttribute("amount", displayAmount);
        request.setAttribute("transId", transId);
        request.setAttribute("payType", payType);
        request.setAttribute("responseTime", responseTime);

        request.getRequestDispatcher("/momo_return.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
