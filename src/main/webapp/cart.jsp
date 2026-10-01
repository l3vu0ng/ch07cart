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
                <form action="vnpay-pay" method="post">
                    <input type="submit" value="Thanh toán qua VNPay Sandbox" style="background-color: #e51f28; color: white; font-weight: bold; border: none; padding: 7px 16px; border-radius: 4px; cursor: pointer;">
                </form>
            </c:if>
        </div>
    </body>
</html>
