package com.forero.application.query;

import com.forero.application.usecase.UserUseCase;
import com.forero.domain.model.User;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserQueryByEmail {
    private final UserUseCase userUseCase;

    public Mono<User> execute(final String email) {
        return this.userUseCase.getUser(email);
    }
}
