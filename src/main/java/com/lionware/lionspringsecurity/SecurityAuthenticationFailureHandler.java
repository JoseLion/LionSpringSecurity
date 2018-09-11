package com.lionware.lionspringsecurity;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class SecurityAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Autowired
	private LionSecurityFactory lionSecurityFactory;
	
	@Autowired
	private AuthenticationService authService;
	
	private AuthenticationFilter authenticationFilter = new AuthenticationFilter();
	
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws AuthenticationException, IOException, ServletException {
		SecurityConfig securityConfig = lionSecurityFactory.getConfig();
		String[] decoded = authenticationFilter.getAuthHeaderDecoded(request);
		String username = decoded[0];
		response.setContentType(LionSecurityConst.TEXT_UTF8_HEADER);
		
		if (!username.isEmpty() && exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
			securityConfig.setLastFailedAttempt(username, new Date(), authService.getExtra());
			
			if (securityConfig.getNumberOfAttempts(username, authService.getExtra()) != null) {
				securityConfig.setNumberOfAttempts(username, securityConfig.getNumberOfAttempts(username, authService.getExtra()) + 1, authService.getExtra());
				
				if (securityConfig.getNumberOfAttempts(username, authService.getExtra()) >= securityConfig.getMaxAttempts()) {
					securityConfig.setLockDate(username, new Date(), authService.getExtra());
				}
			}
		}
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
			response.getWriter().print(LionSecurityConst.BAD_CREDENTIALS);
		} else if (exception.getClass().isAssignableFrom(LockedException.class)) {
			response.getWriter().print(LionSecurityConst.USER_LOCKED);
		} else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
			response.getWriter().print(LionSecurityConst.USER_DISABLED);
		} else {
			response.getWriter().print("La autenticación falló: " + exception.getMessage());
		}
		
		response.flushBuffer();
		
		securityConfig.handleError();
	}
}
