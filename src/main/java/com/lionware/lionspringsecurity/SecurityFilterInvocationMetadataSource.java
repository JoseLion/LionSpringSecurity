package com.lionware.lionspringsecurity;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;


public class SecurityFilterInvocationMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	
	//private UrlCache urlCache;

	private HashMap<String, List<String>> urlRoles;


	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		FilterInvocation fi = (FilterInvocation) object;

		HttpServletRequest req = fi.getHttpRequest();
		String method = req.getMethod();
		
		String urlInc = fi.getRequestUrl();
		int lastSlash = urlInc.lastIndexOf("/");
		
		if(urlInc.substring(lastSlash).matches("^.+?\\d$")){
			urlInc = urlInc.substring(0, lastSlash);
		}
		
		if (urlRoles == null) {
			return null;
		}
		
		String url = method + ":" + urlInc;
		List<String> roles = urlRoles.get(url);
		
		if (roles == null) {
			throw new AccessDeniedException(messages.getMessage("CustomAccessDecisionManager.accessDenied", LionSecurityConst.ACCESS_DENIED));
		}
		
		String[] stockArr = new String[roles.size()];
		stockArr = roles.toArray(stockArr);

		return SecurityConfig.createList(stockArr);
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	/*public void afterPropertiesSet() throws Exception {
		appService.getUrlRoles();
		this.urlRoles = urlCache.getUrlRoles();
		logger.debug("Url Roles object :" + urlRoles);
	}


	public UrlCache getUrlCache() {
		return urlCache;
	}

	public void setUrlCache(UrlCache urlCache) {
		this.urlCache = urlCache;
	}*/

	public HashMap<String, List<String>> getUrlRoles() {
		return urlRoles;
	}

	public void setUrlRoles(HashMap<String, List<String>> urlRoles) {
		this.urlRoles = urlRoles;
	}

	public MessageSourceAccessor getMessages() {
		return messages;
	}

	public void setMessages(MessageSourceAccessor messages) {
		this.messages = messages;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
}
