package com.github.joselion.lionspringsecurity.core;

import org.springframework.security.core.Authentication;

@FunctionalInterface
public interface LionSecurityAfterSuccessHandler {
	
	public void accept(Authentication authentication);

}
