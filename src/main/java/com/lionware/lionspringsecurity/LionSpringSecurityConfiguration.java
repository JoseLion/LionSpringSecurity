package com.lionware.lionspringsecurity;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class LionSpringSecurityConfiguration {
	@Bean
	public SecurityAuthenticationSuccessHandler securityAuthenticationSuccessHandler() {
		return new SecurityAuthenticationSuccessHandler();
	}
	
	@Bean
	public SecurityAuthenticationFailureHandler securityAuthenticationFailureHandler() {
		return new SecurityAuthenticationFailureHandler();
	}
}
