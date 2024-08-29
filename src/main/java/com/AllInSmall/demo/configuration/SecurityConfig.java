package com.AllInSmall.demo.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import com.AllInSmall.demo.repository.MyPersistentTokenRepositoryImpl;
import com.AllInSmall.demo.service.MyUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SecurityConfig {

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	@Autowired
	private MyPersistentTokenRepositoryImpl myPersistentTokenRepositoryImpl;

	@Autowired
	private RememberMeKeyManager keyManager;
	 @Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Bean
	public PasswordEncoder getPasswordEncoder() {
//		 return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return NoOpPasswordEncoder.getInstance();
	}

	

	@Bean
	public String rememberMeKey() {
		log.info("I am getting rememberMeKey bean in security config");
		return keyManager.getCurrentKey();

	}

	/*
	 * OPTION 1: use TokenBasedRememberMeServices - not saved in database
	 * 
	 * @Bean RememberMeServices rememberMeServices(MyUserDetailsService
	 * myUserDetailsService) { RememberMeTokenAlgorithm encodingAlgorithm =
	 * RememberMeTokenAlgorithm.SHA256; TokenBasedRememberMeServices rememberMe =
	 * new TokenBasedRememberMeServices(rememberMeKey, myUserDetailsService,
	 * encodingAlgorithm);
	 * rememberMe.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5); return
	 * rememberMe; }
	 */
	@Bean
	public PersistentTokenBasedRememberMeServices rememberMeServices(String rememberMeKey,
			MyUserDetailsService myUserDetailsService,
			MyPersistentTokenRepositoryImpl myPersistentTokenRepositoryImpl) {

		PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices(rememberMeKey,
				myUserDetailsService, myPersistentTokenRepositoryImpl);
		// additonal setting
		// services.setTokenValiditySeconds(86400); // 1 day
		// services.setParameter("remember-me");
		return services;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationSuccessHandler successHandler)
			throws Exception {

		http.authorizeHttpRequests((requests) -> requests.requestMatchers("/order/**","/index").hasRole("Staff")
				.requestMatchers("/order/**").hasAnyAuthority("ORDER_PROCESS") // addToOrder,viewOrder,resetOrder,viewSavedOrderList,addComment,
																				// updateStatus
				.requestMatchers("/placeOrder/**").hasRole("Staff").requestMatchers("/placeOrder/**")
				.hasAnyAuthority("ORDER_PLACE") // /placeOrder/paypal/create-order,/placeOrder/paypal/confirm,/placeOrder/cash/confirm,/placeOrder/paypal/cancel,/placeOrder/back,/placeOrder/paymentOption,/placeOrder/cash

				.requestMatchers("/order/cancel/approve").hasRole("Manager").requestMatchers("/order/cancel/approve")
				.hasAuthority("ORDER_CANCEL_APPROVE")

				.requestMatchers("/manageCategory").hasRole("Owner").requestMatchers("/manageCategory")
				.hasAuthority("CATEGORY_MANAGE")
				.requestMatchers("/category/**").hasRole("Owner").requestMatchers("/category/**").hasAuthority("CATEGORY_MANAGE") // add new category, update

				.requestMatchers("/manageProduct").hasRole("Owner")
				
				.requestMatchers("/product/**").hasRole("Owner").requestMatchers("/product/**").hasAuthority("PRODUCT_MANAGE") // add product,update
																									// product
																									// price,/product/details
				.requestMatchers("/manageUser", "/manageUser", "/manageOrder","/management").hasRole("Manager")
				
				.requestMatchers("/viewReport").hasRole("Manager").requestMatchers("/viewReport")
				.hasAuthority("REPORT_VIEW")
				
				.requestMatchers("/report/**").hasRole("Manager").requestMatchers("/report/**")
				.hasAuthority("REPORT_VIEW")
				
				.requestMatchers("/user/register/**").hasRole("Manager").requestMatchers("/user/register/**").hasAuthority("USER_REGISTER") // showForm, initiate registration,
																					// /register/confirmRegistration,/register/finaliseRegistration

				.requestMatchers("/WEB-INF/views/**", "/css/**", "/js/**", "/assets/**", "/actuator/**",
						"/register/confirmRegistration**", "/register/finaliseRegistration", "/forgotPassword",
						"/user/resetPassword","/accessDenied","/navigate","https://cdn.jsdelivr.net/npm/chart.js","data:image/**")
				.permitAll()
				.anyRequest().denyAll()

		).formLogin(form -> form.loginPage("/login").loginProcessingUrl("/perform_login").successHandler(successHandler) // Inject
																											// handler
				.failureUrl("/login?error=true").permitAll()).rememberMe((remember) -> remember // note: authentication
																				// scene
				.rememberMeServices(rememberMeServices(rememberMeKey(), myUserDetailsService,
								myPersistentTokenRepositoryImpl)))
				.logout(logout -> logout.logoutUrl("/perform_logout").logoutSuccessUrl("/login?logout=true")
						.permitAll())
				.httpBasic(withDefaults()).csrf(csrf -> csrf.disable())
				.exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler));
		return http.build();

	}

}
