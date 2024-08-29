<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Owner Home</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f4f8;
            color: #333;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #3498db;
        }
        .options {
            display: flex;
            flex-direction: column;
            gap: 15px;
            margin-top: 20px;
        }
        .option {
            padding: 15px;
            background-color: #3498db;
            color: #fff;
            text-align: center;
            border-radius: 5px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }
        .option:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
	<jsp:include page="navbar.jsp" />
    <div class="container">
    <c:if test="${userRole == 'Owner'}">
        <h1>Owner Dashboard</h1>
      </c:if>
      <c:if test="${userRole == 'Manager'}">
        <h1>Manager Dashboard</h1>
      </c:if>
         
        <div class="options">
            <a href="/manageUser" class="option">Manage User</a>
             <!-- Show this option only if the user is an owner -->
        <c:if test="${userRole == 'Owner'}">
            <a href="/manageCategory" class="option">Manage Category</a>
            <a href="/manageProduct" class="option">Manage Product</a>
        </c:if>
            <a href="/manageOrder" class="option">Manage Order</a>
            <a href="/viewReport" class="option">View Report</a>
        </div>
    </div>
</body>
</html>