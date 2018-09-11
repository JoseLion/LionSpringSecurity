package com.lionware.lionspringsecurity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
	@Value("${lion-security.allowed-origins}")
	private String[] allowedOrigins;
	
	@Value("${lion-security.compress-request}")
	private Boolean compressRequest;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		if (allowedOrigins == null) {
			throw new ServletException("No allowed origins defined in properties");
		}
		
		if (compressRequest == null) {
			compressRequest = false;
		}
		
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		String origin = request.getHeader(LionSecurityConst.ORIGIN);
		
		response.setHeader(LionSecurityConst.HEADER_ALLOW_ORIGIN, Arrays.asList(allowedOrigins).contains(origin) ? origin : "");
		response.setHeader(LionSecurityConst.HEADER_VARY, LionSecurityConst.VALUE_VARY);
		response.setHeader(LionSecurityConst.HEADER_MAX_AGE, LionSecurityConst.VALUE_MAX_AGE);
		response.setHeader(LionSecurityConst.HEADER_ALLOW_CREDENTIALS, LionSecurityConst.VALUE_ALLOW_CREDENTIALS);
		response.setHeader(LionSecurityConst.HEADER_ALLOW_METHODS, LionSecurityConst.VALUE_ALLOW_METHODS);
		response.setHeader(LionSecurityConst.HEADER_ALLOW_HEADERS, LionSecurityConst.VALUE_ALLOW_HEADERS);
		
		
		if (compressRequest) {
			boolean isCompressed = true;
			XSSRequestWrapper wrappedRequest = new XSSRequestWrapper(request);
			
			if (request != null && request.getContentType() != null && request.getContentType().length() >= 20) {
				if (request.getContentType().substring(0, 20).equals("multipart/form-data;")) {
					isCompressed = false;
				}
			}
			
			if (request != null && request.getContentLengthLong() > 0 && isCompressed) {
				byte[] array = IOUtils.toByteArray(request.getInputStream());
				InputStream is = new ByteArrayInputStream(array);
				
				GZIPInputStream gzipStream = new GZIPInputStream(is);
				String gzip = IOUtils.toString(gzipStream, StandardCharsets.UTF_8);
				
				if (!gzip.equals("")) {
					wrappedRequest.resetInputStream(gzip.getBytes());
				}
			} else {
				isCompressed = false;
			}
			
			if (!request.getMethod().equals("OPTIONS")) {
				if (isCompressed) {
					chain.doFilter(wrappedRequest, response);
				} else {
					chain.doFilter(request, response);
				}
				
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		
	}
}
