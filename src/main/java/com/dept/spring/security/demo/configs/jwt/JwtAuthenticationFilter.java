package com.dept.spring.security.demo.configs.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> authorizationCookie = findAuthorizationCookie(request);

        authorizationCookie.flatMap(cookie -> decode(cookie.getValue()))
                           .ifPresent(decodedJWT -> {
                               SecurityContextHolder.createEmptyContext();
                               SecurityContextHolder.getContext()
                                                    .setAuthentication(createAuthentication(new UserJwtPayload(decodedJWT)));
                           });

        filterChain.doFilter(request, response);
    }

    private Optional<Cookie> findAuthorizationCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                     .filter(cookie -> HttpHeaders.AUTHORIZATION.equals(cookie.getName()))
                     .findFirst();
    }

    private Authentication createAuthentication(UserJwtPayload userJwtPayload) {
        return new Authentication() {
            private boolean authenticated = true;
            private final Collection<SimpleGrantedAuthority> authorities = userJwtPayload.getGrantedAuthorities();

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return authorities;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return userJwtPayload;
            }

            @Override
            public Object getPrincipal() {
                return userJwtPayload;
            }

            @Override
            public boolean isAuthenticated() {
                return authenticated;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                this.authenticated = isAuthenticated;
            }

            @Override
            public String getName() {
                return userJwtPayload.name;
            }
        };
    }

    private Optional<DecodedJWT> decode(String jwt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret"); //use more secure key
            JWTVerifier verifier = JWT.require(algorithm)
                                      .withIssuer("security-demo")
                                      .build(); //Reusable verifier instance
            return Optional.ofNullable(verifier.verify(jwt));
        } catch (JWTVerificationException exception) {
            logger.error("Failed to decrypt the token.", exception);
            return Optional.empty();
        }
    }
}
