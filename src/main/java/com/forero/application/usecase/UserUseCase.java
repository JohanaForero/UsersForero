package com.forero.application.usecase;

import com.forero.application.exception.UserUseCaseException;
import com.forero.application.service.UserService;
import com.forero.domain.exception.CodeException;
import com.forero.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class UserUseCase {
    private static final String LOGGER_PREFIX = String.format("[%s] ", UserUseCase.class.getSimpleName());
    private final UserService userService;

    public Mono<User> createUser(final User user) {
        this.validateUser(user);
        return this.userService.createUser(user);
    }

    private void validateUser(final User user) {
        if (!user.validateUserName()) {
            log.error(LOGGER_PREFIX + "[validateUser] name user invalid");
            throw new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "userName");
        }
        if (!user.isValidEmail()) {
            log.error(LOGGER_PREFIX + "[validateUser] email user invalid");
            throw new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "email");
        }
        if (!user.isValidPhone()) {
            log.error(LOGGER_PREFIX + "[validateUser] phone user invalid");
            throw new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "phone");
        }
    }

    public Flux<User> getAll() {
        return this.userService.getAllUsers();
    }

    public Mono<User> getUser(final String email) {
        return this.userService.findByEmail(email);
    }
}
