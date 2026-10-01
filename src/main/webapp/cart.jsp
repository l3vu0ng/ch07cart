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
        </div>
    </body>
</html>
