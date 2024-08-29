<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

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

input[type="text"],
input[type="file"],
textarea {
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

button {
    background-color: #007bff;
    color: #fff;
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    width: 100%;
}

button:hover {
    background-color: #0056b3;
}

button:focus {
    outline: none;
}
    </style>
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
    </script>
    
</head>
<body>
<jsp:include page="navbar.jsp" />
    <h1>Product Details</h1>
    <form:form method="post" action="/product/details" modelAttribute="product" enctype="multipart/form-data" id="product-form">
        <input type="hidden" name="productId" value="${product.id}" />
        <div class="form-group">
            <label for="name">Name:</label>
            <form:input path="name" id="name" value="${product.name}" />
            
        </div>
        <div class="form-group">
            <label for="description">Description:</label>
            <form:textarea path="description" id="description" rows="5" cols="40" oninput="validateWordCount(this, 65535)"/>
        </div>
        <div class="form-group">
            <label for="image">Upload New Image:</label>
            <input type="file" id="image" name="imageData" id="upload-product-image" />
        </div>
        <button type="submit">Save Changes</button>
    </form:form>
</body>
</html>