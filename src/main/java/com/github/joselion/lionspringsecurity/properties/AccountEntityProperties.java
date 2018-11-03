package com.github.joselion.lionspringsecurity.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.joselion.lionspringsecurity.core.LionSecurityConst;

@ConfigurationProperties(prefix=LionSecurityConst.PROPS_PREFIX + ".account-entity")
public class AccountEntityProperties {
	
	/**
	 * (Optional) The name of the accountExpired column on the account entity table.
	 * This column will be used to determine if the account has expired.
	 * It's expected to be of type "boolean"
	 * 
	 */
	private String accountExpired = "account_expired";
	
	/**
	 * (Optional)The name of the attempts column on the account entity table.
	 * This column will be used to determine the number of attempts on authenticate that each account has.
	 * It's expected to be of type "number"
	 * 
	 */
	private String attempts = "attempts";
	
	/**
	 * (Optional) The name of the credentialsExpired column on the account entity table.
	 * This column will be used to determine if the accounts credentials has expired.
	 * It's expected to be of type "boolean"
	 * 
	 */
	private String credentialsExpired = "credentials_expired";
	
	/**
	 * (Required) The name of the id column on the account entity table.
	 * This column will be used as the primary key column.
	 * Used as surrogate PK so it's expected to be of type "boolean".
	 * It should have "unique" and "not null" constraints
	 * 
	 */
	private String id = "id";
	
	/**
	 * (Optional) The name of the isEnabled column on the account entity table.
	 * This column will be used to determine if the account is enabled.
	 * It's expected to be of type "boolean"
	 * 
	 */
	private String isEnabled = "is_enabled";
	
	/**
	 * (Optional) The name of the isLocked column on the account entity table.
	 * This column will be used to determine if the account has been temporary locked by reaching the number of failed authentication attempts.
	 * It's expected to be of type "boolean"
	 * 
	 */
	private String isLocked = "is_locked";
	
	/**
	 * (Optional) The name of the lastAttempt column on the account entity table.
	 * This column will be used to determine the date and time of the latest authentication attempt.
	 * It's expected to be of type "timestamp"
	 * 
	 */
	private String lastAttempt = "last_attempt";
	
	/**
	 * (Optional) The name of the lockDate column on the account entity table.
	 * This column will be used to determine the date and time of when an account was locked.
	 * It's expected to be of type "timestamp"
	 * 
	 */
	private String lockDate = "lock_date";
	
	/**
	 * (Required) The name of the password column on the account entity table.
	 * This column will be used to store the encrypted account password credential.
	 * It's expected to be of type "text"
	 * 
	 */
	private String password = "password";
	
	/**
	 * (Optional) The name of the roles column on the account entity table.
	 * This column will be used to store the account roles.
	 * It's expected to be of type "text[]"
	 * 
	 */
	private String roles = "roles";
	
	/**
	 * (Required) The name of the account entity table on your database
	 * 
	 */
	private String tableName = "account";
	
	/**
	 * (Required) The name of the username column on the account entity table.
	 * This column will be used to store the account username credential.
	 * It's expected to be of type "text"
	 * 
	 */
	private String username = "username";

	public String getAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(String accountExpired) {
		this.accountExpired = accountExpired;
	}

	public String getAttempts() {
		return attempts;
	}

	public void setAttempts(String attempts) {
		this.attempts = attempts;
	}

	public String getCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(String credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getLastAttempt() {
		return lastAttempt;
	}

	public void setLastAttempt(String lastAttempt) {
		this.lastAttempt = lastAttempt;
	}

	public String getLockDate() {
		return lockDate;
	}

	public void setLockDate(String lockDate) {
		this.lockDate = lockDate;
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
	
}
