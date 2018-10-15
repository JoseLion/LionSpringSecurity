package com.github.joselion.lionspringsecurity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.joselion.lionspringsecurity.AuthoritiesDecisionManager;

@RunWith(SpringRunner.class)
public class AuthoritiesDecisionManagerTest {
	
	@Test(expected=AccessDeniedException.class)
	public void whenTheresNoAthentication_shouldThrowException() {
		AuthoritiesDecisionManager decisionManager = new AuthoritiesDecisionManager();
		decisionManager.decide(null, null, null);
	}
	
	@Test
	public void whenTheresNoConfigAttributes_shouldDenyAccessWithException() {
		AuthoritiesDecisionManager decisionManager = new AuthoritiesDecisionManager();
		int deny = decisionManager.countDenniedVoted(null, null);
		
		assertThat(deny).isEqualTo(0);
	}
	
	@Test
	public void whenAuthoritiesInConfigAttrs_shouldDenyBeZero() {
		AuthoritiesDecisionManager decisionManager = new AuthoritiesDecisionManager();
		
		SimpleGrantedAuthority adminRole = new SimpleGrantedAuthority("admin");
		SimpleGrantedAuthority userRole = new SimpleGrantedAuthority("user");
		ConfigAttribute attr = () -> "admin";
		
		int deny = decisionManager.countDenniedVoted(Arrays.asList(attr), Arrays.asList(adminRole, userRole));
		
		assertThat(deny).isEqualTo(0);
	}
	
	@Test
	public void whenAuthoritiesNotInConfigAttrs_shouldDenyBeOne() {
		AuthoritiesDecisionManager decisionManager = new AuthoritiesDecisionManager();
		
		SimpleGrantedAuthority adminRole = new SimpleGrantedAuthority("admin");
		ConfigAttribute adminAttr = () -> "admin";
		ConfigAttribute userAttr = () -> "user";
		
		int deny = decisionManager.countDenniedVoted(Arrays.asList(adminAttr, userAttr), Arrays.asList(adminRole));
		
		assertThat(deny).isEqualTo(1);
	}
	
	@Test(expected=AccessDeniedException.class)
	public void whenTheresDenyVotes_shouldDenyAccessWithException() {
		AuthoritiesDecisionManager decisionManager = Mockito.spy(AuthoritiesDecisionManager.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		
		when(decisionManager.countDenniedVoted(null, authentication.getAuthorities())).thenReturn(2);
		
		decisionManager.decide(authentication, null, null);
	}
	
}
