package com.dept.spring.security.demo.features.users;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

    private final Map<UUID, User> userStore = new HashMap<>();

    @PostConstruct
    public void afterBeanCreated() {
        var user = new User();
        user.id = UUID.fromString("53fe84db-7631-4227-9651-7ea6636b26c4");
        user.firstName = "Admin";
        user.lastName = "Admin";
        user.password = "Admin123";
        userStore.put(user.id, user);
    }

    public User create(UserInvitationRequest request) {
        User user = new User();
        user.firstName = request.firstName;
        user.lastName = request.lastName;
        user.email = request.email;
        userStore.put(user.id, user);
        return user;
    }

    public Optional<User> findById(UUID userId) {
        User user = userStore.get(userId);
        return Optional.ofNullable(user);
    }

    public Collection<User> findAll() {
        return userStore.values();
    }
}
