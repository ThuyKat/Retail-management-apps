<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
         <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Product</title>
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>
	<jsp:include page="navbar.jsp" />
    <div class="container">
    <h1>Manage Product</h1>
    <div class="options">
        <a href="/product/form" class="option">Add New Product</a>
        <a href="/product/currentProductList" class="option">Current Product List</a>
    </div>
    </div>
  <%--   <footer>
            <c:if test="${not empty sessionScope.previousPage}">
            <a href="${sessionScope.previousPage}" class="option">&lt; Back</a>
        </c:if>
            
        
    </footer> --%>
</body>
</html>