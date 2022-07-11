package com.dept.spring.security.demo.features.users;

import java.util.UUID;

public class User {
    public UUID id;
    public String firstName;
    public String lastName;
    public String password;
    public String email;

    public User() {
        this.id = UUID.randomUUID();
    }
}
