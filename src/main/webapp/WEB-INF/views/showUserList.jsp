<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User List</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .update-btn {
            background-color: #4CAF50;
            color: white;
            padding: 6px 12px;
            border: none;
            cursor: pointer;
            border-radius: 4px;
        }
        .update-btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<jsp:include page="navbar.jsp"/>
    <h1>User List</h1>
    <table>
        <thead>
            <tr>
                <th>#</th>
                <th>Username</th>
                <th>Email</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Role</th>
                <th>Created Date</th>
                <th>Last Login</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${users}" var="user" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.role.roleName}</td>
                    <td>${fn:replace(user.createdDate, 'T', ' ')}</td>
                    <td>${fn:replace(user.lastLogin, 'T', ' ')}</td>
                    <td>${user.status}</td>
                    <td>
                        <form action="/user/view/${user.username}" method="get">
                            <button type="submit" class="update-btn">Update</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>