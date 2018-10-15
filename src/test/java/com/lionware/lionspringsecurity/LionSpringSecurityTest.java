package com.lionware.lionspringsecurity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.sql.SQLException;
import java.util.Base64;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.lionware.lionspringsecurity.core.AccountService;
import com.lionware.lionspringsecurity.core.LionSecurityConst;
import com.lionware.lionspringsecurity.filters.AuthenticationFilter;
import com.lionware.lionspringsecurity.filters.CorsFilter;
import com.lionware.lionspringsecurity.filters.CsrfCookieFilter;
import com.lionware.lionspringsecurity.properties.LionSecurityProperties;
import com.lionware.lionspringsecurity.properties.PropertiesConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(
	webEnvironment=WebEnvironment.MOCK,
	classes={
		MockConfiguration.class,
		PropertiesConfiguration.class,
		AccountService.class,
		AuthenticationService.class,
		AuthenticationSuccessHandler.class,
		AuthenticationFailureHandler.class,
		LoginController.class,
		WebSecurityConfiguration.class
	},
	properties={
		LionSecurityConst.PROPS_PREFIX + ".login-path=/login",
		LionSecurityConst.PROPS_PREFIX + ".token-path=/token"
	}
)
@WebAppConfiguration
public class LionSpringSecurityTest {
	
	@Autowired
	private LionSecurityProperties securityProperties;
	
	@Autowired
	private AuthenticationFilter authenticationFilter;
	
	@SpyBean
	private CorsFilter corsFilter;
	
	@Autowired
	private CsrfCookieFilter csrfCookieFilter;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@SpyBean
	private Helpers helpers;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() throws SQLException {
		helpers.dropAccountTable();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilters(csrfCookieFilter, authenticationFilter, corsFilter)
			.build();
	}
	
	@Test
	public void whenNoUserOnLogin_shouldFailWithMessage() throws Exception {
		helpers.createBasicAccountTable();
		MvcResult result = mockMvc.perform(
			get("/login")
		)
		.andReturn();
		
		assertThat(result.getResponse().getStatus()).isEqualTo(401);
		assertThat(result.getResponse().getContentAsString()).isEqualTo("Authentication failed with error: Username was not found on Authentication");
	}
	
	@Test
	public void whenUserNotFound_shouldFailWithMessage() throws Exception {
		helpers.createBasicAccountTable();
		String decoded = "mock-user@test.com:12345678";
		String encoded = Base64.getEncoder().encodeToString(decoded.getBytes());
		
		MvcResult result = mockMvc.perform(
			get("/login")
			.header(HttpHeaders.AUTHORIZATION, securityProperties.getBearer() + " " + encoded)
		)
		.andReturn();
		
		assertThat(result.getResponse().getStatus()).isEqualTo(401);
		assertThat(result.getResponse().getContentAsString()).isEqualTo("Invalid username/password");
	}
	
	@Test
	public void whenWrongPassword_shouldFailWithMessage() throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(LionSecurityConst.ENCODER_STRENGTH);
		securityProperties.setEnableLock(false);
		helpers.createBasicAccountTable();
		helpers.insertBasicAccount("mock-user@test.com", encoder.encode("12345678"));
		String decoded = "mock-user@test.com:some-password";
		String encoded = Base64.getEncoder().encodeToString(decoded.getBytes());
		
		MvcResult result = mockMvc.perform(
			get("/login")
			.header(HttpHeaders.AUTHORIZATION, securityProperties.getBearer() + " " + encoded)
		)
		.andReturn();
		
		assertThat(result.getResponse().getStatus()).isEqualTo(401);
		assertThat(result.getResponse().getContentAsString()).isEqualTo("Invalid username/password");
	}
	
	@Test
	public void whenRightPassword_shouldAuthenticate() throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(LionSecurityConst.ENCODER_STRENGTH);
		helpers.createBasicAccountTable();
		helpers.insertBasicAccount("mock-user@test.com", encoder.encode("12345678"));
		String decoded = "mock-user@test.com:12345678";
		String encoded = Base64.getEncoder().encodeToString(decoded.getBytes());
		CsrfToken csrf = Mockito.mock(CsrfToken.class);
		when(csrf.getToken()).thenReturn("mock-csrf-token");
		
		MvcResult result = mockMvc.perform(
			get("/login")
			.requestAttr(CsrfToken.class.getName(), csrf)
			.header(HttpHeaders.AUTHORIZATION, securityProperties.getBearer() + " " + encoded)
		)
		.andReturn();
		
		assertThat(result.getResponse().getStatus()).isEqualTo(200);
		assertThat(result.getResponse().getContentAsString()).isEqualTo("mock-csrf-token");
		assertThat(result.getResponse().getCookie(securityProperties.getCsrfCookie())).isNotNull();
		assertThat(result.getResponse().getCookie(securityProperties.getCsrfCookie()).getValue()).isEqualTo("mock-csrf-token");
	}
	
	@Test
	public void whenRequestTokenAndUnauthorized_shouldRespondWith401Message() throws Exception {
		helpers.createBasicAccountTable();
		
		MvcResult result = mockMvc.perform(
			get("/token")
		)
		.andReturn();
		
		assertThat(result.getResponse().getStatus()).isEqualTo(401);
		assertThat(result.getResponse().getContentAsString()).isEqualTo("Unauthorized access!");
	}
	
	@Test
	public void whenRequestTokenAndAuthorized_shouldRespondWith200AndToken() throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(LionSecurityConst.ENCODER_STRENGTH);
		helpers.createBasicAccountTable();
		helpers.insertBasicAccount("mock-user@test.com", encoder.encode("12345678"));
		String decoded = "mock-user@test.com:12345678";
		String encoded = Base64.getEncoder().encodeToString(decoded.getBytes());
		CsrfToken csrf = Mockito.mock(CsrfToken.class);
		when(csrf.getToken()).thenReturn("mock-csrf-token");
		
		mockMvc.perform(
			get("/login")
			.requestAttr(CsrfToken.class.getName(), csrf)
			.header(HttpHeaders.AUTHORIZATION, securityProperties.getBearer() + " " + encoded)
		);
		
		MvcResult result = mockMvc.perform(
			get("/token")
		)
		.andReturn();
		
		assertThat(result.getResponse().getStatus()).isEqualTo(200);
		assertThat(result.getResponse().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
		assertThat(result.getResponse().getContentAsString()).matches("\\{\"token\": \".+\"\\}");
	}
}
