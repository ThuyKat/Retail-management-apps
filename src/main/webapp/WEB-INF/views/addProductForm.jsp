<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
     <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
</head>
<body>
	<jsp:include page="navbar.jsp" />
	
    <div class="container">
     <c:if test="${empty newProductId}">
     
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
<!--                 <button type="button" class="btn back" href="/manageProduct">Back To Product Management Page</button>
 -->            </div>
        </form>
        </c:if>
       <!--  OPTION: ADD SIZE  -->
        <c:if test="${not empty newProductId}">
            <p class="message">New product has been saved!</p>
            
            <form method="get" action="/product/form" style="display: inline-block; margin-right: 10px;">
                <input type="hidden" name="newProductId" value="${newProductId}">
                <button type="submit" class="btn toggle" name="addSize" value="true">Add Sizes</button>
            </form>
            
            <form method="get" action="/product/form" style="display: inline-block;">
                <button type="submit" class="btn toggle" name="addSize" value="false">Add New Product</button>
            </form>
            
            <c:if test="${addSize}">
                <h2>Add New Size</h2>
                <form action="/product/${newProductId}/size" method="post">
                    <div class="form-group">
                        <label for="sizeName">Size Name:</label>
                        <input type="text" id="sizeName" name="name" required>
                        
    					<label for="priceDifference">Price Difference:</label>
   						 <input type="number" id="priceDifference" name="priceDifference" step="0.01" value="0.00">
						<br>
						<p> Price for product this size: $<span id="finalPrice">${priceNewProduct}</span></p>
                    </div>
                    <button type="submit">Add Size</button>
                </form>
            </c:if>
        </c:if>
<!-- ------ -->
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
        <%-- <div class="modal" id="staticBackdrop">
            <div class="modal-content">
                <div class="modal-header">
                    <h2>Are you sure?</h2>
                    <button class="btn close" onclick="closeModal()">×</button>
                </div>
                <div class="modal-body">Your Product information will not be saved to database</div>
                <div class="modal-footer">
                    <button class="btn" onclick="closeModal()">No</button>
                    <a href="${pageContext.request.contextPath}/manageProduct" class="btn">Understood</a>
                </div>
            </div>
        </div>
    </div> --%>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
    /*     function showModal() {
            document.getElementById('staticBackdrop').style.display = 'flex';
        }

        function closeModal() {
            document.getElementById('staticBackdrop').style.display = 'none';
        } */
   
        function loadProducts() {
            var categoryId = document.getElementById('categorySelect').value;
            window.location.href = '/product/form?showProducts=true&categoryId=' + categoryId;
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
        document.getElementById('priceDifference').addEventListener('input', function() {
            var basePrice = ${priceNewProduct};
            var priceDifference = parseFloat(this.value) || 0;
            var finalPrice = basePrice + priceDifference;
            document.getElementById('finalPrice').textContent = finalPrice.toFixed(2);
        });
</script>

</body>
</html>