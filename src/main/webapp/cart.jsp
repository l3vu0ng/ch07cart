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
        <h1>Your cart</h1>
        <table>
            <tr>
                <th style="width: 22%;">Quantity</th>
                <th style="width: 43%;">Description</th>
                <th class="price" style="width: 10%;">Price</th>
                <th class="price" style="width: 10%;">Amount</th>
                <th style="width: 15%;"></th>
            </tr>
            <c:forEach var="item" items="${cart.items}">
                <tr>
                    <td>
                        <form action="cart" method="post">
                            <input type="hidden" name="productCode" value="${item.product.code}">
                            <input type="text" name="quantity" value="${item.quantity}" id="quantity">
                            <input type="submit" value="Update">
                        </form>
                    </td>
                    <td>${item.product.description}</td>
                    <td class="price">${item.product.priceCurrencyFormat}</td>
                    <td class="price">${item.totalCurrencyFormat}</td>
                    <td>
                        <form action="cart" method="post">
                            <input type="hidden" name="productCode" value="${item.product.code}">
                            <input type="hidden" name="quantity" value="0">
                            <input type="submit" value="Remove Item">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <c:if test="${not empty errorMessage}">
            <p style="color: red; font-weight: bold; padding: 10px; background-color: #ffebee; border: 1px solid #f44336; border-radius: 4px; max-width: 750px;">
                ${errorMessage}
            </p>
        </c:if>

        <c:if test="${not empty cart and cart.count > 0}">
            <p style="max-width: 750px; text-align: right; font-size: 1.15em; font-weight: bold; margin-top: 15px;">
                Tổng tiền: <span style="color: #008080;">${cart.totalCurrencyFormat}</span>
            </p>
        </c:if>

        <p><b>To change the quantity</b>, enter the new quantity and click on the Update button.</p>

        <div class="btn-group">
            <form action="cart" method="post">
                <input type="hidden" name="action" value="shop">
                <input type="submit" value="Continue Shopping">
            </form>
            <form action="cart" method="post">
                <input type="hidden" name="action" value="checkOut">
                <input type="submit" value="Checkout">
            </form>
            <c:if test="${not empty cart and cart.count > 0}">
                <form action="momo-pay" method="post" style="margin-right: 8px;">
                    <input type="hidden" name="requestType" value="payWithATM">
                    <input type="submit" value="Thanh toán MoMo (Thẻ ATM Test)" style="background-color: #a50064; color: white; font-weight: bold; border: none; padding: 8px 18px; border-radius: 4px; cursor: pointer;">
                </form>
                <form action="momo-pay" method="post">
                    <input type="hidden" name="requestType" value="captureWallet">
                    <input type="submit" value="Thanh toán MoMo (Quét mã QR App)" style="background-color: #d82d8b; color: white; font-weight: bold; border: none; padding: 8px 18px; border-radius: 4px; cursor: pointer;">
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
            </c:if>
        </div>
    </body>
</html>
