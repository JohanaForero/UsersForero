package com.forero.application.usecase;

import com.forero.application.exception.UserUseCaseException;
import com.forero.application.service.UserService;
import com.forero.domain.exception.CodeException;
import com.forero.domain.model.User;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserService userService;

    public Mono<User> createUser(final User user) {
        return this.userService.existsByEmail(user.email())
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.error(new RuntimeException());
                    } else {
                        return this.userService.createUser(user);
                    }
                })
                .onErrorResume(ex -> {
                    if (!(ex instanceof UserUseCaseException)) {
                        return Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, (Exception) ex));
                    }
                    return Mono.error(ex);
                });
    }

}
