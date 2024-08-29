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
<link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
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

		<form action="<c:url value='/user/resetPassword'/>" method="POST">
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