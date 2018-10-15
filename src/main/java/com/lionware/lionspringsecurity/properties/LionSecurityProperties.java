package com.lionware.lionspringsecurity.properties;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lionware.lionspringsecurity.core.LionSecurityConst;
/**
 * Lion Spring Security configuration properties
 * <p>
 * @author JoseLuis
 *
 */
@ConfigurationProperties(prefix=LionSecurityConst.PROPS_PREFIX)
public class LionSecurityProperties {
	
	/**
	 * Enables or disables spring security
	 * <p>
	 * @default value is {@value #enabled}
	 * 
	 */
	private Boolean enabled = true;
	
	/**
	 * List of allowed origins which can access the end-points
	 * <p>
	 * Setting this enables CORS feature to the security
	 * 
	 */
	private List<String> allowedOrigins;
	
	/**
	 * List of path matches that will ignore authorization
	 * <p>
	 * @default value is {@value #openPaths}
	 * 
	 */
	private List<String> openPaths = Arrays.asList("/open/**");
	
	/**
	 * The name of the cookie to store the CSRF token
	 * <p>
	 * @default value is {@value #csrfCookie}
	 */
	private String csrfCookie = "CSRF-TOKEN";
	
	/**
	 * The value to be used on the bearer of the Authorization header
	 * <p>
	 * @default value is {@value #bearer}
	 */
	private String bearer = "Bearer";
	
	/**
	 * If true, account locking is enabled
	 * <p>
	 * @default value is {@value #enableLock}
	 * 
	 */
	private Boolean enableLock = true;
	
	/**
	 * The time in milliseconds for which a user is locked
	 * <p>
	 * @default value is {@value #lockTime}
	 * 
	 */
	private Long lockTime = 30L * 60L * 1000L;
	
	/**
	 * The maximum number of attempts for the account to be locked
	 * <p>
	 * @default value is {@value #maxAttempts}
	 * 
	 */
	private Integer maxAttempts = 5;
	
	/**
	 * The login method to be used
	 * <p>
	 * @default value is {@value #loginMethod}
	 * 
	 */
	private RequestMethod loginMethod = RequestMethod.GET;
	
	/**
	 * The path to the login end-point
	 * <p>
	 * @default value is {@value #loginPath}
	 * 
	 */
	private String loginPath = "/login";
	
	/**
	 * The logout method to be used
	 * <p>
	 * @default value is {@value #logoutMethod}
	 * 
	 */
	private RequestMethod logoutMethod = RequestMethod.POST;
	
	/**
	 * The path to the logout end-point
	 * <p>
	 * @default value is {@value #logoutPath}
	 * 
	 */
	private String logoutPath = "/logout";
	
	/**
	 * The path to the token end-point
	 * <p>
	 * @default value is {@value #tokenPath}
	 * 
	 */
	private String tokenPath = "/token";
	
	/**
	 * Properties for all access control headers
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
