package com.forero.application.usecase;

import com.forero.application.service.UserService;
import com.forero.domain.model.User;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserService userService;

    public Mono<User> createUser(final User user) {
        return this.userService.createUser(user);
    }
}
