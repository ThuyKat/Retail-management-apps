<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Payment Options</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f0f0f0;
        }
        .container {
            text-align: center;
            background-color: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            width: 300px;
        }
        h1 {
            color: #003087; /* PayPal blue */
            margin-bottom: 20px;
        }
        .button-group {
            display: flex; /* Use flexbox for alignment */
            justify-content: space-between; /* Space between buttons */
            margin-top: 20px; /* Space above button group */
        }
        button {
            padding: 12px 20px;
            font-size: 16px;
            border: none; /* Remove default border */
            border-radius: 5px; /* Rounded corners */
            transition: background-color 0.3s, transform 0.2s; /* Smooth transition */
            flex: 1; /* Allow buttons to grow equally */
            margin: 0 5px; /* Margin between buttons */
        }
        button[type="submit"] {
            background-color: #0070ba; /* PayPal button color */
            color: white; /* Text color */
        }
        button[type="submit"]:hover {
            background-color: #005ea6; /* Darker shade on hover */
            transform: translateY(-2px); /* Slight lift effect */
        }
        .cash-button {
            background-color: #28a745; /* Green color for Cash button */
            color: white; /* Text color */
        }
        .cash-button:hover {
            background-color: #218838; /* Darker shade on hover */
            transform: translateY(-2px); /* Slight lift effect */
        }
        button.back-button {
            background-color: #d9534f; /* Red color for Back button */
            color: white; /* Text color */
        }
        button.back-button:hover {
            background-color: #c9302c; /* Darker shade on hover */
            transform: translateY(-2px); /* Slight lift effect */
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Select Payment Method</h1>
        <div class="button-group">
            <button class="cash-button" onclick="window.location.href='/order/placeOrder?action=paypal-create'">PayPal</button>
            <button class="cash-button" onclick="window.location.href='/order/placeOrder?action=cash-pay'">Cash</button>
            <button class="back-button" onclick="window.location.href='/order?action=viewOrder'">Back</button> <!-- Back button -->
        </div>
    </div>
</body>
</html>