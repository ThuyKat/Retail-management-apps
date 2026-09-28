<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Details</title>
    <style>
       
        h1 {
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        input[type="text"], input[type="email"], select {
            width: 100%;
            padding: 5px;
            box-sizing: border-box;
        }
        .edit-btn {
            padding: 5px 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        .edit-btn:hover {
            background-color: #45a049;
        }
        .submit-btn {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        .submit-btn:hover {
            background-color: #45a049;
        }
        .hidden {
            display: none;
        }
        .checkbox-group {
            max-height: 150px;
            overflow-y: auto;
        }
    </style>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
</head>
<body>
<jsp:include page="navbar.jsp"></jsp:include>
    <h1>User Details</h1>
    
    <form:form action="/user/update" method="post" modelAttribute="user">
        <table>
            <tr>
                <th>Field</th>
                <th>Value</th>
                <th>Action</th>
            </tr>
            <tr>
                <td>Username</td>
                <td>${user.username}</td>
                <form:hidden path="username" value="${user.username}"/>
                <td></td>
            </tr>
            <tr>
                <td>Email</td>
                <td>
                    <span id="emailDisplay">${user.email}</span>
                    <form:input path="email" type="email" class="hidden" />
                </td>
                <td>
                    <button type="button" class="edit-btn" onclick="toggleEdit('email')">Edit</button>
                </td>
            </tr>
            <tr>
                <td>Status</td>
                <td>
                    <span id="statusDisplay">${user.status}</span>
                    <form:select path="status" class="hidden">
                        <form:option value="ACTIVE">Active</form:option>
                        <form:option value="INACTIVE">Inactive</form:option>
                        <form:option value="BLOCKED">Blocked</form:option>
                    </form:select>
                </td>
                <td>
                    <button type="button" class="edit-btn" onclick="toggleEdit('status')">Edit</button>
                </td>
            </tr>
            <tr>
                <td>Role</td>
                <td>
                    <span id="roleDisplay">${user.role.roleName}</span>
                    <form:select path="role" class="hidden">
                        <form:options items="${allRoles}" itemValue="id" itemLabel="roleName"/>
                    </form:select>
                </td>
                <td>
                    <button type="button" class="edit-btn" onclick="toggleEdit('role')">Edit</button>
                </td>
            </tr>
            <tr>
                <td>Permissions</td>
                <td>
                    <div id="permissionsDisplay">
                        <c:forEach items="${user.role.permissions}" var="permission">
                            ${permission.permissionName}<br>
                        </c:forEach>
                    </div>
                    <div id="permissionsEdit" class="checkbox-group hidden">
                        <c:forEach items="${allPermissions}" var="permission">
                            <label>
                                <form:checkbox path="role.permissions" value="${permission.id}" />
                                ${permission.permissionName}
                            </label><br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <button type="button" class="edit-btn" onclick="toggleEdit('permissions')">Edit</button>
                </td>
            </tr>
        </table>
        
        <input type="submit" value="Update User" class="submit-btn" />
    </form:form>

    <script>
        function toggleEdit(field) {
            var display = document.getElementById(field + 'Display');
            var edit = document.getElementById(field);
            var btn = event.target;

            if (field === 'permissions') {
                edit = document.getElementById(field + 'Edit');
            }

            if (display.style.display !== 'none') {
                display.style.display = 'none';
                edit.classList.remove('hidden');
                btn.textContent = 'Cancel';
            } else {
                display.style.display = '';
                edit.classList.add('hidden');
                btn.textContent = 'Edit';
            }
        };
        window.onload = function() {
            var message = '${message}';
            var error = '${error}';
            if (message) {
                alert(message);
            }
            if (error) {
                alert(error);
            }
        };
    </script>
</body>
</html>