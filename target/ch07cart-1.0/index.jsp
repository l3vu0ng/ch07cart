<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%
    if (application.getAttribute("products") == null && session.getAttribute("products") == null) {
        String path = application.getRealPath("/WEB-INF/products.txt");
        java.util.ArrayList<murach.business.Product> products = murach.data.ProductIO.getProducts(path);
        application.setAttribute("products", products);
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Murach's Java Servlets and JSP</title>
        <link rel="stylesheet" href="styles/main.css" type="text/css"/>
    </head>
    <body>
        <h1>CD list</h1>
        <table>
            <tr>
                <th style="width: 65%;">Description</th>
                <th class="price" style="width: 15%;">Price</th>
                <th style="width: 20%;"></th>
            </tr>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>${product.description}</td>
                    <td class="price">${product.priceCurrencyFormat}</td>
                    <td>
                        <form action="cart" method="post">
                            <input type="hidden" name="productCode" value="${product.code}">
                            <input type="submit" value="Add To Cart">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
