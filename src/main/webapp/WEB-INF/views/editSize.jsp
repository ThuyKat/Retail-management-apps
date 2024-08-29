<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Size</title>
    <style>
        
        .container-size{
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 15px;
        }
        label.size {
            display: block;
            margin-bottom: 5px;
        }
        input.size[type="text"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        button.size {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button.size:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<jsp:include page="navbar.jsp"/>
    <div class="container-size">
        <h1>Edit Size</h1>
        <form action="/category/size/update" method="post">
            <input type="hidden" name="id" value="${size.id}">
            <div class="form-group">
                <label for="sizeName" class="size">Size Name:</label>
                <input type="text" id="sizeName" name="name" value="${size.name}" class="size" required>
            </div>
            <button type="submit" class="size">Update Size</button>
        </form>
    </div>
</body>
</html>