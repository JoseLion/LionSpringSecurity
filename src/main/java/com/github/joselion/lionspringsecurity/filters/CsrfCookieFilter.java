package com.github.joselion.lionspringsecurity.filters;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

public class CsrfCookieFilter extends OncePerRequestFilter {
	
	private String csrfCookieName;
	
	public CsrfCookieFilter(String csrfCookieName) {
		super();
		this.csrfCookieName = csrfCookieName;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Cookie cookie = createOrReuseCookie(request);
		
		if (cookie != null) {
			response.addCookie(cookie);	
		}
		
	    filterChain.doFilter(request, response);
	}
	
	public Cookie createOrReuseCookie(HttpServletRequest request) {
		CsrfToken csrf = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
		
		if (csrf == null) {
			return null;
		}
		
		Cookie cookie = WebUtils.getCookie(request, csrfCookieName);
		String token = csrf.getToken();
		
		if (cookie == null || !Objects.equals(token, cookie.getValue())) {
			cookie = new Cookie(csrfCookieName, token);
			cookie.setPath("/");
		}
		
		return cookie;
	}
}
