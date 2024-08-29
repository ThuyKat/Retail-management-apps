<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="forgotPassword.title"/></title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>
<body>
    <div class="container">
        <h1><spring:message code="forgotPassword.heading"/></h1>
        
        <c:if test="${not empty message}">
            <div class="alert alert-success">
                ${message}
            </div>
            <p><spring:message code="resetPassword.instruction"/></p>
       
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                ${error}
            </div>
        </c:if>
        
        <c:if test="${empty message and empty error}">
            <p><spring:message code="forgotPassword.instructions"/></p>
        </c:if>
        
        <div class="actions">
            <a href="<c:url value='/login'/>" class="btn btn-primary">
                <spring:message code="forgotPassword.backToLogin"/>
            </a>
        </div>
    </div>

</body>
</html>