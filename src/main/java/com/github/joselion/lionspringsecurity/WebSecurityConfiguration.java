package com.github.joselion.lionspringsecurity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.github.joselion.lionspringsecurity.core.LionSecurityConst;
import com.github.joselion.lionspringsecurity.filters.AuthenticationFilter;
import com.github.joselion.lionspringsecurity.filters.CsrfCookieFilter;
import com.github.joselion.lionspringsecurity.properties.LionSecurityProperties;

@Configuration
@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private LionSecurityProperties securityProperties;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private AuthenticationFailureHandler securityAuthenticationFailureHandler;
	
	@Autowired
	private AuthenticationSuccessHandler securityAuthenticationSuccessHandler;
	
	@Autowired(required=false)
	private HandlerExceptionResolver exceptionResolver;
	
	@Autowired(required=false)
	private AuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Autowired(required=false)
	private AccessDeniedHandler customAccessDeniedHandler;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		if(securityProperties.getEnabled()) {
			List<String> openList = new ArrayList<>(securityProperties.getOpenPaths());
			String[] openPaths = openList.toArray(new String[openList.size()]);
			
			List<String> ignoreList = new ArrayList<>(securityProperties.getOpenPaths());
			ignoreList.add(securityProperties.getLoginPath());
			ignoreList.add(securityProperties.getLogoutPath());
			String[] ignorePaths = ignoreList.toArray(new String[ignoreList.size()]);
			
			http.authorizeRequests()
					.antMatchers(openPaths).permitAll()
					.antMatchers(HttpMethod.resolve(securityProperties.getLoginMethod().name()), securityProperties.getLoginPath()).permitAll()
					.antMatchers(HttpMethod.resolve(securityProperties.getLogoutMethod().name()), securityProperties.getLogoutPath()).permitAll()
				.anyRequest()
					.authenticated()
				.and()
					.exceptionHandling()
						.authenticationEntryPoint(this.getAuthenticationEntryPoint())
						.accessDeniedHandler(this.getAccessDeniedHandler())
				.and()
					.csrf()
						.ignoringAntMatchers(ignorePaths)
						.csrfTokenRepository(csrfTokenRepository())
				.and()
					.logout()
						.logoutRequestMatcher(new AntPathRequestMatcher(securityProperties.getLogoutPath(), securityProperties.getLogoutMethod().name()))
						.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
				.and()
					.addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
					.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
		} else {
	    	http.authorizeRequests()
	    		.antMatchers("/").permitAll()
	    	.and()
	    		.csrf().disable();
		}
    }
 
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(LionSecurityConst.ENCODER_STRENGTH);
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authenticationService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.afterPropertiesSet();
        return authenticationProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManager authenticationManager = new ProviderManager(Arrays.asList(authenticationProvider()));
        return authenticationManager;
    }
    
    @Bean
    public AccessDecisionManager customAccessDecisionManager() {   	
    	return new AuthoritiesDecisionManager();
    }
    
    @Bean
    public FilterInvocationSecurityMetadataSource dbFilterInvocationSecurityMetadataSource() { 
    	FilterInvocationSecurityMetadataSource filter = new SecurityFilterInvocationMetadataSource();
    	
    	return filter;
    }
    
    @Bean
    public Filter filterSecurityInterceptor() throws Exception {
    	FilterSecurityInterceptor filter = new FilterSecurityInterceptor();
    	filter.setAuthenticationManager(authenticationManager());
    	filter.setAccessDecisionManager(customAccessDecisionManager());
    	filter.setSecurityMetadataSource(dbFilterInvocationSecurityMetadataSource());
    	// filter.setSecurityMetadataSource(new DefaultFilterInvocationSecurityMetadataSource(new LinkedHashMap<>()));
    	
        return filter;
    }
    
    @Bean
    public Filter usernamePasswordAuthenticationFilter() throws Exception {
    	AbstractAuthenticationProcessingFilter filter = new AuthenticationFilter(securityProperties);
    	filter.setAuthenticationManager(authenticationManager());
    	filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(securityProperties.getLoginPath(), securityProperties.getLoginMethod().name()));
    	filter.setAuthenticationSuccessHandler(securityAuthenticationSuccessHandler);
    	filter.setAuthenticationFailureHandler(securityAuthenticationFailureHandler);
    	
        return filter;
    }
    
	@Bean
	public Filter csrfHeaderFilter() throws Exception {
	  	return new CsrfCookieFilter(securityProperties.getCsrfCookie());
	}
	
	@Bean
	public AuthenticationSuccessHandler securityAuthenticationSuccessHandler() {
		return new AuthenticationSuccessHandler();
	}
	
	@Bean
	public AuthenticationFailureHandler securityAuthenticationFailureHandler() {
		return new AuthenticationFailureHandler();
	}
  
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName(LionSecurityConst.CSRF_HEADER_NAME);
		return repository;
	}
	
	private AuthenticationEntryPoint getAuthenticationEntryPoint() {
		if (customAuthenticationEntryPoint == null) {
			return new AuthenticationEntryPoint() {
				
				@Override
				public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
					if (exceptionResolver == null) {
						response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
					} else {
						exceptionResolver.resolveException(request, response, null, authException);						
					}
				}
			};
		}
		
		return customAuthenticationEntryPoint;
	}
	
	private AccessDeniedHandler getAccessDeniedHandler() {
		if (customAccessDeniedHandler == null) {
			return new AccessDeniedHandler() {
				
				@Override
				public void handle(
					HttpServletRequest request,
					HttpServletResponse response,
					AccessDeniedException accessDeniedException
				) throws IOException, ServletException {
					throw new AccessDeniedException(accessDeniedException.getMessage(), accessDeniedException);
				}
			};
		}
		
		return customAccessDeniedHandler;
	}
}
