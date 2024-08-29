<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Reports</title>
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>
	<jsp:include page="navbar.jsp" />

  
    <div class="container">
    <h1>View Reports</h1>
    <div class="options">
    	<a href="/report/ordersByStatus" class="option">Order Reports</a>
        <a href="/report/salesByProduct" class="option">Sales By Product Reports</a>
        <a href="/report/salesByUser" class="option">Sales By User Reports</a>
<!--         <a href="/productReports" class="option">Product Reports</a>
 -->         
    </div>
    </div>
  <%--   <footer>
            <c:if test="${not empty sessionScope.previousPage}">
            <a href="${sessionScope.previousPage}" class="option">&lt; Back</a>
        </c:if>
    </footer> --%>
</body>
</html>