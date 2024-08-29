<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PayPal QR Code</title>
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
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        h1 {
            color: #003087; /* PayPal blue */
        }
        .qr-code {
            margin-top: 20px;
        }
        .instructions {
            margin-top: 20px;
            color: #666;
        }
        .error {
            color: red;
        }
        .cancel-button {
            margin-top: 20px;
            padding: 10px 20px;
            font-size: 16px;
            color: white;
            background-color: #d9534f; /* Bootstrap danger color */
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .cancel-button:hover {
            background-color: #c9302c; /* Darker shade on hover */
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Scan QR Code to Pay with PayPal</h1>
        <div class="qr-code">
            <c:choose>
                <c:when test="${not empty qrCodeImage}">
                    <img src="data:image/png;base64,${qrCodeImage}" alt="PayPal QR Code" />
                </c:when>
                <c:otherwise>
                    <div class="error">${error}</div>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="instructions">
            <p>1. Open your PayPal app</p>
            <p>2. Tap 'Scan/Pay'</p>
            <p>3. Scan this QR code</p>
            <p>4. Complete the payment in your app</p>
        </div>
        <button class="cancel-button" onclick="window.location.href='http://localhost:8080/order/placeOrder?action=paypal-cancel'">Cancel Payment</button>
    </div>
</body>
</html>
