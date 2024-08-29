<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Confirmation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            background-color: white;
            border-radius: 5px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: #333;
        }
        .message {
            margin-bottom: 20px;
        }
        .button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-right: 10px;
        }
    </style>
</head>
<body>
<jsp:include page="navbar.jsp" />
    <div class="container">
        <h1>Order Confirmation</h1>
        <div class="message">
            <p>${message}</p>
            <% if(request.getAttribute("orderId") != null) { %>
                <p>Order ID: ${orderId}</p>
            <% } %>
        </div>
        <div>
            <a href="/order/viewOrderList" class="button">View Order List</a>
            <a href="/order?action=resetOrder" class="button">New Order</a>
        </div>
    </div>
</body>
</html>