package com.dept.spring.security.demo.configs.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserJwtPayload {

    public String name;
    public List<String> roles;

    public Collection<SimpleGrantedAuthority> getGrantedAuthorities() {
        return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }

    public UserJwtPayload(DecodedJWT decodedJWT) {
        this.name = decodedJWT.getClaim("name").asString();
        this.roles = decodedJWT.getClaim("roles").asList(String.class);
    }
}
