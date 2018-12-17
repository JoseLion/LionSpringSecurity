# LionSpringSecurity
This library is a wrapper for Spring Security used with Spring Boot, meaning it uses `org.springframework.boot:spring-boot-starter-security:SPRING_BOOT_VERSION`. This warpper is meant to work just by passing the security configuration through the `application.properties` or the `application.yml` file. the list of properties and it defaults is listed bellow.

## Installation
The library can be found on Maven Central https://mvnrepository.com/artifact/com.github.joselion/lion-spring-security

### Maven
```
<!-- https://mvnrepository.com/artifact/com.github.joselion/lion-spring-security -->
<dependency>
    <groupId>com.github.joselion</groupId>
    <artifactId>lion-spring-security</artifactId>
    <version>1.2.3</version>
</dependency>
```

### Gradle
```
// https://mvnrepository.com/artifact/com.github.joselion/lion-spring-security
compile group: 'com.github.joselion', name: 'lion-spring-security', version: '1.2.3'
```

### SBT
```
// https://mvnrepository.com/artifact/com.github.joselion/lion-spring-security
libraryDependencies += "com.github.joselion" % "lion-spring-security" % "1.2.3"
```

### IVY
```
<!-- https://mvnrepository.com/artifact/com.github.joselion/lion-spring-security -->
<dependency org="com.github.joselion" name="lion-spring-security" rev="1.2.3"/>
```

### Grape
```
// https://mvnrepository.com/artifact/com.github.joselion/lion-spring-security
@Grapes(
    @Grab(group='com.github.joselion', module='lion-spring-security', version='1.2.3')
)
```

### Leiningen
```
;; https://mvnrepository.com/artifact/com.github.joselion/lion-spring-security
[com.github.joselion/lion-spring-security "1.2.3"]
```

### Buildr
```
# https://mvnrepository.com/artifact/com.github.joselion/lion-spring-security
'com.github.joselion:lion-spring-security:jar:1.2.3'
```

## Usage
As the Spring Application needs to know where to find the configurations, is very important to add `com.github.joselion.lionspringsecurity` to the base scaned packages ib the `@SpringBootApplication` annotation of the main class of the application:
```java
package com.github.my.awesome.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
  scanBasePackages={
    "com.github.my.awesome.app",
    "com.github.joselion.lionspringsecurity"
  }
)
public class MyAwesomeAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(MyAwesomeAppApplication.class, args);
  }

}
```

Then, simply add the dependency to your project and configure as desired with the properties listed bellow. The properties values shown bellow are the default values, so not all the properties need to be added in order to have full features available. The example is over an `application.yml`, but feel free to use `application.properties` if you prefer to.

### Properties (defaults)
```yml
lion:
  security:
    access-control:
      allow-credentials: true
      allow-headers:
      - Accept
      - Authorization
      - Content-Encoding
      - Content-Type
      - Origin
      - X-CSRF-Token
      - X-Requested-With
      allow-methods:
      - delete
      - get
      - options
      - post
      - put
      expose-headers:
      - Content-Encoding
      - Content-Type
      - X-CSRF-Token
      max-age: 3600
    account-entity:
      account-expired: account_expired
      attempts: attempts
      credentials-expired: credentials_expired
      id: id
      is-enabled: is_enabled
      is-locked: is_locked
      last-attempt: last_attempt
      lock-date: lock_date
      password: password
      roles: roles
      table-name: account
      username: username
    allowed-origins:
    bearer: Bearer
    csrf-cookie: CSRF-TOKEN
    enable-lock: true
    enabled: true
    lock-time: 1800000
    login-method: get
    login-path: /login
    logout-method: post
    logout-path: /logout
    max-attempts: 5
    open-paths:
    - /open/**
    token-path: /token
```

### Description
Each property has a javadoc description which should be helpful when you're setting up your security, but also each property is described in the table bellow, where every property has the `lion.security.` prefix:

| Property                             | Default value                                                                                  | Description                                                                  |
| ------------------------------------ | ---------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------- |
| `access-control`                     | N/A                                                                                            | Properties for all access control headers                                    |
| `access-control.allow-credentials`   | true                                                                                           | Value for "Access-Control-Allow-Credentials" header                          |
| `access-control.allow-headers`       | Accept, Authorization, Content-Encoding, Content-Type, Origin, X-CSRF-Token, X-Requested-With  | Value for "Access-Control-Allow-Headers" header                              |
| `access-control.allow-methods`       | delete, get, options, post, put                                                                | Value for "Access-Control-Allow-Methods" header                              |
| `access-control.expose-headers`      | Content-Encoding, Content-Type, X-CSRF-Token                                                   | Value for "Access-Control-Expose-Headers" header                             |
| `access-control.max-age`             | 3600                                                                                           | Value for "Access-Control-Max-Age" header                                    |
| `account-entity`                     | N/A                                                                                            | Properties for the account entity in the database                            |
| `account-entity.account-expired`     | account_expired                                                                                | (Optional) The name of the accountExpired column on the account entity table. This column will be used to determine if the account has expired. It's expected to be of type "boolean"                                                                                                                |
| `account-entity.attempts`            | attempts                                                                                       | (Optional)The name of the attempts column on the account entity table. This column will be used to determine the number of attempts on authenticate that each account has. It's expected to be of type "number"                                                                                    |
| `account-entity.credentials-expired` | account_expired                                                                                | (Optional) The name of the accountExpired column on the account entity table. This column will be used to determine if the account has expired. It's expected to be of type "boolean"                                                                                                                |
| `account-entity.id`                  | id                                                                                             | (Required) The name of the id column on the account entity table. This column will be used as the primary key column. Used as surrogate PK so it's expected to be of type "boolean". It should have "unique" and "not null" constraints                                                              |
| `account-entity.is-enabled`          | is_enabled                                                                                     | (Optional) The name of the isEnabled column on the account entity table. This column will be used to determine if the account is enabled. It's expected to be of type "boolean"                                                                                                                      |
| `account-entity.is-locked`           | is_locked                                                                                      | (Optional) The name of the isLocked column on the account entity table. This column will be used to determine if the account has been temporary locked by reaching the number of failed authentication attempts. It's expected to be of type "boolean"                                              |
| `account-entity.last-attempt`        | last_attempt                                                                                   | (Optional) The name of the lastAttempt column on the account entity table. This column will be used to determine the date and time of the latest authentication attempt. It's expected to be of type "timestamp"                                                                                  |
| `account-entity.lock-date`           | lock_date                                                                                      | (Optional) The name of the lockDate column on the account entity table. This column will be used to determine the date and time of when an account was locked. It's expected to be of type "timestamp"                                                                                              |
| `account-entity.password`            | password                                                                                       | (Required) The name of the password column on the account entity table. This column will be used to store the encrypted account password credential. It's expected to be of type "text"                                                                                                             |
| `account-entity.roles`               | roles                                                                                          | (Optional) The name of the roles column on the account entity table. This column will be used to store the account roles. It's expected to be of type "text[]"                                                                                                                                   |
| `account-entity.table-name`          | account                                                                                        | (Required) The name of the account entity table on your database             |
| `account-entity.username`            | username                                                                                       | (Required) The name of the username column on the account entity table. This column will be used to store the account username credential. It's expected to be of type "text"                                                                                                                       |
| `allowed-origins`                    |                                                                                                | List of allowed origins which can access the end-points. Setting this property enables CORS feature to the security, removing it disabled CORS so all origins are allowed                                                                                                                    |
| `bearer`                             | Bearer                                                                                         | The value to be used for the bearer of the "Authorization" header            |
| `csrf-cookie`                        | CSRF-TOKEN                                                                                     | The name of the cookie where the CSRF token will be stored                   |
| `enable-lock`                        | true                                                                                           | Enables or disables account locking for reaching the number of failed authentication attempts                                                                                                                                                                                                |
| `enabled`                            | true                                                                                           | Enables or disables spring security for all paths (with the "openPaths" exception)                                                                                                                                                                                                             |
| `lock-time`                          | 1800000                                                                                        | The time in milliseconds for which a user is locked if the account lock feature is enabled                                                                                                                                                                                                     |
| `login-method`                       | get                                                                                            | The HTTP request method for the login end-point                              |
| `login-path`                         | /login                                                                                         | The routing path for the login end-point                                     |
| `logout-method`                      | post                                                                                           | The HTTP request method for the logout end-point                             |
| `logout-path`                        | /logout                                                                                        | The routing path for the logout end-point                                    |
| `max-attempts`                       | 5                                                                                              | The maximum number of failed authentication attempts before the account gets locked if the account lock feature is enabled                                                                                                                                                                          |
| `open-paths`                         | /open/**                                                                                       | List of path matches that will ignore authentication and can be openly accessed                                                                                                                                                                                                               |
| `token-path`                         | /token                                                                                         | The routing path the session token end-point. This end-point can be used to retrieve the session token of an authenticated account                                                                                                                                                                 |

### Handlers
There two handlers that may be implemented to control events: `LionSecurityAfterSuccessHandler` and `LionSecurityAfterFailureHandler`. Both interfaces should be implemeted as beans and as they both are `@FunctionalInterface`, the can be uses with lambda functions as the expample bellow:

```java
@Bean
public LionSecurityAfterSuccessHandler afterSuccessHandler() {
	return (Authentication authentication) -> {
		// Your success handler code here
	};
}
```

```java
@Bean
public LionSecurityAfterFailureHandler afterFailureHandler() {
	return (AuthenticationException authenticationException) -> {
		// Your failure handler code here
	};
}
```

### Custom exception handling
Authentication exception handling can be customized by adding bean configurations for the intefaces `AuthenticationEntryPoint` and `AccessDeniedHandler`. Just create you own implementation and add a bean to your configuration:

```java
@Configuration
public class MyAwesomeAppConfiguration {

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return new myCustomAuthenticationEntryPoint();
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new myCustomAccessDeniedHandler();
  }
}
```

## Contributions
Any contribution to the project is welcome, please you can create an issue and then open a pull request so the changes may be reviewed and merged for the next release.

Thanks for using this library!
