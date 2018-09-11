package com.lionware.lionspringsecurity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUser implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private Long id = 0L;

	private String user = "";
	
	private String password = "";
	
	private List<String> roles = new ArrayList<>();
	
	private Boolean isLocked;
	
	public AuthenticatedUser(Long id, String user, String password, List<String> roles, Boolean isLocked) {
		super();
		this.id = id;
		this.user = user;
		this.password = password;
		this.roles = roles;
		this.isLocked = isLocked;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (roles == null) {
			return null;
		} else {
			List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
			
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
			
			return authorities;
		}
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return user;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		if (isLocked == null) {
			return true;
		} else {
			return !isLocked;
		}
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public Long getId() {
		return id;
	}
}