package com.lionware.lionspringsecurity;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class LionSecurityFactory {
	private SecurityConfig config;

	public LionSecurityFactory(SecurityConfig config) {
		super();
		this.config = config;
	}

	public SecurityConfig getConfig() {
		return config;
	}

	public void setConfig(SecurityConfig config) {
		this.config = config;
	}
}
