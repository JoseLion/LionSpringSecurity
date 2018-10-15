package com.lionware.lionspringsecurity.core;

import java.util.Date;
import java.util.List;

public class Account {
	
	private Long id;
	
	private String username;
	
	private String password;
	
	private List<String> roles;
	
	private Boolean isEnabled;
	
	private Boolean isLocked;
	
	private Date lockDate;
	
	private Date lastAttempt;
	
	private Integer attempts;
	
	private Boolean accountExpired;
	
	private Boolean credentialsExpired;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Boolean getIsLocked() {
		if (isLocked == null) {
			isLocked = false;
		}
		
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Date getLockDate() {
		return lockDate;
	}

	public void setLockDate(Date lockDate) {
		this.lockDate = lockDate;
	}

	public Date getLastAttempt() {
		return lastAttempt;
	}

	public void setLastAttempt(Date lastAttempt) {
		this.lastAttempt = lastAttempt;
	}

	public Integer getAttempts() {
		if (attempts == null) {
			attempts = 0;
		}
		
		return attempts;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	public Boolean getAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(Boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public Boolean getCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(Boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}
	
}
