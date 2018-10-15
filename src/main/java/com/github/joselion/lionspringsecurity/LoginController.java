package com.github.joselion.lionspringsecurity;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.joselion.lionspringsecurity.core.LionSecurityConst;
import com.github.joselion.lionspringsecurity.properties.LionSecurityProperties;

@CrossOrigin
@RestController
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class LoginController {
	
	@Autowired
	private LionSecurityProperties securityProperties;
	
	@RequestMapping(value="${" + LionSecurityConst.PROPS_PREFIX + ".login-path}")
	public Principal user(Principal user) {
		return user;
	}
	
	@RequestMapping(value="${" + LionSecurityConst.PROPS_PREFIX + ".token-path}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> token(HttpSession session) {
		String token = "{\"token\": \"" + session.getId() + "\"}";
		
		if (securityProperties.getEnabled()) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			if (authentication != null && authentication.isAuthenticated()) {
				return new ResponseEntity<>(token, HttpStatus.OK);			
			}
			
			return new ResponseEntity<String>("Unauthorized access!", HttpStatus.UNAUTHORIZED);
		}
		
		return new ResponseEntity<String>(token, HttpStatus.OK);
	}
}
