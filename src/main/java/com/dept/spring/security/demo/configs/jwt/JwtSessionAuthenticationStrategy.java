package com.dept.spring.security.demo.configs.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

public class JwtSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
        var jwtCookie = new Cookie(HttpHeaders.AUTHORIZATION, jwtFromAuthentication(authentication));
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(-1);
        response.addCookie(jwtCookie);
    }

    private String jwtFromAuthentication(Authentication authentication) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            return JWT.create()
                      .withIssuer("security-demo")
                      .withClaim("username", authentication.getName())
                      .withClaim("roles", authentication.getAuthorities()
                                                        .stream()
                                                        .map(GrantedAuthority::getAuthority)
                                                        .collect(Collectors.toList()))
                      .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new SessionAuthenticationException(exception.getMessage());
        }
    }
}
