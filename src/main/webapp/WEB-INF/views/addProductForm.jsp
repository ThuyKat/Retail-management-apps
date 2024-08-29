<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
</head>
<body>
	<jsp:include page="navbar.jsp" />
    <div class="container">
        <h1>Add New Product</h1>
        <form id="product-form" action="${pageContext.request.contextPath}/product/add" method="post" enctype="multipart/form-data">
            <input type="text" class="form-control" name="productId" placeholder="Id" disabled>
            <input type="text" class="form-control" name="productName" placeholder="Product Name" required>
            <input type="number" class="form-control" name="price" placeholder="Product Price" required>
            <select class="form-control" name="categoryId" required>
                <option value="" disabled selected>Select Category</option>
                <c:forEach items="${allCategory}" var="category">
                    <option value="${category.id}">${category.name}</option>
                </c:forEach>
            </select>
            <input type="file" class="form-control" name="imageData" required>
            <div class="button-group">
                <button type="submit" class="btn save">Save Product</button>
                <button type="button" class="btn back" onclick="showModal()">Back To Home Page</button>
            </div>
        </form>

        <div class="product-list-toggle">
            <c:choose>
                <c:when test="${!showProducts}">
                    <form method="post" action="${pageContext.request.contextPath}/product/form">
                        <button type="submit" class="btn toggle" name="showProducts" value="true">Show Product List</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <form method="post" action="${pageContext.request.contextPath}/product/form">
                        <button type="submit" class="btn toggle" name="showProducts" value="false">Hide Product List</button>
                    </form>
                    <jsp:include page="showProduct.jsp"/>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Modal -->
        <div class="modal" id="staticBackdrop">
            <div class="modal-content">
                <div class="modal-header">
                    <h2>Are you sure?</h2>
                    <button class="btn close" onclick="closeModal()">×</button>
                </div>
                <div class="modal-body">Your Product information will not be saved to database</div>
                <div class="modal-footer">
                    <button class="btn" onclick="closeModal()">No</button>
                    <a href="${pageContext.request.contextPath}/" class="btn">Understood</a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        function showModal() {
            document.getElementById('staticBackdrop').style.display = 'flex';
        }

        function closeModal() {
            document.getElementById('staticBackdrop').style.display = 'none';
        }
    </script>
</body>
</html>