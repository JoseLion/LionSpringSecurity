package com.github.joselion.lionspringsecurity.properties;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.github.joselion.lionspringsecurity.core.LionSecurityConst;

@ConfigurationProperties(prefix=LionSecurityConst.PROPS_PREFIX + ".access-control")
public class AccessControlProperties {
	
	/**
	 * Value for "Access-Control-Allow-Credentials" header
	 * 
	 */
	private Boolean allowCredentials = true;
	
	/**
	 * Value for "Access-Control-Allow-Headers" header
	 * 
	 */
	private List<String> allowHeaders = Arrays.asList(
		HttpHeaders.ACCEPT,
		HttpHeaders.AUTHORIZATION,
		HttpHeaders.CONTENT_ENCODING,
		HttpHeaders.CONTENT_TYPE,
		HttpHeaders.ORIGIN,
		LionSecurityConst.CSRF_HEADER_NAME,
		"X-Requested-With"
	);
	
	/**
	 * Value for "Access-Control-Allow-Methods" header
	 * 
	 */
	private List<HttpMethod> allowMethods = Arrays.asList(
		HttpMethod.DELETE,
		HttpMethod.GET,
		HttpMethod.OPTIONS,
		HttpMethod.POST,
		HttpMethod.PUT
	);
	
	/**
	 * Value for "Access-Control-Expose-Headers" header
	 * 
	 */
	private List<String> exposeHeaders = Arrays.asList(
		HttpHeaders.CONTENT_ENCODING,
		HttpHeaders.CONTENT_TYPE,
		LionSecurityConst.CSRF_HEADER_NAME
	);
	
	/**
	 * Value for "Access-Control-Max-Age" header
	 * 
	 */
	private Integer maxAge = 3600;
	
	public Boolean getAllowCredentials() {
		return allowCredentials;
	}

	public void setAllowCredentials(Boolean allowCredentials) {
		this.allowCredentials = allowCredentials;
	}

	public List<String> getAllowHeaders() {
		return allowHeaders;
	}

	public void setAllowHeaders(List<String> allowHeaders) {
		this.allowHeaders = allowHeaders;
	}

	public List<HttpMethod> getAllowMethods() {
		return allowMethods;
	}

	public void setAllowMethods(List<HttpMethod> allowMethods) {
		this.allowMethods = allowMethods;
	}

	public List<String> getExposeHeaders() {
		return exposeHeaders;
	}

	public void setExposeHeaders(List<String> exposeHeaders) {
		this.exposeHeaders = exposeHeaders;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public String allowHeadersToString() {
		return String.join(", ", this.allowHeaders);
	}
	
	public String allowMethodsToString() {
		List<String> methods = this.allowMethods.stream().map(header -> header.name()).collect(Collectors.toList());
		return String.join(", ", methods);
	}
	
	public String exposeHeadersToString() {
		return String.join(", ", this.exposeHeaders);
	}
}
