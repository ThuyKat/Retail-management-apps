<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Category Management</title>
    <style>
       
        h1, h2 {
            color: #333;
        }
        .container-category {
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
        button.add-category {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button.add-category:hover {
            background-color: #45a049;
        }
        .btn.toggle {
            background-color: #008CBA;
        }
        .btn.toggle:hover {
            background-color: #007B9A;
        }
        .message {
            background-color: #d4edda;
            border-color: #c3e6cb;
            color: #155724;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
        }
    </style>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <jsp:include page="navbar.jsp"/>
    <div class="container-category">
        <h1>Category Management</h1>
        
        <c:if test="${empty newCategoryId}">
            <h2>Add New Category</h2>
            <form action="/category" method="post">
                <div class="form-group">
                    <label for="categoryName">Category Name:</label>
                    <input type="text" id="categoryName" name="name" required>
                </div>
                <div class="form-group">
                    <label for="parentCategory">Parent Category:</label>
                    <select id="parentCategory" name="parentId">
                        <option value="">Top Level Category</option>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" class="add-category">Add Category</button>
            </form>
        </c:if>
        
        <c:if test="${not empty newCategoryId}">
            <p class="message">New category has been saved!</p>
            
            <form method="get" action="/category/form" style="display: inline-block; margin-right: 10px;">
                <input type="hidden" name="newCategoryId" value="${newCategoryId}">
                <button type="submit" class="btn toggle" name="addSize" value="true">Add Sizes</button>
            </form>
            
            <form method="get" action="/category/form" style="display: inline-block;">
                <button type="submit" class="btn toggle" name="addSize" value="false">Add New Category</button>
            </form>
            
            <c:if test="${addSize}">
                <h2>Add New Size</h2>
                <form action="/category/${newCategoryId}/size" method="post">
                    <div class="form-group">
                        <label for="sizeName">Size Name:</label>
                        <input type="text" id="sizeName" name="name" required>
                    </div>
                    <button type="submit">Add Size</button>
                </form>
            </c:if>
        </c:if>

        <h2>Category Structure</h2>
        <div class="category-tree">
        <c:forEach items="${topLevelCategories}" var="category">
            <div>
                ${category.name}
                <c:if test="${not empty category.sizes}">
                     <div class="category-tree">
                        Sizes:
                     	<c:forEach items="${category.sizes}" var="size">
                            <div>${size.name}</div>
                         </c:forEach>
                      </div>
                </c:if>
                <c:if test="${not empty category.subcategories}">
                    <div class="category-tree">
                        <c:forEach items="${category.subcategories}" var="subcategory">
                            <div>
                                ${subcategory.name}
                                <c:if test="${not empty subcategory.sizes}">
                                    <div class="category-tree">
                                        Sizes:
                                        <c:forEach items="${subcategory.sizes}" var="size">
                                            <div>${size.name}</div>
                                        </c:forEach>
                                    </div>
                                </c:if>
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