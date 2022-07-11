package com.dept.spring.security.demo.configs;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * With custom authentication provider we can set our own custom implementation for providing the authentication data.
 * Custom authentication provider has to check if user credentials are ok. For example if we are using another service for login
 * then we would send login request to that service to check if credentails are valid. If they are we can then add user details to the authentication token.
 * This token will the populate the security principal we can use in other parts of the application.
 *
 * In other type of authentication like Form login with UserDetailsService checking of passwords is done with built-in DAOAuthenticationProvider.
 */
//@Configuration
public class CustomAuthenticationProvider {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomAuthenticationManager authenticationManager) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        httpSecurity.authenticationManager(authenticationManager);
        httpSecurity.formLogin();
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                return new UsernamePasswordAuthenticationToken("user", "user123", Collections.emptyList());
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return true;
            }
        };
    }
}
