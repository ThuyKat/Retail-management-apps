<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
    .order-details {
        font-family: Arial, sans-serif;
        max-width: 1000px;
        margin: 0 auto;
        padding: 20px;
    }

    .order-details h1 {
        color: #333;
        text-align: center;
        margin-bottom: 20px;
    }

    .order-details p {
        background-color: #f4f4f4;
        padding: 10px;
        border-radius: 5px;
        margin-bottom: 20px;
    }

    .order-details table {
        width: 100%;
        border-collapse: collapse;
        box-shadow: 0 2px 5px rgba(0,0,0,0.1);
    }

    .order-details th, .order-details td {
        padding: 12px;
        text-align: left;
        border-bottom: 1px solid #ddd;
    }

    .order-details th {
        background-color: #4CAF50;
        color: white;
    }

    .order-details tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    .order-details tr:hover {
        background-color: #ddd;
    }
</style>

<div class="order-details">
<h1>Order Details Report</h1>
    <p>Start Date: ${startDate} | End Date: ${endDate} | Status: ${status}</p>
    
    <table>
        <thead>
            <tr>
                <th>Order ID</th>
                <th>Sales Person</th>
                <th>Order Date</th>
                <th>Total</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${orders}" var="order">
                <tr>
                    <td>${order.id}</td>
                    <td>${order.user.username}</td>
                    <td>${fn:replace(order.date, 'T', ' ')}</td>
                    <td><fmt:formatNumber value="${order.totalPrice}" type="currency" currencyCode="AUD"/></td>
                    <td>${order.status}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
