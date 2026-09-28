<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Order Search and Filter</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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

h2 {
	color: #3498db;
	margin-bottom: 20px;
}

.inline-form {
	background-color: white;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.inline-form {
	display: grid;
	grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
	gap: 15px;
}

label {
	font-weight: bold;
	display: block;
	margin-bottom: 5px;
	color: #333;
}

input, select {
	width: 100%;
	padding: 8px;
	border: 1px solid #ddd;
	border-radius: 4px;
	box-sizing: border-box;
}

.search-button {
	background-color: #3498db;
	color: white;
	padding: 10px 15px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-weight: bold;
	transition: background-color 0.3s;
}

.search-button:hover {
	background-color: #2980b9;
}

.no-results {
	text-align: center;
	padding: 20px;
	font-style: italic;
	background-color: #fff;
	border-radius: 8px;
	margin-top: 20px;
}

.clear-filters {
	display: block;
	text-align: center;
	margin-top: 20px;
	color: #3498db;
	text-decoration: none;
}

.clear-filters:hover {
	text-decoration: underline;
}
</style>
</head>
<body>
	<jsp:include page="navbar.jsp" />
	<div class="container">
		<h2>Order Search and Filter</h2>
		<form action="/order/search/result" method="GET" class="inline-form">
			<div>
				<label for="orderStatus">Order Status:</label> <select
					name="orderStatus" id="orderStatus">
					<option value="">All Statuses</option>
					<c:forEach items="${orderStatuses}" var="status">
						<option value="${status}">${status}</option>
					</c:forEach>
				</select>
			</div>
			<div>
				<label for="username">Username:</label> <select name="username"
					id="username">
					<option value="">All Users</option>
					<c:forEach items="${usernames}" var="user">
						<option value="${user}">${user}</option>
					</c:forEach>
				</select>
			</div>
			<div>
				<label for="startDate">Start Date:</label> 
				<input type="date" id="startDate" name="startDate">
			</div>
			<div>
				<label for="endDate">End Date:</label> 
				<input type="date" id="endDate" name="endDate">
			</div>
			<div>
				<label for="minPrice">Minimum Price:</label>
				 <input type="number" id="minPrice" name="minPrice" min="0" step="0.01">
			</div>
			<div>
				<label for="maxPrice">Maximum Price:</label>
				 <input type="number" id="maxPrice" name="maxPrice" min="0" step="0.01">
			</div>
			<div>
				<label for="customerInfo">Customer Info:</label>
				 <input type="text" id="customerInfo" name="customerInfo">
			</div>
			<button type="submit" class="search-button">
				<i class="fas fa-search"></i> Search
			</button>
		</form>
		<br>
		<c:if test="${isSubmit}">
			<c:choose>
				<c:when test="${not empty orders}">
					<jsp:include page="orderSearchResults.jsp" />
				</c:when>
				<c:otherwise>
					<p class="no-results">No orders found matching your search
						criteria.</p>
				</c:otherwise>
			</c:choose>
			<a href="/order/search" class="clear-filters">Clear All Filters</a>
		</c:if>
	</div>
</body>
</html>