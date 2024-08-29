<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            padding: 20px;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
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
    </style>
</head>
<body>
	<jsp:include page="navbar.jsp" />
    <div class="container">
        <h1>Processing Orders</h1>
        <table>
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Customer Info</th>
                    <th>Payment Method</th>
                    <th>Order Value</th>
                    <th>Status</th>
                    <th> </th>
                </tr>
            </thead>
            <tbody>
             <c:if test="${empty orderList}">
        <p>No Order Found</p>
    </c:if>
    
    <c:if test="${not empty orderList}">
                <c:forEach var="order" items="${orderList}">
                    <tr>
                        <td>${order.id}</td>
                        <td>${order.customerInfo}</td>
                        <td>${order.paymentMethod}</td>
                        <td>${order.totalValue}</td>
                        <td>${order.status}</td>
                        <td>
                        <a href="/order/viewOrderList?action=viewSavedOrder&orderId=${order.id}">Update </a>
                        </td>
                        
                    </tr>
                </c:forEach>
              </c:if>
            </tbody>
        </table>
        <c:choose>
        <c:when test="${sessionScope.sourcePage == 'confirm'}">
            <a href="/index">Back to Order</a>
        </c:when>
        <c:when test="${sessionScope.sourcePage == 'manageOrder'}">
            <a href="/manageOrder">Back </a>
        </c:when>
    </c:choose>
    </div>
</body>
</html>