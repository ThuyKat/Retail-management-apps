<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Search Results</title>
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
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
       
    </style>
</head>
<body>
    <h1>Order Search Results</h1>
    
    <c:if test="${not empty orders}">
        <table>
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>SalePerson</th>
                    <th>Order Date</th>
                    <th>Status</th>
                    <th>Total Price</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <td>${order.id}</td>
                        <td>${order.user.username}</td>
<%--                         <fmt:parseDate value="${order.date}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" type="both" />
 --%>                        <td>${fn:replace(order.date, 'T', ' ')}</td>
                        <td>${order.status}</td>
                        <td><fmt:formatNumber value="${order.totalPrice}" type="currency" currencyCode="AUD" /></td>
   						<td>
                        <a href="/order/viewOrderList?action=viewSavedOrder&orderId=${order.id}">Update</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
     
    
</body>
</html>