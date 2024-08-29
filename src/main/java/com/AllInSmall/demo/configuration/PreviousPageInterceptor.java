package com.AllInSmall.demo.configuration;

import java.util.Stack;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class PreviousPageInterceptor implements HandlerInterceptor {

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
		
		HttpSession session = request.getSession();
        String currentPath = request.getRequestURI();     // Initialize the stack if it doesn't exist
        @SuppressWarnings("unchecked")
		Stack<String> navigationStack = (Stack<String>) session.getAttribute("navigationStack");
        if (navigationStack == null) {
            navigationStack = new Stack<>();
            session.setAttribute("navigationStack", navigationStack);
        }
     // Check if the current path is in the excluded paths
        if (currentPath.contains("/css/")|| 
        	currentPath.contains("/js/")||
        	currentPath.contains("/assets/")||
        	currentPath.contains("/navigate")||
        	currentPath.contains("/login")||
        	currentPath.contains("/error")||
        	currentPath.contains("/favicon.ico")||
        	currentPath.contains("/user/oauth2callback")||
        	currentPath.contains("/forgotPassword")||
        	currentPath.contains("/user/resetPassword")||
        	currentPath.contains("/accessDenied")||
        	currentPath.contains("/register/confirmRegistration")||
        	currentPath.contains("/register/finaliseRegistration")||
        	currentPath.contains("/report/salesByUser/data")||
        	currentPath.contains("/report/salesByProduct/data")||
        	currentPath.contains("/report/ordersByStatus/data")||
        	currentPath.contains("/report/ordersByStatus/details")||
        	currentPath.contains("/category/{categoryId}/size"))
        {
            return true; // Skip adding to the navigation stack
        }

        // Push the current path onto the stack if it's not the same as the last visited
        if (navigationStack.isEmpty() || !navigationStack.peek().equals(currentPath)) {
        	log.info("add current path to stack: "+ currentPath);
            navigationStack.push(currentPath);
        }
        return true;
    }
}
	

