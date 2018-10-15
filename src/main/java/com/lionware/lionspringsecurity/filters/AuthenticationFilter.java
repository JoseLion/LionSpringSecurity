package com.lionware.lionspringsecurity.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.lionware.lionspringsecurity.core.Account;
import com.lionware.lionspringsecurity.core.AccountService;
import com.lionware.lionspringsecurity.core.LionSecurityConst;
import com.lionware.lionspringsecurity.core.LionSecurityException;
import com.lionware.lionspringsecurity.properties.LionSecurityProperties;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	@Autowired
	private AccountService accountService;
	
	private LionSecurityProperties securityProperties;
	
	public AuthenticationFilter(LionSecurityProperties securityProperties) {
		super(new AntPathRequestMatcher(securityProperties.getLoginPath(), securityProperties.getLoginMethod().name()));
		this.securityProperties = securityProperties;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			if (authentication == null) {
				Map<String, String> decodedHeader = getAuthHeaderDecoded(request, securityProperties.getBearer());
				String username = decodedHeader.get("username");
				String password = decodedHeader.get("password");
				
				if (username == null || username.isEmpty()) {
					throw new AuthenticationCredentialsNotFoundException("Username was not found on Authentication");
				}
				
				if (password == null || password.isEmpty()) {
					throw new AuthenticationCredentialsNotFoundException("Password was not found on Authentication");
				}
				
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
				Account account = accountService.populate(username);
				
				if (account != null) {
					account.setPassword("<Encrypted Password>");
					authenticationToken.setDetails(account);					
				}
				
				authentication = this.getAuthenticationManager().authenticate(authenticationToken);
			}
			
			return authentication;
		} catch (LionSecurityException | SQLException e) {
			throw new AuthenticationServiceException("[Authentication filter expection]: " + e.getMessage(), e);
		}
	}
	
	public static Map<String, String> getAuthHeaderDecoded(HttpServletRequest request, String bearer) throws UnsupportedEncodingException {
		Map<String, String> decoded = new HashMap<>();
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (authorizationHeader != null) {
			String authToken = authorizationHeader.replaceFirst(bearer, "").trim();
			
			byte[] valueDecodedBytes = Base64.getDecoder().decode(authToken.getBytes(StandardCharsets.UTF_8));
			String valueDecoded = new String(valueDecodedBytes, StandardCharsets.UTF_8);
			String usernamePasswordSeparated[] = valueDecoded.split(LionSecurityConst.CREDENTIALS_SEPARATOR);
			
			decoded.put("username", usernamePasswordSeparated[0]);
			decoded.put("password", usernamePasswordSeparated[1]);
		}
		
		return decoded;
	}
}
