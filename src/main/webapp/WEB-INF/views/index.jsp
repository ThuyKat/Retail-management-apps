<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QUICKSHOP - Menu</title>
    
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100..900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet" href="css/style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="js/script.js"></script>
     <script>
        $(document).ready(function() {
            // Show alert messages
            var message = '${message}';
            var error = '${error}';
            if (message) {
                alert(message);
            }
            if (error) {
                alert(error);
            }
        });
        </script>
</head>
<body>
	<!-- include the banner -->
	<jsp:include page="navbar.jsp" />
   <!--  <nav class="menu-nav-bar">
        <i class="fa-solid fa-bars hambuger-icon"></i>
        <h1 class="menu-nav-brand">QUICKSHOP</h1>
    </nav> -->


  <%--  <!-- Display success message if it exists -->
    <c:if test="${not empty message}">
        <div class="alert alert-success">
        <p>${message}</p>
        </div>
    </c:if>

    <!-- Display error message if it exists -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if> --%>
    
    <section id="categories">
        <ul class="category-section">
            <c:forEach items="${allCategory}" var="category">
                <li>
                    <a href="?categoryId=${category.id}">${category.name}</a>
                </li>
            </c:forEach>
            <li><a href="?">All</a></li>
            
        </ul>
    </section>

    <section class="product-section">
        <form:form method="post" action="/order?action=addToOrder" modelAttribute="orderWrapper" id="product-form">
            <c:forEach items="${orderWrapper.orderItems}" var="orderItem" varStatus="status">
                <div class="product-container">
                    <c:if test="${orderItem.product.base64Image != null}">
                        <img src="data:image/jpeg;base64,${orderItem.product.base64Image}" alt="${orderItem.product.name}">
                    </c:if>
                    <div class="product-details">
                        <div class="product-name">${orderItem.product.name}</div>
                        <div class="product-price">$${orderItem.product.price}</div>
                        <div class="product-quantity">
                            <input type="button" value="-" class="button-minus" data-field="orderItems[${status.index}].quantity">
                            <form:input path="orderItems[${status.index}].quantity" type="number" step="1" min="0" class="quantity-field text-center" />
                            <input type="button" value="+" class="button-plus" data-field="orderItems[${status.index}].quantity">
                            <form:hidden path="orderItems[${status.index}].product.id" />
                        </div>
                    </div>
                </div>
            </c:forEach>
            
      
            <footer>
            <button class="order-btn" type="submit">Add To Order</button>
                <a href="/order?action=viewOrder" class="order-btn">View Order
                <span class="item-count"><c:out value="${itemCount != null ? itemCount : 0}" /></span>
                </a>
            <a href="/order?action=resetOrder" class="order-btn" style="text-decoration: none;">Reset Order</a>
            </footer>
            </form:form>
        
    </section>
</body>
</html>