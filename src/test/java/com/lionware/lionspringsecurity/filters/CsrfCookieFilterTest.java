package com.lionware.lionspringsecurity.filters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.junit4.SpringRunner;

import com.lionware.lionspringsecurity.properties.LionSecurityProperties;
import com.lionware.lionspringsecurity.properties.PropertiesConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={ PropertiesConfiguration.class })
public class CsrfCookieFilterTest {
	
	@Autowired
	private LionSecurityProperties securityProperties;
	
	@Test
	public void whenTokenIsNotInAttributes_shouldReturnNullCookie() {
		CsrfCookieFilter filter = new CsrfCookieFilter(securityProperties.getCsrfCookie());
		MockHttpServletRequest request = new MockHttpServletRequest();
		Cookie cookie = filter.createOrReuseCookie(request);
		
		assertThat(cookie).isNull();
	}
	
	@Test
	public void whenCookieAlreadyInRequest_shouldReturnTheCookie() {
		CsrfCookieFilter filter = new CsrfCookieFilter(securityProperties.getCsrfCookie());
		CsrfToken csrf = Mockito.mock(CsrfToken.class);
		when(csrf.getToken()).thenReturn("some_fake_csrf_cookie");
		Cookie mockCookie = new Cookie(securityProperties.getCsrfCookie(), "some_fake_csrf_cookie");
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(CsrfToken.class.getName(), csrf);
		request.setCookies(mockCookie);
		
		Cookie cookie = filter.createOrReuseCookie(request);
		
		assertThat(cookie).isNotNull();
		assertThat(cookie.getValue()).isEqualTo(mockCookie.getValue());
	}
	
	@Test
	public void whenCookieInRequestAndDifferentFromToken_shouldReturnNewCookie() {
		CsrfCookieFilter filter = new CsrfCookieFilter(securityProperties.getCsrfCookie());
		CsrfToken csrf = Mockito.mock(CsrfToken.class);
		when(csrf.getToken()).thenReturn("some_fake_csrf_cookie");
		Cookie mockCookie = new Cookie(securityProperties.getCsrfCookie(), "another_csrf_token");
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(CsrfToken.class.getName(), csrf);
		request.setCookies(mockCookie);
		
		Cookie cookie = filter.createOrReuseCookie(request);
		
		assertThat(cookie).isNotNull();
		assertThat(cookie.getValue()).isEqualTo(csrf.getToken());
	}
	
	@Test
	public void whenNoCookieInRequest_shouldReturnNewCookie() {
		CsrfCookieFilter filter = new CsrfCookieFilter(securityProperties.getCsrfCookie());
		CsrfToken csrf = Mockito.mock(CsrfToken.class);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(CsrfToken.class.getName(), csrf);
		
		Cookie cookie = filter.createOrReuseCookie(request);
		
		assertThat(cookie).isNotNull();
		assertThat(cookie.getValue()).isEqualTo(csrf.getToken());
	}
}
