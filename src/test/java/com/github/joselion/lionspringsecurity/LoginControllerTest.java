package com.github.joselion.lionspringsecurity;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.github.joselion.lionspringsecurity.properties.PropertiesConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={PropertiesConfiguration.class})
@WebMvcTest(controllers={ LoginController.class }, secure=false)
public class LoginControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@SpyBean
	private LoginController loginController;
	
	@Test
	public void whenLoginPathMathches_shouldRespondWithOkStatus() throws Exception {
		Principal mockPrincipal = () -> "mockUser";
		
		mockMvc.perform(
			get("/login")
			.principal(mockPrincipal)
		)
		.andExpect(status().isOk())
		.andExpect(jsonPath("name", is("mockUser")));
	}
	
	@Test
	public void whenLoginPathNotMatches_shouldRespondWithNotFoundStatus() throws Exception {
		Principal mockPrincipal = Mockito.mock(Principal.class);
		
		mockMvc.perform(
			get("/some-path")
			.principal(mockPrincipal)
		)
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void whenTokenPathMathches_shouldRespondWithOkStatus() throws Exception {
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.isAuthenticated()).thenReturn(true);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		MockHttpSession mockSession = new MockHttpSession();
		
		mockMvc.perform(
			get("/token")
			.session(mockSession)
		)
		.andExpect(status().isOk())
		.andExpect(jsonPath("token", is(mockSession.getId())));
	}
	
	@Test
	public void whenTokenPathNotMatches_shouldRespondWithNotFoundStatus() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		
		mockMvc.perform(
			get("/some-path")
			.session(mockSession)
		)
		.andExpect(status().isNotFound());
	}
}
