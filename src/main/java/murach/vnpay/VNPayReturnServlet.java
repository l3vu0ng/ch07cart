package murach.vnpay;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "VNPayReturnServlet", urlPatterns = {"/vnpay-return"})
public class VNPayReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String secretKey = VNPayConfig.getHashSecret();
        String signValue = VNPayConfig.hashAllFields(fields, secretKey);

        boolean isSuccess = false;
        String message;

        if (signValue.equalsIgnoreCase(vnp_SecureHash)) {
            String responseCode = request.getParameter("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                isSuccess = true;
                message = "Giao dịch thanh toán thành công!";
                // Xóa giỏ hàng sau khi thanh toán hoàn tất
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.removeAttribute("cart");
                }
            } else if ("24".equals(responseCode)) {
                message = "Khách hàng đã hủy giao dịch thanh toán.";
            } else {
                message = "Giao dịch không thành công. Mã phản hồi lỗi: " + responseCode;
            }
        } else {
            message = "Chữ ký không hợp lệ! Dữ liệu có thể đã bị can thiệp.";
        }

        request.setAttribute("isSuccess", isSuccess);
        request.setAttribute("message", message);
        request.setAttribute("orderId", request.getParameter("vnp_TxnRef"));

        String amountParam = request.getParameter("vnp_Amount");
        long displayAmount = 0;
        if (amountParam != null && !amountParam.isEmpty()) {
            try {
                displayAmount = Long.parseLong(amountParam) / 100;
            } catch (NumberFormatException ignored) {
            }
        }
        request.setAttribute("amount", displayAmount);
        request.setAttribute("bankCode", request.getParameter("vnp_BankCode"));
        request.setAttribute("transactionNo", request.getParameter("vnp_TransactionNo"));
        request.setAttribute("payDate", request.getParameter("vnp_PayDate"));
        request.setAttribute("responseCode", request.getParameter("vnp_ResponseCode"));

        request.getRequestDispatcher("/vnpay_return.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
