<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Category</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
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
        label {
            display: block;
            margin-bottom: 5px;
        }
        input[type="text"], select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
     
</head>
<body>
<jsp:include page="navbar.jsp"/>
    <div class="container">
        <h1>Edit Category</h1>
        <form action="/category/update" method="post">
            <input type="hidden" name="id" value="${category.id}">
            <div class="form-group">
                <label for="categoryName">Category Name:</label>
                <input type="text" id="categoryName" name="name" value="${category.name}" required>
            </div>
            <div class="form-group">
                <label for="parentCategory">Parent Category:</label>
                <select id="parentCategory" name="parentId">
                    <option value="">Top Level Category</option>
                    <c:forEach items="${allCategories}" var="cat">
                        <c:if test="${cat.id != category.id}">
                            <option value="${cat.id}" ${cat.id == category.parent.id ? 'selected' : ''}>${cat.name}</option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>
            <button type="submit">Update Category</button>
        </form>

        <h2>Add Size</h2>
        <form action="/category/${category.id}/size" method="post">
            <div class="form-group">
                <label for="sizeName">Size Name:</label>
                <input type="text" id="sizeName" name="name" required>
            </div>
            <button type="submit">Add Size</button>
        </form>

        <h2>Existing Sizes</h2>
        <c:forEach items="${category.sizes}" var="size">
            <div>
                ${size.name}
                <a href="/size/edit/${size.id}" class="edit-btn">Edit</a>
            </div>
        </c:forEach>
    </div>
     
</body>
</html>