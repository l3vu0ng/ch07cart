<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Kết quả thanh toán MoMo Sandbox</title>
        <link rel="stylesheet" href="styles/main.css" type="text/css"/>
        <style>
            .status-box {
                padding: 16px 20px;
                border-radius: 6px;
                margin-bottom: 20px;
                max-width: 600px;
            }
            .success-box {
                background-color: #fdf2f8;
                border: 1px solid #d82d8b;
                color: #a50064;
            }
            .error-box {
                background-color: #fff1f2;
                border: 1px solid #e11d48;
                color: #be123c;
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
                background-color: #a50064;
                color: white;
                border: none;
                padding: 10px 20px;
                font-size: 11pt;
                font-weight: bold;
                border-radius: 4px;
                cursor: pointer;
            }
            .btn-action:hover {
                background-color: #83004f;
            }
        </style>
    </head>
    <body>
        <h1 style="color: #a50064;">Kết Quả Thanh Toán MoMo Sandbox</h1>

        <c:choose>
            <c:when test="${isSuccess}">
                <div class="status-box success-box">
                    <h2 style="margin: 0 0 6px 0; font-size: 1.3em;">✔ ${message}</h2>
                    <p style="margin: 0;">Đơn hàng của bạn đã được thanh toán thành công qua cổng thanh toán MoMo Sandbox.</p>
                </div>

                <table class="result-table">
                    <tr>
                        <td style="width: 40%;"><b>Mã đơn hàng:</b></td>
                        <td>${orderId}</td>
                    </tr>
                    <tr>
                        <td><b>Số tiền thanh toán:</b></td>
                        <td style="font-weight: bold; color: #a50064;">
                            <fmt:formatNumber value="${amount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </td>
                    </tr>
                    <tr>
                        <td><b>Mã giao dịch MoMo:</b></td>
                        <td>${transId}</td>
                    </tr>
                    <c:if test="${not empty payType}">
                        <tr>
                            <td><b>Phương thức thanh toán:</b></td>
                            <td>${payType}</td>
                        </tr>
                    </c:if>
                </table>
            </c:when>
            <c:otherwise>
                <div class="status-box error-box">
                    <h2 style="margin: 0 0 6px 0; font-size: 1.3em;">✘ ${message}</h2>
                    <p style="margin: 0;">Giao dịch chưa hoàn thành. Bạn có thể quay lại giỏ hàng để thử lại.</p>
                </div>
            </c:otherwise>
        </c:choose>

        <form action="cart" method="post">
            <input type="hidden" name="action" value="shop">
            <input type="submit" value="Tiếp tục mua sắm" class="btn-action">
        </form>
    </body>
</html>
