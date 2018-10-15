package com.lionware.lionspringsecurity.properties;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@EnableConfigurationProperties(LionSecurityProperties.class)
public class PropertiesConfiguration {

}
