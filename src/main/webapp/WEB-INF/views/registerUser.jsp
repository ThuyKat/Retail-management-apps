<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register User</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/userForm.css">

</head>
<body>
	<jsp:include page="navbar.jsp" />
    <div class="container">
        <h1>Register Form</h1>
        <form action="${pageContext.request.contextPath}/user/register" method="post">
            <div class="form-group">
                <label for="firstName">First Name:</label>
                <input type="text" id="firstName" name="firstName" required>
            </div>
            
            <div class="form-group">
                <label for="lastName">Last Name:</label>
                <input type="text" id="lastName" name="lastName" required>
            </div>
            
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            
            <div class="form-group">
                <label for="mobile">Phone:</label>
                <input type="tel" id="mobile" name="mobile" required>
            </div>
            
            <div class="form-group">
                <label for="role">Role:</label>
                <select id="role" name="role">
                    <c:if test="${userRole == 'Owner'}">
                        <option value="owner">Owner</option>
                        <option value="manager">Manager</option>
                    </c:if>
                    <option value="staff">Staff</option>
                </select>
            </div>
            
            <div class="form-group">
                <label>Permissions:</label>
                <table class="permissions-table">
                    <tr>
                        <th>Order Related</th>
                        <th>Payment Related</th>
                        <th>User Related</th>
                        <th>Report Related</th>
                        <th>Category Related</th>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="permissions" value="ORDER_TAKE"> ORDER_TAKE</td>
                        <td><input type="checkbox" name="permissions" value="PAYMENT_ACCEPT"> PAYMENT_ACCEPT</td>
                        <td><input type="checkbox" name="permissions" value="USER_REGISTER"> USER_REGISTER</td>
                        <td><input type="checkbox" name="permissions" value="REPORT_VIEW"> REPORT_VIEW</td>
                        <td><input type="checkbox" name="permissions" value="CATEGORY_CREATE"> CATEGORY_CREATE</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="permissions" value="ORDER_VIEW_LIST"> ORDER_VIEW_LIST</td>
                        <td></td>
                        <td><input type="checkbox" name="permissions" value="USER_DETAIL_EDIT"> USER_DETAIL_EDIT</td>
                        <td></td>
                        <td><input type="checkbox" name="permissions" value="CATEGORY_EDIT"> CATEGORY_EDIT</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="permissions" value="ORDER_PLACE"> ORDER_PLACE</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="permissions" value="ORDER_CANCEL_APPROVE"> ORDER_CANCEL_APPROVE</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                </table>
            </div>
            
            <div class="form-group">
                <input type="submit" value="Submit">
            </div>
        </form>
    </div>
</body>
</html>