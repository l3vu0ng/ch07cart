<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Murach's Java Servlets and JSP</title>
        <link rel="stylesheet" href="styles/main.css" type="text/css"/>
    </head>
    <body>
        <h1>Checkout</h1>
        <c:choose>
            <c:when test="${not empty cart and cart.count > 0}">
                <p>Tổng giá trị đơn hàng của bạn: <b style="color: #008080;">${cart.totalCurrencyFormat}</b></p>
                <div class="btn-group">
                    <form action="vnpay-pay" method="post">
                        <input type="submit" value="Thanh toán qua cổng VNPay Sandbox" style="background-color: #e51f28; color: white; font-weight: bold; border: none; padding: 8px 18px; border-radius: 4px; cursor: pointer;">
                    </form>
                    <form action="cart" method="post" style="margin-top: 10px;">
                        <input type="hidden" name="action" value="cart">
                        <input type="submit" value="Quay lại giỏ hàng">
                    </form>
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
