<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Reset Username and Password</title>
</head>
<body>
<h2>Reset Username and Password</h2>
<form action="/register/finaliseRegistration" method="post" onsubmit="return validateForm()">
			<input type="hidden" id="token" name="token" value="${token}"> <!-- Hidden token field -->
			<label for="username">New Username:</label>
            <input type="text" id="username" name="username" required>
            
            <label for="password">New Password:</label>
            <input type="password" id="password" name="password" required>
            
            <label for="confirmPassword">Confirm Password:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
            
            <div id="errorMessage" class="error"></div>
            
            <input type="submit" value="Reset Credentials">
            
        <script>
        function validateForm() {
            var username = document.getElementById('username').value;
            var password = document.getElementById('password').value;
            var confirmPassword = document.getElementById('confirmPassword').value;
            var errorMessage = document.getElementById('errorMessage');
            
            if (username.length < 4) {
                errorMessage.textContent = "Username must be at least 4 characters long.";
                return false;
            }
            
            if (password.length < 8) {
                errorMessage.textContent = "Password must be at least 8 characters long.";
                return false;
            }
            
            if (password !== confirmPassword) {
                errorMessage.textContent = "Passwords do not match.";
                return false;
            }
            
            errorMessage.textContent = "";
            return true;
        }
    </script>

</form>
</body>
</html>