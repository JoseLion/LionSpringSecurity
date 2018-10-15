package com.lionware.lionspringsecurity.filters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lionware.lionspringsecurity.core.XSSRequestWrapper;
import com.lionware.lionspringsecurity.properties.LionSecurityProperties;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
	
	@Autowired
	private LionSecurityProperties securityProperties;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		
		this.setHeadersOnResponse(request, response);
		chain.doFilter(processRequest(request), response);
	}

	@Override
	public void destroy() {
		
	}
	
	public void setHeadersOnResponse(HttpServletRequest request, HttpServletResponse response) {
		String origin = request.getHeader(HttpHeaders.ORIGIN);
		
		if (securityProperties.getAllowedOrigins() != null && securityProperties.getAllowedOrigins().contains(origin)) {
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
			response.setHeader(HttpHeaders.VARY, HttpHeaders.ORIGIN);
		}
		
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, securityProperties.getAccessControl().getAllowCredentials().toString());
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, securityProperties.getAccessControl().allowHeadersToString());
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, securityProperties.getAccessControl().allowMethodsToString());
		response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, securityProperties.getAccessControl().exposeHeadersToString());
		response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, securityProperties.getAccessControl().getMaxAge().toString());
	}
	
	public HttpServletRequest processRequest(HttpServletRequest request) throws IOException {
		if (
			request != null &&
			request.getContentLengthLong() > 0 &&
			request.getHeader("Content-Encoding") == "gzip" &&
			!request.getMethod().equals(RequestMethod.OPTIONS.name())
		) {
			XSSRequestWrapper wrappedRequest = new XSSRequestWrapper(request);
			
			byte[] array = IOUtils.toByteArray(request.getInputStream());
			InputStream is = new ByteArrayInputStream(array);
			
			GZIPInputStream gzipStream = new GZIPInputStream(is);
			String gzip = IOUtils.toString(gzipStream, StandardCharsets.UTF_8);
			
			if (!gzip.isEmpty()) {
				wrappedRequest.resetInputStream(gzip.getBytes());
			}
			
			return wrappedRequest;
		}

		return request;
	}
	
}
