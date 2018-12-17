package com.github.joselion.lionspringsecurity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;

import com.github.joselion.lionspringsecurity.core.Account;
import com.github.joselion.lionspringsecurity.core.AccountService;
import com.github.joselion.lionspringsecurity.core.LionSecurityAfterSuccessHandler;
import com.github.joselion.lionspringsecurity.core.LionSecurityException;
import com.github.joselion.lionspringsecurity.properties.LionSecurityProperties;

public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
	private LionSecurityProperties securityProperties;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired(required=false)
	private LionSecurityAfterSuccessHandler successHandler;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		if (securityProperties.getEnabled()) {
			try {
				Account account = (Account)authentication.getDetails();
				
				if (account.getAttempts() != 0) {
					account.setAttempts(0);
					account.setLastAttempt(null);
					accountService.update(account);
				}
				
				CsrfToken token = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
				response.setContentType(String.join("; ", MediaType.TEXT_HTML_VALUE, "charset=" + StandardCharsets.UTF_8));
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print(token.getToken());
				response.flushBuffer();
				
				if (successHandler != null) {
					successHandler.accept(authentication);
				}
			} catch (LionSecurityException | SQLException | RuntimeException e) {
				throw new ServletException("Security Exception: " + e.getMessage(), e);
			}
		}
	}
}
