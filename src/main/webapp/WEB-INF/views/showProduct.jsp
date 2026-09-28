<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Product List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .product-table {
            width: 100%;
            border-collapse: collapse;
        }
        .product-table th, .product-table td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }
        .product-image {
            width: 100px;
            height: 100px;
        }
        .more-link {
            margin-left: 10px;
        }
    </style>
   
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<c:if test="${currentURI == '/product/currentProductList'}">
<jsp:include page="navbar.jsp" />
</c:if>
    <h1>Product List</h1>
        <!-- Add category selection dropdown -->
    <div class="category-select">
        <label for="categorySelect">Select Category: </label>
        <select id="categorySelect" onchange="loadProducts()">
            <option value="">All Categories</option>
            <c:forEach items="${categories}" var="category">
                <option value="${category.id}">${category.name}</option>
            </c:forEach>
        </select>
    </div>
    
    <form:form method="post" action="updatePrice" modelAttribute="productListWrapper">
        <table class="product-table">
            <thead>
                <tr>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Sizes</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${productListWrapper.products}" var="product" varStatus="status">
                    <tr>
                        <td><img src="data:image/jpeg;base64,${product.base64Image}" alt="${product.name}" class="product-image" /></td>
                        <td>${product.name}</td>
                        <td>${product.category.name}</td>
                        <td>
                            <form:input path="products[${status.index}].price" class="product-price" value="${product.price}" />
                            <form:hidden path="products[${status.index}].id" />
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty product.sizes}">
                                    <c:forEach items="${product.sizes}" var="size" varStatus="sizeStatus">
                                        ${size.name}<c:if test="${!sizeStatus.last}">, </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    N/A
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="/product/${product.id}" class="more-link">More</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <button type="submit">Save Update</button>
    </form:form>
</body>
<c:if test="${currentURI == '/product/currentProductList'}">
<script>
        function loadProducts() {
            var categoryId = document.getElementById('categorySelect').value;
            window.location.href = '/product/currentProductList?categoryId=' + categoryId;
        }
        
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
</c:if>
</html>