package com.lionware.lionspringsecurity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	@Autowired
	private AuthenticationService authenticationService;
	
	protected AuthenticationFilter() {
		super(new AntPathRequestMatcher(LionSecurityConst.LOGIN_BASE_URL + LionSecurityConst.LOGIN_USER_URL, LionSecurityConst.ANT_REQUEST_MATCHER_METHOD));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth == null) {
			String username = getAuthHeaderDecoded(request)[0].trim();
			String password = getAuthHeaderDecoded(request)[1];
			
			if (password.isEmpty()) {
				password = "****";
			}
			
			auth = getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password));
		}
		
		return auth;
	}
	
	public String[] getAuthHeaderDecoded(HttpServletRequest request) throws UnsupportedEncodingException {
		if (authenticationService != null) {
			authenticationService.setExtra(request.getHeader(LionSecurityConst.EXTRA_HEADER));
		}
		
		String authorizationHeader = request.getHeader(LionSecurityConst.AUTHORIZATION_HEADER);
		
		if (authorizationHeader != null) {
			String authorizationEncoded = authorizationHeader.substring(LionSecurityConst.AUTHORIZATION_HEADER_OFFSET);
			byte[] valueDecodedBytes = Base64.getDecoder().decode(authorizationEncoded.getBytes(LionSecurityConst.UTF8));
			String valueDecoded = new String(valueDecodedBytes, LionSecurityConst.UTF8);
			String usernamePasswordSeparated[] = valueDecoded.split(LionSecurityConst.HEADER_SPLITTER);
			
			return usernamePasswordSeparated;
		} else {
			return new String[]{"", ""};
		}
	}
}
