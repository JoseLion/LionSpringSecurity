package com.lionware.lionspringsecurity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lionware.lionspringsecurity.core.Account;

public class AuthenticatedAccount implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private String username = "";
	
	private String password = "";
	
	private List<String> roles = new ArrayList<>();
	
	private Boolean isLocked;
	
	private Boolean isEnabled;
	
	private Boolean accountExpired;
	
	private Boolean credentialsExpired;
	
	public AuthenticatedAccount(Account account) {
		super();
		this.username = account.getUsername();
		this.password = account.getPassword();
		this.roles = account.getRoles();
		this.isLocked = account.getIsLocked();
		this.isEnabled = account.getIsEnabled();
		this.accountExpired = account.getAccountExpired();
		this.credentialsExpired = account.getCredentialsExpired();
	}
	
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (roles != null) {
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();
			
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
			
			return authorities;
		}
		
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		if (this.accountExpired != null) {
			return !this.accountExpired;
		}
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		if (isLocked == null) {
			return true;
		}
		
		return !isLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		if (this.credentialsExpired != null) {
			return !this.credentialsExpired;
		}
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		if (isEnabled == null) {
			return true;
		}
		
		return isEnabled;
	}
	
}