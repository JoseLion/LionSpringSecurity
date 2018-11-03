package com.github.joselion.lionspringsecurity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.github.joselion.lionspringsecurity.core.Account;
import com.github.joselion.lionspringsecurity.core.AccountService;
import com.github.joselion.lionspringsecurity.core.LionSecurityAfterFailureHandler;
import com.github.joselion.lionspringsecurity.core.LionSecurityException;
import com.github.joselion.lionspringsecurity.filters.AuthenticationFilter;
import com.github.joselion.lionspringsecurity.properties.LionSecurityProperties;

public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private LionSecurityProperties securityProperties;
	
	@Autowired(required=false)
	private LionSecurityAfterFailureHandler errorHandler;
	
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws AuthenticationException, IOException, ServletException {
		try {
			Date today = new Date();
			Map<String, String> decoded = AuthenticationFilter.getAuthHeaderDecoded(request, securityProperties.getBearer());
			Account account = accountService.populate(decoded.get("username"));
			
			response.setContentType(String.join("; ", MediaType.TEXT_HTML_VALUE, "charset=" + StandardCharsets.UTF_8));
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			
			if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
				String message = "Invalid username/password";
				
				if (account != null && securityProperties.getEnableLock()) {
					account.setLastAttempt(today);
					account.setAttempts(account.getAttempts() + 1);
					
					if (account.getAttempts() >= securityProperties.getMaxAttempts()) {
						account.setIsLocked(true);
						account.setLockDate(today);
					}
					
					account = accountService.update(account);
					
					message += ". You've used " + account.getAttempts() + " of " + securityProperties.getMaxAttempts() + " attempts";
					
					if (account.getIsLocked()) {
						Long minutes = Math.round(securityProperties.getLockTime() / 1000.0 / 60.0);
						String qualifier = minutes == 1 ? "minute" : "minutes";
						message += ". Your account have been locked for " + minutes + " " + qualifier;
					}
				}
				
				response.getWriter().print(message);
			} else if (exception.getClass().isAssignableFrom(LockedException.class)) {
				String message = "Your account is locked";
				
				if (account != null) {
					Long millisLeft = securityProperties.getLockTime() - (today.getTime() - account.getLockDate().getTime());
					Long minutes = Math.round(millisLeft / 1000.0 / 60.0);
					String qualifier = minutes == 1 ? "minute" : "minutes";
					message += ". Please try again in " + minutes + " " + qualifier;
				}
				
				response.getWriter().print(message);
			} else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
				response.getWriter().print("Your account has been disabled");
			} else if (exception.getClass().isAssignableFrom(AccountExpiredException.class)) {
				response.getWriter().print("Your account has expired");
			} else if (exception.getClass().isAssignableFrom(CredentialsExpiredException.class)) {
				response.getWriter().print("Your account credentials have expired");
			} else {
				response.getWriter().print("Authentication failed with error: " + exception.getMessage());
			}
			
			response.flushBuffer();
			
			if (errorHandler != null) {
				errorHandler.accept(exception);
			}
		} catch (LionSecurityException | SQLException e) {
			throw new ServletException("Security exception: " + e.getMessage(), e);
		}
	}
}
