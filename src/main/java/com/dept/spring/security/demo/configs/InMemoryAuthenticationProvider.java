package com.dept.spring.security.demo.configs;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


/**
 * With passwordEncoder and userDetailsService we are hooking into the spring security and adding our own source of userDetails.
 * Generally our users are in our database that we have access to, so in UserDetailsService we need to load that user from the database and return it.
 * In this example we are using InMemoryUserDetailsManager, that stores user in the memory we can see that InMemoryUserDetailsManager implements UserDetailsService.
 *
 * To ENABLE this configuration just uncomment the @Configuration annotation so that Spring can pick it up.
 */
// @Configuration
public class InMemoryAuthenticationProvider {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth.antMatchers("/csrf-token").permitAll());
        httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        httpSecurity.httpBasic(withDefaults());
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        var userManager = new InMemoryUserDetailsManager();
        var user = User.withUsername("user").password(passwordEncoder.encode("user123")).roles().build();
        userManager.createUser(user);
        return userManager;
    }


}
