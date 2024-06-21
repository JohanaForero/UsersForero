package com.forero.application.command;

import com.forero.application.usecase.UserUseCase;
import com.forero.domain.model.User;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateUserCommand {
    private final UserUseCase userUseCase;

    public Mono<User> execute(final User user) {
        return this.userUseCase.createUser(user);
    }
}
