package com.lionware.lionspringsecurity;

public class LionSecurityConst {
	
	public static final String LOGIN_BASE_URL = "/api/";
	
	public static final String LOGIN_USER_URL = "user";
	
	public static final String LOGIN_TOKEN_URL = "token";
	
	public static final String TOKEN_KEY = "token";
	
	public static final String ANT_REQUEST_MATCHER_METHOD = "GET";
	
	public static final String AUTHORIZATION_HEADER = "authorization";
	
	public static final String EXTRA_HEADER = "extra";
	
	public static final String TOKEN_HEADER = "X-XSRF-TOKEN";
	
	public static final String COOKIES_TOKEN_NAME = "XSRF-TOKEN";
	
	public static final Integer ENCODER_STRENGTH = 12;
	
	public static final Integer AUTHORIZATION_HEADER_OFFSET = 6;
	
	public static final String UTF8 = "UTF8";
	
	public static final String HEADER_SPLITTER = ":";
	
	public static final String ORIGIN = "Origin";
	
	public static final String OK = "OK";
	
	public static final String HEADER_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	
	public static final String HEADER_VARY = "Vary";
	
	public static final String HEADER_MAX_AGE = "Access-Control-Max-Age";
	
	public static final String HEADER_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	
	public static final String HEADER_ALLOW_METHODS = "Access-Control-Allow-Methods";
	
	public static final String HEADER_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	
	public static final String VALUE_VARY = "Origin";
	
	public static final String VALUE_MAX_AGE = "3600";
	
	public static final String VALUE_ALLOW_CREDENTIALS = "true";
	
	public static final String VALUE_ALLOW_METHODS = "POST, GET, OPTIONS, DELETE";
	
	public static final String VALUE_ALLOW_HEADERS = "Origin, Authorization, Extra, X-Requested-With, Content-Type, Accept, X-XSRF-TOKEN, Content-Encoding";
	
	public static final String MAIL_FAILURE_MESSAGE = "No se pudo enviar el correo con la clave temporal, por favor intente nuevamente";
	
	public static final String BAD_CREDENTIALS = "Usuario y/o contraseña incorrectos";
	
	public static final String ACCESS_DENIED = "Acceso denegado";
	
	public static final String USER_LOCKED = "El usuario se encuentra bloqueado. Por favor intente más tarde";
	
	public static final String USER_DISABLED = "El usuario al que se quiere acceder fue inactivado. Comuniquese con el admministrador del sistema";
	
	public static final String USER_NOT_FOUND = "Usuario no encontrado";
	
	public static final String TEXT_UTF8_HEADER = "text/html; charset=UTF-8";
}
