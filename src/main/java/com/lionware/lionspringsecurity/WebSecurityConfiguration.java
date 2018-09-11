package com.lionware.lionspringsecurity;

import java.util.Arrays;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${lion-security.enabled}")
	private Boolean securityEnabled;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private SecurityAuthenticationFailureHandler securityAuthenticationFailureHandler;
	
	@Autowired
	private SecurityAuthenticationSuccessHandler securityAuthenticationSuccessHandler;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		if(securityEnabled) {
			http
				.authorizeRequests()
					.antMatchers(HttpMethod.GET, "/open/**").permitAll()
					.antMatchers(HttpMethod.POST, "/open/**").permitAll()
					.antMatchers(HttpMethod.GET, "/login/**").permitAll()
					.antMatchers(HttpMethod.POST, "/login/**").permitAll()
					.antMatchers(HttpMethod.POST, "/logout/**").permitAll()
				.anyRequest().authenticated()
				.and().csrf()
				.ignoringAntMatchers("/open/**", "/login/**", "/logout/**")
				.csrfTokenRepository(csrfTokenRepository())
				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
				.and()
				.addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
		} else {
	    	http.csrf().disable();
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
    	return new SecurityAccessDecisionManager();
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
    	
        return filter;
    }
    
    @Bean
    public Filter usernamePasswordAuthenticationFilter() throws Exception {
    	AbstractAuthenticationProcessingFilter filter = new AuthenticationFilter();
    	filter.setAuthenticationManager(authenticationManager());
    	filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LionSecurityConst.LOGIN_BASE_URL + LionSecurityConst.LOGIN_USER_URL, LionSecurityConst.ANT_REQUEST_MATCHER_METHOD));
    	filter.setAuthenticationSuccessHandler(securityAuthenticationSuccessHandler);
    	filter.setAuthenticationFailureHandler(securityAuthenticationFailureHandler);
    	
        return filter;
    }
    
	@Bean
	public Filter csrfHeaderFilter() throws Exception {
	  	return new CsrfHeaderFilter();
	}
  
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName(LionSecurityConst.TOKEN_HEADER);
		return repository;
	}
}
