package com.github.joselion.lionspringsecurity.properties;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.joselion.lionspringsecurity.core.LionSecurityConst;
import com.github.joselion.lionspringsecurity.properties.LionSecurityProperties;
import com.github.joselion.lionspringsecurity.properties.PropertiesConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={PropertiesConfiguration.class})
public class SecurityPropertiesTest {
	@Autowired
	private LionSecurityProperties properties;

	@Test
	public void should_getDefaultProperties() {
		assertThat(properties).isNotNull();
		assertThat(properties.getEnabled()).isEqualTo(true);
		assertThat(properties.getAllowedOrigins()).isNull();
		assertThat(properties.getOpenPaths()).contains("/open/**");
		assertThat(properties.getCsrfCookie()).isEqualTo("CSRF-TOKEN");
		assertThat(properties.getBearer()).isEqualTo("Bearer");
		assertThat(properties.getEnableLock()).isEqualTo(true);
		assertThat(properties.getLockTime()).isEqualTo(30L * 60L * 1000L);
		assertThat(properties.getMaxAttempts()).isEqualTo(5);
		assertThat(properties.getLoginMethod()).isEqualTo(RequestMethod.GET);
		assertThat(properties.getLoginPath()).isEqualTo("/login");
		assertThat(properties.getLogoutMethod()).isEqualTo(RequestMethod.POST);
		assertThat(properties.getLogoutPath()).isEqualTo("/logout");
		assertThat(properties.getTokenPath()).isEqualTo("/token");
		
		assertThat(properties.getAccountEntity()).isNotNull();
		assertThat(properties.getAccountEntity().getTableName()).isEqualTo("account");
		assertThat(properties.getAccountEntity().getId()).isEqualTo("id");
		assertThat(properties.getAccountEntity().getUsername()).isEqualTo("username");
		assertThat(properties.getAccountEntity().getPassword()).isEqualTo("password");
		assertThat(properties.getAccountEntity().getRoles()).isEqualTo("roles");
		assertThat(properties.getAccountEntity().getIsEnabled()).isEqualTo("is_enabled");
		assertThat(properties.getAccountEntity().getIsLocked()).isEqualTo("is_locked");
		assertThat(properties.getAccountEntity().getLockDate()).isEqualTo("lock_date");
		assertThat(properties.getAccountEntity().getLastAttempt()).isEqualTo("last_attempt");
		assertThat(properties.getAccountEntity().getAttempts()).isEqualTo("attempts");
		assertThat(properties.getAccountEntity().getAccountExpired()).isEqualTo("account_expired");
		assertThat(properties.getAccountEntity().getCredentialsExpired()).isEqualTo("credentials_expired");
		
		assertThat(properties.getAccessControl()).isNotNull();
		assertThat(properties.getAccessControl().getAllowCredentials()).isEqualTo(true);
		assertThat(properties.getAccessControl().getAllowHeaders()).contains(
			HttpHeaders.ACCEPT,
			HttpHeaders.AUTHORIZATION,
			HttpHeaders.CONTENT_ENCODING,
			HttpHeaders.CONTENT_TYPE,
			HttpHeaders.ORIGIN,
			"X-Requested-With",
			LionSecurityConst.CSRF_HEADER_NAME
		);
		assertThat(properties.getAccessControl().getAllowMethods()).contains(
			HttpMethod.OPTIONS,
			HttpMethod.GET,
			HttpMethod.POST,
			HttpMethod.PUT,
			HttpMethod.DELETE
		);
		assertThat(properties.getAccessControl().getExposeHeaders()).contains(
			HttpHeaders.CONTENT_ENCODING,
			HttpHeaders.CONTENT_TYPE,
			LionSecurityConst.CSRF_HEADER_NAME
		);
		assertThat(properties.getAccessControl().getMaxAge()).isEqualTo(3600);
		assertThat(properties.getAccessControl().allowHeadersToString()).isEqualTo("Accept, Authorization, Content-Encoding, Content-Type, Origin, X-Requested-With, " + LionSecurityConst.CSRF_HEADER_NAME);
		assertThat(properties.getAccessControl().allowMethodsToString()).isEqualTo("OPTIONS, GET, POST, PUT, DELETE");
		assertThat(properties.getAccessControl().exposeHeadersToString()).isEqualTo("Content-Encoding, Content-Type, " + LionSecurityConst.CSRF_HEADER_NAME);
	}
	
}
