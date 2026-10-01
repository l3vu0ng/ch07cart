<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Murach's Java Servlets and JSP</title>
        <link rel="stylesheet" href="styles/main.css" type="text/css"/>
    </head>
    <body>
        <h1>Checkout</h1>
        <p>Thank you for your order!</p>
        <form action="cart" method="post">
            <input type="hidden" name="action" value="shop">
            <input type="submit" value="Continue Shopping">
        </form>
    </body>
</html>
