package com.lionware.lionspringsecurity;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Date;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.lionware.lionspringsecurity.core.Account;
import com.lionware.lionspringsecurity.core.AccountService;
import com.lionware.lionspringsecurity.core.LionSecurityException;
import com.lionware.lionspringsecurity.properties.PropertiesConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= { MockConfiguration.class, PropertiesConfiguration.class, AccountService.class })
public class AuthenticationFailureHandlerTest {
	
	@SpyBean
	private AuthenticationFailureHandler failureHandler;
	
	@SpyBean
	private Helpers helpers;
	
	@SpyBean
	private AccountService accountService;
	
	@Before
	public  void beforeEach() throws SQLException {
		helpers.dropAccountTable();
	}
	
	@Test
	public void whenExceptionIsAbstract_shouldWriteGenericResponse() throws AuthenticationException, IOException, ServletException, SQLException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		AuthenticationException exception = Mockito.mock(AuthenticationException.class);
		
		helpers.createBasicAccountTable();
		failureHandler.onAuthenticationFailure(request, response, exception);
		
		assertThat(response).isNotNull();
		assertThat(response.getContentAsString()).contains("Authentication failed with error:");
	}
	
	@Test
	public void whenUsernameIsNullAndBadCredentials_shouldWriteInvalidUsernamePassword() throws AuthenticationException, IOException, ServletException, SQLException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		BadCredentialsException exception = new BadCredentialsException("Bad Credentials");
		
		helpers.createBasicAccountTable();
		failureHandler.onAuthenticationFailure(request, response, exception);
		
		assertThat(response).isNotNull();
		assertThat(response.getContentAsString()).isEqualTo("Invalid username/password");
	}
	
	@Test
	public void whenUsernameExistsAndBadCredentials_shouldProcessFailedAttempt() throws AuthenticationException, IOException, ServletException, SQLException, LionSecurityException, InterruptedException {
		String decoded = "mock-user@test.com:mockPassword";
		String encoded = Base64.getEncoder().encodeToString(decoded.getBytes());
		helpers.createCompleteAccountTable();
		helpers.insertBasicAccount("mock-user@test.com", "1234");
		
		for(int i = 1; i <= 5; i++) {
			Date attemptDate = new Date();
			Thread.sleep(1000);
			MockHttpServletRequest request = new MockHttpServletRequest();
			MockHttpServletResponse response = new MockHttpServletResponse();
			BadCredentialsException exception = new BadCredentialsException("Bad Credentials");
			request.addHeader(HttpHeaders.AUTHORIZATION, encoded);
			
			failureHandler.onAuthenticationFailure(request, response, exception);
			
			Account account = accountService.populate("mock-user@test.com");
			String expected = MessageFormat.format("Invalid username/password. You''ve used {0} of 5 attempts", i);
			
			assertThat(account).isNotNull();
			assertThat(account.getLastAttempt()).isAfter(attemptDate);
			assertThat(account.getAttempts()).isEqualTo(i);
			assertThat(account.getIsLocked()).isEqualTo(i == 5);
			assertThat(account.getLockDate()).isEqualTo(i == 5 ? account.getLastAttempt() : null);
			
			assertThat(response).isNotNull();
			assertThat(response.getContentAsString()).isEqualTo(i != 5 ? expected : expected + ". Your account have been locked for 30 minutes");
		}
	}
	
	@Test
	public void whenUsernameIsNullAndLocked_shouldWriteUserLocked() throws AuthenticationException, IOException, ServletException, SQLException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LockedException exception = new LockedException("User locked");
		
		helpers.createBasicAccountTable();
		failureHandler.onAuthenticationFailure(request, response, exception);
		
		assertThat(response).isNotNull();
		assertThat(response.getContentAsString()).isEqualTo("Your account is locked");
	}
	
	@Test
	public void whenUsernameExistsAndLocked_shouldWriteLockedForTime() throws SQLException, AuthenticationException, IOException, ServletException {
		String decoded = "mock-user@test.com:mockPassword";
		String encoded = Base64.getEncoder().encodeToString(decoded.getBytes());
		helpers.createCompleteAccountTable();
		helpers.insertBasicAccount("mock-user@test.com", "1234");
		
		for(int i = 1; i <= 5; i++) {
			MockHttpServletRequest request = new MockHttpServletRequest();
			MockHttpServletResponse response = new MockHttpServletResponse();
			BadCredentialsException exception = new BadCredentialsException("Bad Credentials");
			request.addHeader(HttpHeaders.AUTHORIZATION, encoded);
			
			failureHandler.onAuthenticationFailure(request, response, exception);
		}
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LockedException exception = new LockedException("User locked");
		request.addHeader(HttpHeaders.AUTHORIZATION, encoded);
		
		failureHandler.onAuthenticationFailure(request, response, exception);
		
		assertThat(response).isNotNull();
		assertThat(response.getContentAsString()).isEqualTo("Your account is locked. Please try again in 30 minutes");
	}
	
	@Test
	public void whenIsDisabled_shouldWriteUserDisabled() throws SQLException, AuthenticationException, IOException, ServletException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		DisabledException exception = new DisabledException("User disabled");
		
		helpers.createBasicAccountTable();
		failureHandler.onAuthenticationFailure(request, response, exception);
		
		assertThat(response).isNotNull();
		assertThat(response.getContentAsString()).isEqualTo("Your account has been disabled");
	}
	
	@Test
	public void whenAccountHasExpired_shouldWriteAccountExpired() throws SQLException, AuthenticationException, IOException, ServletException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		AccountExpiredException exception = new AccountExpiredException("Account expired");
		
		helpers.createBasicAccountTable();
		failureHandler.onAuthenticationFailure(request, response, exception);
		
		assertThat(response).isNotNull();
		assertThat(response.getContentAsString()).isEqualTo("Your account has expired");
	}
	
	@Test
	public void whenCredentialsHasExpired_shouldWriteCredentialsExpired() throws SQLException, AuthenticationException, IOException, ServletException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		CredentialsExpiredException exception = new CredentialsExpiredException("Credentials expired");
		
		helpers.createBasicAccountTable();
		failureHandler.onAuthenticationFailure(request, response, exception);
		
		assertThat(response).isNotNull();
		assertThat(response.getContentAsString()).isEqualTo("Your account credentials have expired");
	}
}
