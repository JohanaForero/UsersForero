package com.forero.application.command;

import com.forero.application.usecase.UserUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserDeleteCommand {
    private final UserUseCase userUseCase;

    public Mono<Void> execute(final String userName, final String email) {
        return this.userUseCase.delete(userName, email);
    }
}
