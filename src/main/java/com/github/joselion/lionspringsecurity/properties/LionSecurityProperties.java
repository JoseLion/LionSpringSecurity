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
	 * Enables or disables spring security
	 * 
	 */
	private Boolean enabled = true;
	
	/**
	 * List of allowed origins which can access the end-points
	 * 
	 * Setting this property enables CORS feature to the security, removing it disabled CORS so all origins are allowed
	 * 
	 */
	private List<String> allowedOrigins;
	
	/**
	 * List of path matches that will ignore authorization
	 * 
	 */
	private List<String> openPaths = Arrays.asList("/open/**");
	
	/**
	 * The name of the cookie to store the CSRF token
	 * 
	 */
	private String csrfCookie = "CSRF-TOKEN";
	
	/**
	 * The value to be used on the bearer of the Authorization header
	 * 
	 */
	private String bearer = "Bearer";
	
	/**
	 * Enables or disables account locking feature
	 * 
	 */
	private Boolean enableLock = true;
	
	/**
	 * The time in milliseconds for which a user is locked (enableLock must be true)
	 * 
	 */
	private Long lockTime = new Long(1800000);
	
	/**
	 * The maximum number of attempts for the account to be locked
	 * 
	 */
	private Integer maxAttempts = 5;
	
	/**
	 * Default: get
	 * 
	 * The method for the login end-point
	 * 
	 */
	private RequestMethod loginMethod = RequestMethod.GET;
	
	/**
	 * The path for the login end-point
	 * 
	 */
	private String loginPath = "/login";
	
	/**
	 * Default: post
	 * 
	 * The method for the logout end-point
	 * 
	 */
	private RequestMethod logoutMethod = RequestMethod.POST;
	
	/**
	 * The path to the logout end-point
	 * 
	 */
	private String logoutPath = "/logout";
	
	/**
	 * The path to the session token end-point
	 * 
	 */
	private String tokenPath = "/token";
	
	/**
	 * Properties for all access control headers
	 * 
	 */
	private AccessControlProperties accessControl = new AccessControlProperties();
	
	/**
	 * Properties for the account entity
	 * 
	 */
	private AccountEntityProperties accountEntity = new AccountEntityProperties();

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getAllowedOrigins() {
		return allowedOrigins;
	}

	public void setAllowedOrigins(List<String> allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}

	public List<String> getOpenPaths() {
		return openPaths;
	}

	public void setOpenPaths(List<String> openPaths) {
		this.openPaths = openPaths;
	}

	public String getCsrfCookie() {
		return csrfCookie;
	}

	public void setCsrfCookie(String csrfCookie) {
		this.csrfCookie = csrfCookie;
	}

	public String getBearer() {
		return bearer;
	}

	public void setBearer(String bearer) {
		this.bearer = bearer;
	}

	public Boolean getEnableLock() {
		return enableLock;
	}

	public void setEnableLock(Boolean enableLock) {
		this.enableLock = enableLock;
	}

	public Long getLockTime() {
		return lockTime;
	}

	public void setLockTime(Long lockTime) {
		this.lockTime = lockTime;
	}

	public Integer getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(Integer maxAttempts) {
		this.maxAttempts = maxAttempts;
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

	public String getTokenPath() {
		return tokenPath;
	}

	public void setTokenPath(String tokenPath) {
		this.tokenPath = tokenPath;
	}

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
	
}
