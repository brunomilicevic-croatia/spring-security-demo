package com.dept.spring.security.demo.configs.jwt;

import com.dept.spring.security.demo.configs.jwt.JwtAuthenticationFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
public class StatelessConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth.antMatchers("/users").hasRole("ADMIN"));
        httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.formLogin();
        httpSecurity.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionAuthenticationStrategy(new JwtSessionAuthenticationStrategy());
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        httpSecurity.logout().addLogoutHandler((request, response, authentication) -> {
            var removeAuthenticationCookie = new Cookie(HttpHeaders.AUTHORIZATION, "");
            removeAuthenticationCookie.setMaxAge(0);
            response.addCookie(removeAuthenticationCookie);
        });
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
        var admin = User.withUsername("admin").password(passwordEncoder.encode("admin123")).roles("ADMIN").build();
        userManager.createUser(user);
        userManager.createUser(admin);
        return userManager;
    }
}
