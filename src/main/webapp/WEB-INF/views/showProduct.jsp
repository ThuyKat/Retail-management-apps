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
</head>
<body>

<c:if test="${currentURI == '/product/currentProductList'}">
<jsp:include page="navbar.jsp" />
</c:if>
    <h1>Product List</h1>
    <form:form method="post" action="updatePrice" modelAttribute="productListWrapper">
        <table class="product-table">
            <thead>
                <tr>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Price</th>
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
                            <a href="/product/details?productId=${product.id}" class="more-link">More</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <button type="submit">Save Update</button>
    </form:form>
</body>
</html>