<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="menu-nav-bar">
    <div class="nav-content">
        <div class="nav-left">
            <form id="logoutForm" action="/perform_logout" method="post" >
    		<button type="submit" class="logout-btn">Logout</button>
			</form>
            <h1 class="menu-nav-brand">QUICKSHOP</h1>
        </div>
        <div class="nav-right">
            <span class="welcome-message">Welcome, <c:out value="${username}" /></span>
            
        </div>
         
            <c:if test="${not empty sessionScope.navigationStack && sessionScope.navigationStack.size() > 1}">
            <a href="/navigate" class="nav-option"> &lt; Back</a>
        </c:if>
   
    </div>
</nav>

<style>
.menu-nav-brand {
    color: white;
    font-weight: 600;
}
.menu-nav-bar {
    background-color: #3498db;
    color: white;
    padding: 10px 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.nav-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.nav-left {
    display: flex;
    align-items: center;
    gap: 15px;
}

.nav-right {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-left: auto;
}

.menu-nav-brand {
    font-size: 1.5rem;
    margin: 0;
    font-weight: bold;
}

.welcome-message {
    font-size: 1rem;
    margin-right: 20px;
    font-family: sans-serif;
    font-style: italic;
}

#logoutForm {
    margin: 0;
    padding: 0;
}

.logout-btn {
    background-color: #e74c3c;
    color: white;
    padding: 12px 20px;
    border-radius: 10px; /* More curve */
    text-decoration: none;
    font-weight: bold;
    border: none;
    cursor: pointer;
    transition: background-color 0.3s ease;
    font-size: 1.1rem; /* Increased font size */
    border-radius: 25px;
    padding: 12px 25px;
}

.logout-btn:hover {
    background-color: #c0392b;
    
}

.option, .nav-option {
    border-radius: 25px; /* Increased border-radius for more curve */

    background-color: #3498db;
    color: #ffffff;
    font-weight: 500;
    text-align: center;
    font-size: 1.2rem; /* Increased font size */
    margin: 0 5px;
    text-decoration: none;
    padding: 12px 25px; /* Increased padding */
    transition: background-color 0.3s ease, color 0.3s ease;
    display: inline-block; /* Ensures proper rendering of rounded corners */
}

.option:hover, .nav-option:hover {
    background-color: #00008B; /* Dark blue on hover */
    color: #fff; /* Ensure text remains white on hover */
}
</style>