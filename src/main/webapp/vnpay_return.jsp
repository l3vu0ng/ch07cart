<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Kết quả thanh toán VNPay Sandbox</title>
        <link rel="stylesheet" href="styles/main.css" type="text/css"/>
        <style>
            .status-box {
                padding: 15px;
                border-radius: 5px;
                margin-bottom: 20px;
                max-width: 600px;
            }
            .success-box {
                background-color: #e8f5e9;
                border: 1px solid #4caf50;
                color: #2e7d32;
            }
            .error-box {
                background-color: #ffebee;
                border: 1px solid #f44336;
                color: #c62828;
            }
            .result-table {
                width: 100%;
                max-width: 600px;
                margin-bottom: 25px;
            }
            .result-table td {
                padding: 8px 12px;
            }
            .btn-action {
                background-color: #008080;
                color: white;
                border: none;
                padding: 10px 18px;
                font-size: 11pt;
                font-weight: bold;
                border-radius: 4px;
                cursor: pointer;
            }
            .btn-action:hover {
                background-color: #006666;
            }
        </style>
    </head>
    <body>
        <h1>Kết Quả Thanh Toán VNPay</h1>

        <c:choose>
            <c:when test="${isSuccess}">
                <div class="status-box success-box">
                    <h2 style="margin: 0 0 8px 0; font-size: 1.3em;">✔ ${message}</h2>
                    <p style="margin: 0;">Đơn hàng của bạn đã được thanh toán thành công qua cổng thanh toán VNPay Sandbox.</p>
                </div>

                <table class="result-table">
                    <tr>
                        <td style="width: 40%;"><b>Mã tham chiếu đơn hàng:</b></td>
                        <td>${orderId}</td>
                    </tr>
                    <tr>
                        <td><b>Số tiền đã thanh toán:</b></td>
                        <td style="font-weight: bold; color: #008080;">
                            <fmt:formatNumber value="${amount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </td>
                    </tr>
                    <tr>
                        <td><b>Ngân hàng thực hiện:</b></td>
                        <td>${bankCode}</td>
                    </tr>
                    <tr>
                        <td><b>Mã giao dịch VNPay:</b></td>
                        <td>${transactionNo}</td>
                    </tr>
                    <c:if test="${not empty payDate}">
                        <tr>
                            <td><b>Thời gian thanh toán:</b></td>
                            <td>${payDate}</td>
                        </tr>
                    </c:if>
                </table>
            </c:when>
            <c:otherwise>
                <div class="status-box error-box">
                    <h2 style="margin: 0 0 8px 0; font-size: 1.3em;">✘ ${message}</h2>
                    <p style="margin: 0;">Giao dịch chưa hoàn thành hoặc bị lỗi. Vui lòng kiểm tra lại.</p>
                </div>
            </c:otherwise>
        </c:choose>

        <form action="cart" method="post">
            <input type="hidden" name="action" value="shop">
            <input type="submit" value="Tiếp tục mua sắm" class="btn-action">
        </form>
    </body>
</html>
