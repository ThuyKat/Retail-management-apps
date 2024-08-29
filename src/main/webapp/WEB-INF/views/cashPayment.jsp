<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cash Payment</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f0f0f0;
        }
        .container {
            text-align: center;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #003087; /* PayPal blue */
            color: white;
        }
        button {
            padding: 10px 20px;
            font-size: 16px;
            margin: 10px;
            cursor: pointer;
        }
    </style>
</head>
<body>

    <div class="container">
        <h1>Cash Payment</h1>
        <p>Please confirm your cash payment.</p>

        <table>
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Subtotal</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="orderDetail" items="${order.orderDetails}">
                    <tr>
                        <td>${orderDetail.product.name}</td>
                        <td>${orderDetail.quantity}</td>
                        <td>$<fmt:formatNumber value="${orderDetail.product.price}" pattern="#,##0.00"/></td>
                        <td>$<fmt:formatNumber value="${orderDetail.quantity * orderDetail.product.price}" pattern="#,##0.00"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <h2>Total: $<fmt:formatNumber value="${order.totalPrice}" pattern="#,##0.00"/></h2>

        <button onclick="window.location.href='/order/placeOrder?action=cash-confirm'">Paid</button>
        <form action="/order/placeOrder" method="post">
                	<input type="hidden" name="action" value="select-payment">
                	<input type="hidden" name="customerInfo" value="${customerInfo}">
        			<input type="hidden" name="orderNote" value="${orderNote}">
    	<button type="submit">Cancel</button>
		</form>
    </div>
</body>
</html>