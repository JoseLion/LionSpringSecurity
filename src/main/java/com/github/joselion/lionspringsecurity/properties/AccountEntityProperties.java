package com.github.joselion.lionspringsecurity.properties;

public class AccountEntityProperties {
	
	/**
	 * The name of the accounts table on your database
	 * <p>
	 * @default value is {@value #tableName}
	 * 
	 */
	private String tableName = "account";
	
	/**
	 * The name of the ID column on the {@value #tableName} table
	 * <p>
	 * @default value is {@value #id}
	 * 
	 */
	private String id = "id";
	
	/**
	 * The name of the username column on the {@value #tableName} table
	 * <p>
	 * @default value is {@value #username}
	 * 
	 */
	private String username = "username";
	
	/**
	 * The name of the password column on the {@value #tableName} table
	 * <p>
	 * @default value is {@value #password}
	 * 
	 */
	private String password = "password";
	
	/**
	 * The name of the roles column on the {@value #tableName} table
	 * <p>
	 * @default value is {@value #roles}
	 * 
	 */
	private String roles = "roles";
	
	/**
	 * The name of the isEnabled column on the {@value #tableName} table
	 * <p>
	 * @default value is {@value #isEnabled}
	 * 
	 */
	private String isEnabled = "is_enabled";
	
	/**
	 * The name of the isLocked column on the {@value #tableName} table
	 * <p>
	 * @default value is {@value #isLocked}
	 * 
	 */
	private String isLocked = "is_locked";
	
	/**
	 * The name of the lockDate column on the {@value #tableName} table
	 * <p>
	 * @default value is {@value #lockDate}
	 * 
	 */
	private String lockDate = "lock_date";
	
	/**
	 * The name of the lastAttempt column on the {@value #tableName} table
	 * <p>
	 * @default value is {@value #lastAttempt}
	 * 
	 */
	private String lastAttempt = "last_attempt";
	
	/**
	 * The name of the attempts column on the {@value #tableName} table
	 * <p>
	 * @default value is {@value #attempts}
	 * 
	 */
	private String attempts = "attempts";
	
	/**
	 * The name of the accountExpired column on the {@value #tableName} table
	 * <p>
	 * @default value is {@value #accountExpired}
	 * 
	 */
	private String accountExpired = "account_expired";
	
	/**
	 * The name of the credentialsExpired column on the {@value #tableName} table
	 * <p>
	 * @default value is {@value #credentialsExpired}
	 * 
	 */
	private String credentialsExpired = "credentials_expired";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}

	public String getLockDate() {
		return lockDate;
	}

	public void setLockDate(String lockDate) {
		this.lockDate = lockDate;
	}

	public String getLastAttempt() {
		return lastAttempt;
	}

	public void setLastAttempt(String lastAttempt) {
		this.lastAttempt = lastAttempt;
	}

	public String getAttempts() {
		return attempts;
	}

	public void setAttempts(String attempts) {
		this.attempts = attempts;
	}

	public String getAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(String accountExpired) {
		this.accountExpired = accountExpired;
	}

	public String getCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(String credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}
	
}
