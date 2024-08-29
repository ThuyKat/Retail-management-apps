<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Order</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .button, .link {
            display: inline-block;
            padding: 10px 15px;
            margin: 10px 0;
            color: white;
            background-color: #007bff; /* Bootstrap primary color */
            text-decoration: none; /* Remove underline from link */
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .link {
            background-color: #28a745; /* Green for continue shopping */
        }
        .button:hover, .link:hover {
            opacity: 0.8; /* Slightly dim on hover */
        }
        .input-field {
            width: 100%;
            padding: 8px;
            margin: 10px 0;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
    </style> 
</head>
<body>
    <h1>Order Details</h1>
    
    <c:if test="${empty order.orderDetails}">
        <p>Your order is empty.</p>
    </c:if>
    
    <c:if test="${not empty order.orderDetails}">
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
        
        <!-- Form for additional order information -->
        <form method="post" action="/order/placeOrder">
        	<input type="hidden" name="action" value="select-payment">
        	
            <label for="customerInfo">Customer Information:</label>
            <input type="text" id="customerInfo" name="customerInfo" class="input-field" placeholder="Enter customer information" required>
            
            <label for="orderNote">Order Notes:</label>
            <textarea id="orderNote" name="orderNote" class="input-field" placeholder="Enter any additional notes" rows="4"></textarea>
            
            <button type="submit" class="button">Place Order</button>
        </form>
    </c:if>
    
    <a href="/" class="link">Continue Shopping</a>
</body>
</html>