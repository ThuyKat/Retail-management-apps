<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.util.Date" %>

<style>
    .comments-section {
        background-color: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .comment {
        margin-bottom: 15px;
        padding-bottom: 15px;
        border-bottom: 1px solid #eee;
    }
    .comment-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 5px;
        font-size: 0.9em;
        color: #666;
    }
    .comment-subject {
        font-weight: bold;
        margin-bottom: 5px;
    }
    .input-group {
        margin-bottom: 15px;
    }
    .input-group label {
        display: block;
        margin-bottom: 5px;
    }
    .input-group input[type="text"],
    .input-group textarea {
        width: 100%;
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }
    .submit-btn {
        background-color: #3498db;
        color: white;
        padding: 10px 15px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }
    .submit-btn:hover {
        background-color: #2980b9;
    }
</style>

<div class="comments-section">
    <h2><i>Comments</i></h2>
    <c:if test="${empty comments}">
        <p>No comments available.</p>
    </c:if>
    <c:if test="${not empty comments}">
        <c:forEach var="comment" items="${comments}">  
            <div class="comment">
                <fmt:formatDate value="${comment.createAt}" pattern="yyyy-MM-dd HH:mm:ss" var="formattedDate"/>
                <div class="comment-header">
                    <span>${formattedDate} - ${commentUsernames[comment.id]} <i>wrote</i>:</span>
                </div>
                <div class="comment-subject"><b>${comment.subject}</b></div>
                <div class="comment-text">${comment.commentText}</div>
            </div>
        </c:forEach>
    </c:if>
    
    <hr>
	
    <h3>Add a Comment</h3>
    <div class="input-group">
        <label for="subject">Subject line:</label>
        <input type="text" id="subject" name="subject" required>
    </div>
    <div class="input-group">
        <label for="comment">Comment:</label>
        <textarea id="comment" name="commentText" rows="5" required></textarea>
    </div>
    <div>
        <input type="submit" value="Submit Comment" class="submit-btn">
    </div>
    
</div>