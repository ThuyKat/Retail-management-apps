<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Categories</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container-structure {
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .category-tree {
            margin-left: 20px;
            border-left: 1px solid #ccc;
            padding-left: 10px;
        }
        .category-item, .size-item {
            margin: 10px 0;
            padding: 5px;
            background-color: #f9f9f9;
            border-radius: 3px;
        }
        .edit-btn, .delete-btn {
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
            font-size: 0.9em;
            margin-left: 10px;
        }
        .edit-btn {
            background-color: #4CAF50;
        }
        .edit-btn:hover {
            background-color: #45a049;
        }
        .delete-btn {
            background-color: #f44336;
        }
        .delete-btn:hover {
            background-color: #d32f2f;
        }
    </style>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"/>
    <div class="container-structure">
        <h1>Category Structure</h1>
        <div class="category-tree">
            <c:forEach items="${topLevelCategories}" var="category">
                <div class="category-item">
                    ${category.name}
                    <a href="/category/edit/${category.id}" class="edit-btn">Edit</a>
                    <a href="/category/delete/${category.id}" class="delete-btn" onclick="return confirm('Are you sure you want to delete this category?')">Delete</a>
                    
                    <c:if test="${not empty category.subcategories}">
                        <div class="category-tree">
                            <c:forEach items="${category.subcategories}" var="subcategory">
                                <div class="category-item">
                                    ${subcategory.name}
                                    <a href="/category/edit/${subcategory.id}" class="edit-btn">Edit</a>
                                    <a href="/category/delete/${subcategory.id}" class="delete-btn" onclick="return confirm('Are you sure you want to delete this subcategory?')">Delete</a>
                                    
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>
            </c:forEach>
        </div>
    </div>
    <script>
        $(document).ready(function() {
            var message = "${message}";
            if (message) {
                alert(message);
            }

            var error = "${error}";
            if (error) {
                alert(error);
            }
        });
    </script>
</body>
</html>