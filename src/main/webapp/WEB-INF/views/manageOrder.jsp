<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Order</title>
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>
	<jsp:include page="navbar.jsp" />

	<div class="container">
    <h1>Manage Order</h1>
    <div class="options">
        <a href="/order/viewOrderList" class="option">Current Orders</a>
        <a href="/index" class="option">Take Order</a>
        <a href="/order/search" class="option">Search And Filters</a>
    </div>
    </div>
    <%-- <footer>
            <c:if test="${not empty sessionScope.previousPage}">
            <a href="${sessionScope.previousPage}" class="option">&lt; Back</a>
        </c:if>
    </footer> --%>
</body>
</html>