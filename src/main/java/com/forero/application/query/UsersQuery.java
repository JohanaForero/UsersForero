package com.forero.application.query;

import com.forero.application.usecase.UserUseCase;
import com.forero.domain.model.User;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class UsersQuery {
    private final UserUseCase userUseCase;

    public Flux<User> execute() {
        return this.userUseCase.getAll();
    }
}
