<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        window.addEventListener('resize', function() {
            if (window.salesUserChart) {
                window.salesUserChart.resize();
            }
        });
    });

    function drawChart(data) {
        console.log("Received data:", data); // Log the received data

        const chartContainer = document.getElementById('chartContainer');
        chartContainer.style.display = 'block';

        const ctx = document.getElementById('salesByUserChart').getContext('2d');

        if (window.salesUserChart instanceof Chart) {
            window.salesUserChart.destroy();
        }

        // Get unique dates across all users
        const allDates = [...new Set(data.map(item => item.date))].sort();

     // Group data by user and fill in missing dates with null values
        const groupedData = data.reduce((acc, item) => {
            if (!acc[item.username]) {
                acc[item.username] = {};
            }
            acc[item.username][item.date] = parseFloat(item.totalSales);
            return acc;
        }, {});

     
     // Find the maximum sales value
        const maxSales = Math.max(...data.map(item => parseFloat(item.totalSales)));

        // Prepare datasets
        const datasets = Object.entries(groupedData).map(([username, userData], index) => {
            const color = getColor(index);
            return {
                label: username,
                data: allDates.map(date => userData[date] || null),
                borderColor: color,
                backgroundColor: color,
                fill: false,
                spanGaps: true // This will connect points even if there are null values in between
            };
        });

        // Check if datasets are populated
        if (datasets.length === 0) {
            console.error("No data available for chart.");
            return;
        }

        // Create the chart
        window.salesUserChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: allDates,
                datasets: datasets
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Sales Amount (AUD)'
                        },
                        ticks: {
                            callback: function(value, index, values) {
                                return new Intl.NumberFormat('en-AU', { style: 'currency', currency: 'AUD' }).format(value);
                            }
                        },
                        suggestedMax: maxSales * 1.1 // Set the max value to 110% of the highest sales value
                    
                    
                    },
                    x: {
                        type: 'time',
                        time: {
                            unit: 'day',
                            displayFormats: {
                                day: 'MMM d'
                            }
                        },
                        title: {
                            display: true,
                            text: 'Date'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                        labels: {
                            generateLabels: function(chart) {
                                const datasets = chart.data.datasets;
                                return datasets.map((dataset, i) => ({
                                    text: dataset.label,
                                    fillStyle: dataset.backgroundColor,
                                    hidden: !chart.isDatasetVisible(i),
                                    lineCap: dataset.borderCapStyle,
                                    lineDash: dataset.borderDash,
                                    lineDashOffset: dataset.borderDashOffset,
                                    lineJoin: dataset.borderJoinStyle,
                                    lineWidth: dataset.borderWidth,
                                    strokeStyle: dataset.borderColor,
                                    pointStyle: dataset.pointStyle,
                                    datasetIndex: i
                                }));
                            }
                        }
                    
                    },
                    tooltip: {
                        mode: 'index',
                        intersect: false,
                        callbacks: {
                            title: function(tooltipItems) {
                                return new Date(tooltipItems[0].label).toLocaleDateString('en-AU');
                            },
                            label: function(context) {
                                let label = context.dataset.label || '';
                                if (label) {
                                    label += ': ';
                                }
                                if (context.parsed.y !== null) {
                                    label += new Intl.NumberFormat('en-AU', { style: 'currency', currency: 'AUD' }).format(context.parsed.y);
                                }
                                return label;
                            }
                        }
                    }
                }
            }
        });
    }

    // Function to generate colors for different users
    function getColor(index) {
        const colors = [
            'rgba(75, 192, 192, 1)',
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)'
        ];
        return colors[index % colors.length];
    }
</script>