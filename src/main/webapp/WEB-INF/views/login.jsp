<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login QUICKQUICK</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .login-container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 300px;
            text-align: center;
        }

        h1 {
            margin-bottom: 20px;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            margin-bottom: 5px;
            text-align: left;
        }

        input[type="text"],
        input[type="password"] {
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .checkbox-container {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }

        .checkbox-container label {
            margin-left: 5px;
            font-size: 14px;
        }

        .forgot-password {
            color: #007bff;
            text-decoration: none;
            font-size: 14px;
            margin-bottom: 20px;
        }

        button {
            background-color: #007bff;
            color: white;
            padding: 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        .contact-info, .working-hours {
            font-size: 14px;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h1>Login QUICKQUICK</h1>
        <form action="/perform_login" method="post">
            <label for="username">User name</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>

            <div class="checkbox-container">
                <input type="checkbox" id="case-sensitive" name="case-sensitive">
                <label for="case-sensitive">Do not distinguish lower and upper case.</label>
            </div>

            <div class="checkbox-container">
                <input type="checkbox" id="remember-me" name="remember-me">
                <label for="remember-me">Remember Me</label>
            </div>

            <a href="/forgotPassword" class="forgot-password">Forgot password?</a>

            <button type="submit">Login</button>
        </form>

        <p class="contact-info">
            Email: <a href="mailto:fsales@quickquick.com.vn">fsales@quickshop.com.vn</a><br>
            Help
        </p>

        <p class="working-hours">
            Working hour: Mon-Fri: 7:30 - 22:00,<br>
            Sat, Sun: 8:00 - 12:00; 13:30 - 22:00
        </p>
    </div>
</body>
</html>