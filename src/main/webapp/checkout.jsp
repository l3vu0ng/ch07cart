<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="utf-8">
            <title>Murach's Java Servlets and JSP</title>
            <link rel="stylesheet" href="styles/main.css" type="text/css" />
        </head>

        <body>
            <h1>Checkout</h1>
            <c:choose>
                <c:when test="${not empty cart and cart.count > 0}">
                    <p>Tổng giá trị đơn hàng của bạn: <b style="color: #008080;">${cart.totalCurrencyFormat}</b></p>
                    <div class="btn-group">
                        <form action="momo-pay" method="post" style="margin-bottom: 8px;">
                            <input type="hidden" name="requestType" value="payWithATM">
                            <input type="submit" value="Thanh toán MoMo (Thẻ ATM Test)"
                                style="background-color: #a50064; color: white; font-weight: bold; border: none; padding: 8px 18px; border-radius: 4px; cursor: pointer;">
                        </form>
                        <form action="momo-pay" method="post" style="margin-bottom: 8px;">
                            <input type="hidden" name="requestType" value="captureWallet">
                            <input type="submit" value="Thanh toán MoMo (Quét mã QR App)"
                                style="background-color: #d82d8b; color: white; font-weight: bold; border: none; padding: 8px 18px; border-radius: 4px; cursor: pointer;">
                        </form>
                        <form action="cart" method="post">
                            <input type="hidden" name="action" value="cart">
                            <input type="submit" value="Quay lại giỏ hàng">
                        </form>

                        <div style="margin-top: 25px; max-width: 650px; background-color: #fdf8fb; border: 1px dashed #d82d8b; border-radius: 6px; padding: 12px 18px; color: #444; font-size: 10pt;">
                            <div style="font-weight: bold; color: #a50064; margin-bottom: 8px; font-size: 10.5pt;">
                                💳 Thông tin thẻ thử nghiệm MoMo Sandbox (Dùng để test):
                            </div>
                            <table style="width: 100%; border: none; font-size: 9.5pt; background: transparent; max-width: none;">
                                <tr style="border: none;">
                                    <td style="border: none; padding: 4px 6px; width: 25%;"><b>Số thẻ:</b></td>
                                    <td style="border: none; padding: 4px 6px; font-family: monospace; color: #a50064; font-weight: bold;">9704 0000 0000 0018</td>
                                    <td style="border: none; padding: 4px 6px; width: 25%;"><b>Ngày phát hành:</b></td>
                                    <td style="border: none; padding: 4px 6px; font-family: monospace;">03/07</td>
                                </tr>
                                <tr style="border: none;">
                                    <td style="border: none; padding: 4px 6px;"><b>Tên chủ thẻ:</b></td>
                                    <td style="border: none; padding: 4px 6px; font-family: monospace;">NGUYEN VAN A</td>
                                    <td style="border: none; padding: 4px 6px;"><b>Số điện thoại:</b></td>
                                    <td style="border: none; padding: 4px 6px; font-family: monospace;">0988888888</td>
                                </tr>
                                <tr style="border: none;">
                                    <td style="border: none; padding: 4px 6px;"><b>Mã OTP:</b></td>
                                    <td colspan="3" style="border: none; padding: 4px 6px; font-family: monospace; color: #d82d8b; font-weight: bold; font-size: 11pt;">OTP</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <p>Giỏ hàng của bạn hiện đang trống.</p>
                    <form action="cart" method="post">
                        <input type="hidden" name="action" value="shop">
                        <input type="submit" value="Tiếp tục mua sắm">
                    </form>
                </c:otherwise>
            </c:choose>
        </body>

        </html>