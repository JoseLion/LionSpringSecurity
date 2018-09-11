package com.lionware.lionspringsecurity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;

public class SecurityAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Autowired
	private LionSecurityFactory lionSecurityFactory;
	
	@Autowired
	private AuthenticationService authService;
	
	private AuthenticationFilter authenticationFilter = new AuthenticationFilter();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		SecurityConfig securityConfig = lionSecurityFactory.getConfig();
		String username = authenticationFilter.getAuthHeaderDecoded(request)[0];
		
		if (securityConfig.getNumberOfAttempts(username, authService.getExtra()) != null) {
			if (!username.isEmpty() && securityConfig.getNumberOfAttempts(username, authService.getExtra()) != 0) {
				securityConfig.setNumberOfAttempts(username, 0, authService.getExtra());
				securityConfig.setLockDate(username, null, authService.getExtra());
				securityConfig.setLastFailedAttempt(username, null, authService.getExtra());
			}
		}
		
		CsrfToken token = (CsrfToken)request.getAttribute("_csrf");
		
		response.setContentType(LionSecurityConst.TEXT_UTF8_HEADER);
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().print(token.getToken());
        response.flushBuffer();
        
        securityConfig.handleSuccess();
	}
}
