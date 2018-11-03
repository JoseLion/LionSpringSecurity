package com.github.joselion.lionspringsecurity.core;

import org.springframework.security.core.AuthenticationException;

@FunctionalInterface
public interface LionSecurityAfterFailureHandler {
	
	public void accept(AuthenticationException authenticationException);

}
