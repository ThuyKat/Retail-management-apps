	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<script>
	    document.addEventListener('DOMContentLoaded', function() {

	 // Add event listener for window resize
        window.addEventListener('resize', function() {
            if (window.orderStatusChart) {
                window.orderStatusChart.resize();
            }
        });
    });
	
	    function drawChart(data) {
	        const chartContainer = document.getElementById('chartContainer');
	        chartContainer.style.display = 'block';
	
	        const ctx = document.getElementById('ordersByStatusChart').getContext('2d');
	        
	        // Destroy existing chart if it exists
	        if (window.orderStatusChart instanceof Chart) {
	            window.orderStatusChart.destroy();
	        }
	
	        window.orderStatusChart = new Chart(ctx, {
	            type: 'bar',
	            data: {
	                labels: data.map(item => item.status),
	                datasets: [{
	                    label: 'Orders by Status',
	                    data: data.map(item => item.count),
	                    backgroundColor: 'rgba(37, 66, 190, 0.6)',
	                    borderColor: 'rgba(37, 66, 190, 1)',
	                    borderWidth: 1
	                }]
	            },
	            options: {
	                responsive: true,
	                scales: {
	                    y: {
	                        beginAtZero: true
	                    }
	                },
	                onClick: (event, elements) => {
	                    if (elements.length > 0) {
	                        const index = elements[0].index;
	                        const status = data[index].status;
	                        fetchOrderDetails(status);
	                    }
	                }
	            }
	        });
	    }
	    
	    function fetchOrderDetails(status) {
	        const startDate = document.getElementById('startDate').value;
	        const endDate = document.getElementById('endDate').value;
	
	        const url = new URL('/report/ordersByStatus/details', window.location.origin);
	        url.searchParams.append('startDate', startDate);
	        url.searchParams.append('endDate', endDate);
	        url.searchParams.append('status', status);
	
	        fetch(url)
	        .then(response => response.text())
	        .then(html => {
	            document.getElementById('orderDetailsContainer').innerHTML = html;
	            document.getElementById('orderDetailsContainer').style.display = 'block';
	        })
	        .catch(error => console.error('Error:', error));
	    }
	</script>