package com.AllInSmall.demo.configuration;

import java.util.HashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.annotation.SessionScope;

import com.AllInSmall.demo.model.Order;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
public class SessionConfig {

	@Bean(name = "sessionOrder") // this is to be called in jsp
	@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS) 
	/*CGLIB proxy for the Order bean. 
	 * The proxy acts as a placeholder that delegates calls to the actual session-scoped instance of the bean
	 * Scoped proxies ensure that each user session has its own instance of the session-scoped bean.
	 * Avoid singleton bean to hold onto 1 session-scoped bean even after log out
	 */
	public Order sessionOrder() {
		log.info("I am creating session scope order");
		Order sessionOrder = new Order();
		sessionOrder.setOrderDetails(new HashSet<>());
		
		return sessionOrder;
		
	}
}
