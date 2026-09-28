<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Orders by Status Report</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
    #reportForm {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            /* max-width: 500px; */
            margin: 0 auto;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #666;
        }
        input[type="date"], select {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .generate-report {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
        }
        .generate-report:hover {
            background-color: #45a049;
        }
        #chartContainer {
            margin-top: 20px;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
<jsp:include page="navbar.jsp" />
    <h1>Orders by Status Report</h1>
    <form id="reportForm" onsubmit="return fetchData();">
        <label for="startDate">Start Date:</label>
        <input type="date" id="startDate" name="startDate" required>
        
        <label for="endDate">End Date:</label>
        <input type="date" id="endDate" name="endDate" required>
        
        <label for="status">Status:</label>
        <select id="status" name="status">
            <option value="">All Statuses</option>
            <c:forEach items="${statuses}" var="status">
                <option value="${status}">${status}</option>
            </c:forEach>
        </select>
        
        <button type="submit" class="generate-report">Generate Report</button>
    </form>

    <div id="chartContainer" style="display: none;">
        <canvas id="ordersByStatusChart"></canvas>
    </div>
    
    <div id="orderDetailsContainer" style="display: none;"></div>

    <script>

        function fetchData() {
            const startDate = document.getElementById('startDate').value;
            const endDate = document.getElementById('endDate').value;
            const status = document.getElementById('status').value;
            console.log('Start Date:', startDate); // Log the start date
            console.log('End Date:', endDate); // Log the end date
            console.log('Status:', status); // Log the selected status

             if (!startDate || !endDate) {
                alert("Please select both start and end dates.");
                return;
            } 

            const url = new URL('/report/ordersByStatus/data', window.location.origin);
            url.searchParams.append('startDate', startDate);
            url.searchParams.append('endDate', endDate);
            url.searchParams.append('status', status);
            
            console.log('Fetching data from:', url.toString());

            fetch(url)
                .then(response => {
                    console.log('Response status:', response.status);
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Received data:', data);
                    if (data && data.length > 0) {
                        drawChart(data);
                    } else {
                        console.log('No data received or empty data');
                        document.getElementById('chartContainer').style.display = 'none'; //The chart is only drawn when there's data to display.
                        //Subsequent searches are  blocked if we modify the innerHTML of the chart container.
                        alert('No orders found for the selected criteria.');                    
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    document.getElementById('chartContainer').style.display = 'none';
                    alert('An error occurred while fetching data. Please try again.');
                });
            return false; // Prevent form submission, regardless of the outcome of the fetch operation.
        }
    </script>

    <jsp:include page="ordersByStatusChart.jsp" />
       
</body>
</html>