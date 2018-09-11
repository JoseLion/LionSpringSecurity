package com.lionware.lionspringsecurity;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@RequestMapping(value=LionSecurityConst.LOGIN_BASE_URL)
public class LoginController {
	@RequestMapping(value=LionSecurityConst.LOGIN_USER_URL, method=RequestMethod.GET)
	public Principal user(Principal user) {
		return user;
	}
	
	@RequestMapping(value=LionSecurityConst.LOGIN_TOKEN_URL, method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> token(HttpSession session) {
		return Collections.singletonMap(LionSecurityConst.TOKEN_KEY, session.getId());
	}
}
