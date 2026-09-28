<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Access Denied</title>
    <style>
       
       /*  .container-access-denied {
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        } */
        h1 {
            color: #e74c3c;
        }
        p {
            margin-bottom: 20px;
        }
        a {
            color: #3498db;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
   <jsp:include page="navbar.jsp"></jsp:include>
    <div class="container-access-denied">
        <h1>Access Denied</h1>
        <p>Sorry, you do not have permission to access this page.</p>
        <p><a href="/index">Return to Home Page</a></p>
    </div>
</body>
</html>