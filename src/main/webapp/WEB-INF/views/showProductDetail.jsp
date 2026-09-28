<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<title>Product Details</title>
<style>
form {
	background-color: #ffffff;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	/*  max-width: 500px; */
	width: 100%;
}

h1 {
	text-align: center;
	color: #333;
	margin-bottom: 20px;
}

.form-group {
	margin-bottom: 15px;
}

label {
	display: block;
	font-weight: bold;
	margin-bottom: 5px;
	color: #555;
}

input[type="text"], input[type="file"], textarea {
	width: 100%;
	padding: 10px;
	border: 1px solid #ddd;
	border-radius: 4px;
	box-sizing: border-box;
	font-size: 14px;
}

input[type="file"] {
	padding: 5px;
}

textarea {
	resize: vertical;
}

button.product-detail {
	background-color: #007bff;
	color: #fff;
	padding: 10px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 16px;
	width: 100%;
}

button.product-detail:hover {
	background-color: #0056b3;
}

button.product-detail:focus {
	outline: none;
}

.button-group {
	display: flex;
	flex-direction: column;
	gap: 10px;
}

.delete-button {
	background-color: #dc3545;
	color: #fff;
	padding: 10px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 16px;
	width: 100%;
}

.delete-button:hover {
	background-color: #c82333;
}

.delete-button:focus {
	outline: none;
}
.no-sizes-message {
  text-align: center;
  color: #6c757d;
  font-style: italic;
  padding: 20px;
  background-color: #ffffff;
  border: 1px dashed #ced4da;
  border-radius: 4px;
}
/* Edit button styles */
.edit-btn {
  background-color: #007bff;
  color: #ffffff;
  padding: 6px 12px;
  border-radius: 4px;
  text-decoration: none;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.edit-btn:hover {
  background-color: #0056b3;
}

.delete-btn {
  background-color: #dc3545; /* Bootstrap's danger color */
  color: #ffffff;
  padding: 6px 12px;
  border-radius: 4px;
  text-decoration: none;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

.delete-btn:hover {
  background-color: #c82333; /* Darker shade of red for hover */
}

/* Size list styles */
.size-list {
  list-style-type: none;
  padding: 0;
}

.size-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.size-name {
  flex-grow: 1;
  margin-right: 10px;
}

.size-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.edit-btn,
.delete-btn {
  margin-left: 5px;
}

</style>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    

</head>
<body>
	<jsp:include page="navbar.jsp" />
	<h1>Product Details</h1>
	<form:form method="post" action="/product/${product.id}" modelAttribute="product" enctype="multipart/form-data" id="product-form">
		
		<div class="form-group">
			<label for="name">Name:</label>
			<form:input path="name" id="name" value="${product.name}" />

		</div>
		<div class="form-group">
			<label for="description">Description:</label>
			<form:textarea path="description" id="description" rows="5" cols="40"
				oninput="validateWordCount(this, 65535)" />
		</div>
		<div class="form-group">
			<label for="image">Upload New Image:</label> <input type="file"
				id="image" name="imageData" id="upload-product-image" />
		</div>
		<div class="button-group">
			<button type="submit" class="product-detail">Save Changes</button>
			<button type="button" class="delete-button"
				onclick="if(confirmDelete()) window.location.href='/product/delete?productId=${product.id}';">
				Delete This Product
			</button>
		</div>
	</form:form>
	
	 <h2>Add Size</h2>
        <form action="/product/${product.id}/size" method="post">
            <div class="form-group">
                <label for="sizeName">Size Name:</label>
                <input type="text" id="sizeName" name="name" required>
                <label for="priceDifference">Price Difference:</label>
   			    <input type="number" id="priceDifference" name="priceDifference" step="0.01" value="0.00">
				<br>
				<p> Price for product this size: $<span id="finalPrice">${product.price}</span></p>
            </div>
            <button type="submit">Add Size</button>
        </form>
	<c:choose>
    <c:when test="${not empty product.sizes}">
        <h2>Existing Sizes</h2>
        <ul class="size-list">
            <c:forEach items="${product.sizes}" var="size">
                <li class="size-item">
                    <span class="size-name">${size.name} - $${size.price}</span>
                    <a href="<c:url value='/product/size/edit/${size.id}'/>" class="edit-btn">Edit</a>
                    <a href="<c:url value='/product/size/delete/${size.id}'/>" class="delete-btn" onclick="return confirm('Are you sure you want to delete this size?')">Delete</a>
                </li>
            </c:forEach>
        </ul>
    </c:when>
    <c:otherwise>
        <p class="no-sizes-message">No existing sizes</p>
    </c:otherwise>
</c:choose>
	
	<script>
        function validateWordCount(textarea, maxLength) {
            if (textarea.value.length > maxLength) {
                alert("Description cannot exceed " + maxLength + " characters.");
                textarea.value = textarea.value.substring(0, maxLength); // Trim the value
            }
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
        function confirmDelete() {
            return confirm("Are you sure you want to delete this product?");
        }
        document.getElementById('priceDifference').addEventListener('input', function() {
            var basePrice = ${product.price};
            var priceDifference = parseFloat(this.value) || 0;
            var finalPrice = basePrice + priceDifference;
            document.getElementById('finalPrice').textContent = finalPrice.toFixed(2);
        });
</script>

</body>
</html>