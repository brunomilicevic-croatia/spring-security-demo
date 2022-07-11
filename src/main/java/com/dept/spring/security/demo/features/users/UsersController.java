package com.dept.spring.security.demo.features.users;

import static java.lang.String.format;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("users")
@RestController
public class UsersController {

    private UserRepository userRepo;

    public UsersController(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    @PostMapping
    public ResponseEntity<Void> inviteUser(@RequestBody UserInvitationRequest request) throws URISyntaxException {
        User newUser = userRepo.create(request);
        return ResponseEntity.created(generateUserLocation(newUser))
                             .build();
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAll() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") UUID userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userOptional.get());
    }

    private URI generateUserLocation(User newUser) throws URISyntaxException {
        return new URI(format("/users/%s", newUser.id));
    }
}
