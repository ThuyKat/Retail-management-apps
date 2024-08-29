<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Order</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f4f4f4;
        }
        h1, h2 {
            color: #2c3e50;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #3498db;
            color: white;
        }
        .total {
            font-size: 1.2em;
            font-weight: bold;
            text-align: right;
            margin-top: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        select, textarea {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        hr {
            border: 0;
            height: 1px;
            background: #ddd;
            margin: 20px 0;
        }
    </style>
</head>
<body>
<jsp:include page="navbar.jsp" />
    <h1>Update Order</h1>
       <!-- <div>
       <a href="/order/viewOrderList" class="btn btn-secondary">
        Back to Order List
    </a> -->
   <!--  </div> -->
    <c:choose>
        <c:when test="${empty order}">
            <p>${errorMessage}</p>
        </c:when>
        <c:otherwise>
            <h2>Order Details</h2>
            <c:if test="${empty orderDetails}">
                <p>Your order is empty.</p>
            </c:if>
            <c:if test="${not empty orderDetails}">
                <table>
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>SubTotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="orderDetail" items="${orderDetails}">
                            <tr>
                                <td>${orderDetail.product.name}</td>
                                <td>${orderDetail.quantity}</td>
                                <td>$<fmt:formatNumber value="${orderDetail.product.price}" pattern="#,##0.00"/></td>
                                <td>$<fmt:formatNumber value="${orderDetail.quantity * orderDetail.product.price}" pattern="#,##0.00"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div class="total">
                    Total: $<fmt:formatNumber value="${order.totalPrice}" pattern="#,##0.00"/>
                </div>
                <div class="form-group">
                    <label for="orderNotes">Order Notes:</label>
                    <textarea id="orderNote" name="orderNote" rows="4" readonly>${order.orderNote}</textarea>
                </div>
            </c:if>
            
            <form id="statusForm" method="post" action="/order/viewSavedOrder">
            	<input type="hidden" name="action" value="updateOrderStatus">
                <input type="hidden" name="orderId" value="${orderId}">
                <input type="hidden" name="userId" value="${userId}">
                <div class="form-group">
                    <label for="orderStatus">Order Status:</label>
                    <select id="orderStatus" name="orderStatus">
                        <c:forEach var="status" items="${orderStatuses}">
                            <option value="${status}" ${status == currentStatus ? 'selected' : ''}>${status}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Update Status</button>
                </form>
                <hr>
                <form id="commentForm" method="post" action="/order/viewSavedOrder">
                <input type="hidden" name="action" value="addComment">
    			<input type="hidden" name="orderId" value="${orderId}">
    			<input type="hidden" name="userId" value="${userId}">
    			<jsp:include page="orderComment.jsp"/>
				</form>
            
        </c:otherwise>
    </c:choose>
</body>
</html>