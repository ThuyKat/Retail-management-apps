<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><spring:message code="resetPassword.title" /></title>
<style>body {
    font-family: Arial, sans-serif;
    background-color: #f4f4f4;
    margin: 0;
    padding: 0;
}

.container {
    max-width: 400px;
    margin: 50px auto;
    padding: 20px;
    background-color: #ffffff;
    border-radius: 5px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h1 {
    text-align: center;
    color: #333;
    margin-bottom: 20px;
}

.alert {
    padding: 10px;
    margin-bottom: 15px;
    border-radius: 4px;
}

.alert-info {
    background-color: #d9edf7;
    border: 1px solid #bce8f1;
    color: #31708f;
}

.alert-danger {
    background-color: #f2dede;
    border: 1px solid #ebccd1;
    color: #a94442;
}

.form-group {
    margin-bottom: 15px;
}

label {
    display: block;
    margin-bottom: 5px;
    color: #666;
}

.form-control {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    box-sizing: border-box;
}

.btn {
    display: inline-block;
    padding: 10px 20px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s;
}

.btn:hover {
    background-color: #0056b3;
}

.mt-3 {
    margin-top: 15px;
}

a {
    color: #007bff;
    text-decoration: none;
}

a:hover {
    text-decoration: underline;
}</style>
</head>
<body>
	<div class="container">
		<h1>
			<spring:message code="resetPassword.heading" />
		</h1>

		<c:if test="${not empty message}">
			<div class="alert alert-info">${message}</div>
		</c:if>

		<c:if test="${not empty error}">
			<div class="alert alert-danger">${error}</div>
		</c:if>

		<form action="<c:url value='/resetPassword'/>" method="POST">
			<input type="hidden" name="token" value="${token}" />

			<div class="form-group">
				<label for="password"><spring:message
						code="resetPassword.newPassword" /></label> <input type="password"
					id="password" name="password" required class="form-control" />
			</div>

			<div class="form-group">
				<label for="confirmPassword"><spring:message
						code="resetPassword.confirmPassword" /></label> <input type="password"
					id="confirmPassword" name="confirmPassword" required
					class="form-control" />
			</div>

			<button type="submit" class="btn btn-primary">
				<spring:message code="resetPassword.submit" />
			</button>
		</form>

		<div class="mt-3">
			<a href="<c:url value='/login'/>"><spring:message
					code="resetPassword.backToLogin" /></a>
		</div>
	</div>

</body>
</html>