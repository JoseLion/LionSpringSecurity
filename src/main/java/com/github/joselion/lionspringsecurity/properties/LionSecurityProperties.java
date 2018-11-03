package com.github.joselion.lionspringsecurity.properties;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.joselion.lionspringsecurity.core.LionSecurityConst;
/**
 * Lion Spring Security configuration properties
 * @author JoseLion
 *
 */
@ConfigurationProperties(prefix=LionSecurityConst.PROPS_PREFIX)
public class LionSecurityProperties {
	
	/**
	 * Properties for all access control headers
	 * 
	 */
	private AccessControlProperties accessControl = new AccessControlProperties();
	
	/**
	 * Properties for the account entity in the database
	 * 
	 */
	private AccountEntityProperties accountEntity = new AccountEntityProperties();
	
	/**
	 * List of allowed origins which can access the end-points.
	 * Setting this property enables CORS feature to the security, removing it disabled CORS so all origins are allowed
	 * 
	 */
	private List<String> allowedOrigins;
	
	/**
	 * The value to be used for the bearer of the "Authorization" header
	 * 
	 */
	private String bearer = "Bearer";
	
	/**
	 * The name of the cookie where the CSRF token will be stored
	 * 
	 */
	private String csrfCookie = "CSRF-TOKEN";
	
	/**
	 * Enables or disables account locking for reaching the number of failed authentication attempts
	 * 
	 */
	private Boolean enableLock = true;
	
	/**
	 * Enables or disables spring security for all paths (with the "openPaths" exception)
	 * 
	 */
	private Boolean enabled = true;
	
	/**
	 * The time in milliseconds for which a user is locked if the account lock feature is enabled
	 * 
	 */
	private Long lockTime = 1800000L;
	
	/**
	 * The HTTP request method for the login end-point
	 * 
	 */
	private RequestMethod loginMethod = RequestMethod.GET;
	
	/**
	 * The routing path for the login end-point
	 * 
	 */
	private String loginPath = "/login";
	
	/**
	 * The HTTP request method for the logout end-point
	 * 
	 */
	private RequestMethod logoutMethod = RequestMethod.POST;
	
	/**
	 * The routing path for the logout end-point
	 * 
	 */
	private String logoutPath = "/logout";
	
	/**
	 * The maximum number of failed authentication attempts before the account gets locked if the account lock feature is enabled 
	 * 
	 */
	private Integer maxAttempts = 5;
	
	/**
	 * List of path matches that will ignore authentication and can be openly accessed
	 * 
	 */
	private List<String> openPaths = Arrays.asList("/open/**");
	
	/**
	 * The routing path the session token end-point.
	 * This end-point can be used to retrieve the session token of an authenticated account
	 * 
	 */
	private String tokenPath = "/token";

	public AccessControlProperties getAccessControl() {
		return accessControl;
	}

	public void setAccessControl(AccessControlProperties accessControl) {
		this.accessControl = accessControl;
	}

	public AccountEntityProperties getAccountEntity() {
		return accountEntity;
	}

	public void setAccountEntity(AccountEntityProperties accountEntity) {
		this.accountEntity = accountEntity;
	}

	public List<String> getAllowedOrigins() {
		return allowedOrigins;
	}

	public void setAllowedOrigins(List<String> allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	public String getBearer() {
		return bearer;
	}

	public void setBearer(String bearer) {
		this.bearer = bearer;
	}

	public String getCsrfCookie() {
		return csrfCookie;
	}

	public void setCsrfCookie(String csrfCookie) {
		this.csrfCookie = csrfCookie;
	}

	public Boolean getEnableLock() {
		return enableLock;
	}

	public void setEnableLock(Boolean enableLock) {
		this.enableLock = enableLock;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Long getLockTime() {
		return lockTime;
	}

	public void setLockTime(Long lockTime) {
		this.lockTime = lockTime;
	}

	public RequestMethod getLoginMethod() {
		return loginMethod;
	}

	public void setLoginMethod(RequestMethod loginMethod) {
		this.loginMethod = loginMethod;
	}

	public String getLoginPath() {
		return loginPath;
	}

	public void setLoginPath(String loginPath) {
		this.loginPath = loginPath;
	}

	public RequestMethod getLogoutMethod() {
		return logoutMethod;
	}

	public void setLogoutMethod(RequestMethod logoutMethod) {
		this.logoutMethod = logoutMethod;
	}

	public String getLogoutPath() {
		return logoutPath;
	}

	public void setLogoutPath(String logoutPath) {
		this.logoutPath = logoutPath;
	}

	public Integer getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(Integer maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public List<String> getOpenPaths() {
		return openPaths;
	}

	public void setOpenPaths(List<String> openPaths) {
		this.openPaths = openPaths;
	}

	public String getTokenPath() {
		return tokenPath;
	}

	public void setTokenPath(String tokenPath) {
		this.tokenPath = tokenPath;
	}
	
}
