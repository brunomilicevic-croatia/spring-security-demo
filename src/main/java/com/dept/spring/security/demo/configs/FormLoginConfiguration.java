package com.dept.spring.security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * To have a built in form login and logut pages you just need to add the formLogin() to httpSecurity.
 * Check the logs when starting the app and securityFilterChain will add additional filters for Login and Logout handling:
 *  1. DefaultLoginPageGeneratingFilter
 *  2. DefaultLogoutPageGeneratingFilter
 */
//@Configuration
public class FormLoginConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth.antMatchers("/users").hasRole("ADMIN"));
        httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest()
                                                       .authenticated());
        httpSecurity.formLogin();
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        var userManager = new InMemoryUserDetailsManager();
        var user = User.withUsername("user")
                       .password(passwordEncoder.encode("user123"))
                       .roles()
                       .build();
        var admin = User.withUsername("admin")
                       .password(passwordEncoder.encode("user123"))
                       .roles("ADMIN")
                       .build();
        userManager.createUser(user);
        userManager.createUser(admin);
        return userManager;
    }
}

