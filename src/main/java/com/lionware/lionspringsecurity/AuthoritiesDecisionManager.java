package com.lionware.lionspringsecurity;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AuthoritiesDecisionManager implements AccessDecisionManager {
	
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		if(authentication == null) {
			throw new AccessDeniedException("Access denied!");
		}
		
		if (this.countDenniedVoted(configAttributes, authentication.getAuthorities()) > 0) {
			throw new AccessDeniedException("Access denied!");
		}
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		if (attribute != null && attribute.getAttribute() != null && !attribute.getAttribute().isEmpty()) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		if (clazz != null) {
			return true;
		}
		
		return false;
	}
	
	public int countDenniedVoted(Collection<ConfigAttribute> configAttributes, Collection<? extends GrantedAuthority> authorities) {
		if (configAttributes == null) {
			return 0;
		}
		
		int deny = 0;
		
		for (ConfigAttribute attribute : configAttributes) {
			if (attribute != null &&
				this.supports(attribute) &&
				authorities.stream().noneMatch(authority -> attribute.getAttribute().equals(authority.getAuthority()))
			) {
				deny++;
			}
		}
		
		return deny;
	}

}
