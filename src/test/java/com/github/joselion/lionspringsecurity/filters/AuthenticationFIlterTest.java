package com.github.joselion.lionspringsecurity.filters;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import javax.servlet.ServletException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.joselion.lionspringsecurity.MockConfiguration;
import com.github.joselion.lionspringsecurity.core.AccountService;
import com.github.joselion.lionspringsecurity.core.LionSecurityConst;
import com.github.joselion.lionspringsecurity.filters.AuthenticationFilter;
import com.github.joselion.lionspringsecurity.properties.LionSecurityProperties;
import com.github.joselion.lionspringsecurity.properties.PropertiesConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {
	MockConfiguration.class,
	PropertiesConfiguration.class,
	AccountService.class
})
public class AuthenticationFIlterTest {
	
	@SpyBean
	private LionSecurityProperties securityProperties;
	
	@Test
	public void whenCredentialsInHeader_shouldGetAuthHeaderDecoded() throws AuthenticationException, IOException, ServletException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		String userAndPass = "mockuser@test.com" + LionSecurityConst.CREDENTIALS_SEPARATOR + "mockpassword";
		request.addHeader(HttpHeaders.AUTHORIZATION, securityProperties.getBearer() + " " + Base64.getEncoder().encodeToString(userAndPass.getBytes(StandardCharsets.UTF_8)));
		
		Map<String, String> decoded = AuthenticationFilter.getAuthHeaderDecoded(request, securityProperties.getBearer());
		assertThat(decoded).isNotNull();
		assertThat(decoded).hasSize(2);
		assertThat(decoded).containsEntry("username", "mockuser@test.com");
		assertThat(decoded).containsEntry("password", "mockpassword");
	}
	
	@Test
	public void whenCredentialsNotInHeader_shoudReturnEmptiMap() throws UnsupportedEncodingException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		Map<String, String> decoded = AuthenticationFilter.getAuthHeaderDecoded(request, securityProperties.getBearer());
		assertThat(decoded).isNotNull();
		assertThat(decoded).hasSize(0);
	}
	
}
