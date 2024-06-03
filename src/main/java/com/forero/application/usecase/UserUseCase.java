package com.forero.application.usecase;

import com.forero.application.service.UserService;
import com.forero.domain.model.User;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserService userService;

    public Mono<User> createUser(final User user) {
        return userService.existsByEmail(user.getEmail())
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.error(new RuntimeException("Email already exists"));
                    } else {
                        return userService.createUser(user)
                                .map(this::postProcessUser);
                    }
                });
    }

    private User postProcessUser(final User user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(Instant.now());
        }
        user.setProcessed(true);

        return user;
    }
}
