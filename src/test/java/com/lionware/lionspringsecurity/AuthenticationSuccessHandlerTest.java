package com.lionware.lionspringsecurity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.junit4.SpringRunner;

import com.lionware.lionspringsecurity.core.Account;
import com.lionware.lionspringsecurity.core.AccountService;
import com.lionware.lionspringsecurity.core.LionSecurityException;
import com.lionware.lionspringsecurity.properties.PropertiesConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= { MockConfiguration.class, PropertiesConfiguration.class, AccountService.class })
public class AuthenticationSuccessHandlerTest {
	
	@SpyBean
	private AuthenticationSuccessHandler successHandler;
	
	@SpyBean
	private Helpers helpers;
	
	@SpyBean
	private AccountService accountService;
	
	@Before
	public  void beforeEach() throws SQLException {
		helpers.dropAccountTable();
	}
	
	@Test
	public void whenAuthenticated_shouldSetPropertiesAndSendCSRFToken() throws SQLException, LionSecurityException, IOException, ServletException {
		helpers.createCompleteAccountTable();
		helpers.insertCompleteAccount("mock-user@test.com", "1234");
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		Authentication authentication = Mockito.mock(Authentication.class);
		CsrfToken csrfToken = Mockito.mock(CsrfToken.class);
		
		Account details = accountService.populate("mock-user@test.com");
		when(authentication.getDetails()).thenReturn(details);
		when(csrfToken.getToken()).thenReturn("mockToken");
		request.setAttribute(CsrfToken.class.getName(), csrfToken);
		
		successHandler.onAuthenticationSuccess(request, response, authentication);
		
		Account account = accountService.populate("mock-user@test.com");
		assertThat(account).isNotNull();
		assertThat(account.getAttempts()).isEqualTo(0);
		assertThat(account.getLastAttempt()).isNull();
		
		assertThat(response).isNotNull();
		assertThat(response.getContentAsString()).isEqualTo("mockToken");
	}
}
