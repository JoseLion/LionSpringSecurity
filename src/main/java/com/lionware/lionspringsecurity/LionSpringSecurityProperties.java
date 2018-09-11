package com.lionware.lionspringsecurity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="lion-security")
public class LionSpringSecurityProperties {
	
	private Boolean enabled = true;
	
	private String[] allowedOrigins;
	
	private Boolean compressRequest = true;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String[] getAllowedOrigins() {
		return allowedOrigins;
	}

	public void setAllowedOrigins(String[] allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	public Boolean getCompressRequest() {
		return compressRequest;
	}

	public void setCompressRequest(Boolean compressRequest) {
		this.compressRequest = compressRequest;
	}
	
	
}
