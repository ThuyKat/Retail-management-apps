<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Add event listener for window resize
        window.addEventListener('resize', function() {
            if (window.salesProductChart) {
                window.salesProductChart.resize();
            }
        });
    });

    function drawChart(data) {
        const chartContainer = document.getElementById('chartContainer');
        chartContainer.style.display = 'block';

        const ctx = document.getElementById('salesByProductChart').getContext('2d');
        
        // Destroy existing chart if it exists
        if (window.salesProductChart instanceof Chart) {
            window.salesProductChart.destroy();
        }

        // Calculate total sales
        const totalSales = data.reduce((sum, item) => sum + item.totalSales, 0);

        window.salesProductChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: data.map(item => item.productName),
                datasets: [{
                    label: 'Sales by Product',
                    data: data.map(item => item.totalSales),
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 159, 64, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const label = context.label || '';
                                const value = context.raw || 0;
                                const percentage = ((value / totalSales) * 100).toFixed(2);
                                return `${label}: ${percentage}%`;
                            }
                        }
                    },
                    legend: {
                        position: 'top',
                    },
                    datalabels: {
                        formatter: (value, ctx) => {
                            const percentage = ((value / totalSales) * 100).toFixed(2);
                            return percentage + '%';
                        },
                        color: '#000080',
                        anchor: 'center',
                        align: 'center',
                        font: {
                            weight: 'bold'
                        }
                    }
                }
            },
            plugins: [ChartDataLabels]
        });
    }

    
</script>