package com.lionware.lionspringsecurity;

import java.util.Collection;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;

public class SecurityAccessDecisionManager implements AccessDecisionManager {
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	
	private boolean allowIfAllAbstainDecisions = false;
	
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		
		int deny = 0;
		
		if(authentication == null) {
			deny++;
		}
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		for (ConfigAttribute attribute : configAttributes) {
			if (this.supports(attribute)) {
				deny++;
				// Attempt to find a matching granted authority
				for (GrantedAuthority authority : authorities) {
					if (attribute.getAttribute().equals(authority.getAuthority())) {
						return;
					}
				}
			}
		}
		
		if (deny > 0) {
			throw new AccessDeniedException(LionSecurityConst.ACCESS_DENIED);
		}
		
		// To get this far, every AccessDecisionVoter abstained
		checkAllowIfAllAbstainDecisions();
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	protected final void checkAllowIfAllAbstainDecisions() {
		if (!this.isAllowIfAllAbstainDecisions()) {
			throw new AccessDeniedException(messages.getMessage("CustomAccessDecisionManager.accessDenied", LionSecurityConst.ACCESS_DENIED));
		}
	}

	public MessageSourceAccessor getMessages() {
		return messages;
	}

	public void setMessages(MessageSourceAccessor messages) {
		this.messages = messages;
	}

	public boolean isAllowIfAllAbstainDecisions() {
		return allowIfAllAbstainDecisions;
	}

	public void setAllowIfAllAbstainDecisions(boolean allowIfAllAbstainDecisions) {
		this.allowIfAllAbstainDecisions = allowIfAllAbstainDecisions;
	}
}
