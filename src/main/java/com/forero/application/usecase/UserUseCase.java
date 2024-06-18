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

    public Flux<User> getUsers() {
        return this.userService.getAllUsers();
    }

    public Mono<User> getUser(final String email) {
        return this.userService.findByEmail(email);
    }

    public Mono<User> createUser(final User user) {
        return this.validateUserFields(user)
                .then(this.validateUniqueEmail(user.email()))
                .then(Mono.fromCallable(() -> user.toBuilder().build()))
                .flatMap(this.userService::save);
    }

    public Mono<Void> validateUniqueEmail(final String email) {
        return this.userService.existsByEmail(email)
                .flatMap(existEmail -> {
                    if (Boolean.FALSE.equals(existEmail)) {
                        return Mono.empty();
                    } else {
                        return Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "email"));
                    }
                });
    }

    private Mono<Void> validateUserFields(final User user) {
        return Mono.when(
                this.validateName(user),
                this.validatePhone(user),
                this.validateEmail(user)
        ).then();
    }

    private Mono<Void> validateName(final User user) {
        return Mono.just(user)
                .flatMap(u -> {
                    if (!u.validateUserName()) {
                        return Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "name"));
                    }
                    return Mono.empty();
                })
                .doOnError(error -> log.error(LOGGER_PREFIX + "[validateName] name user invalid", error))
                .then();
    }

    private Mono<Void> validateEmail(final User user) {
        return Mono.just(user)
                .flatMap(u -> {
                    if (!u.isValidEmail()) {
                        return Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "email"));
                    }
                    return Mono.empty();
                })
                .doOnError(error -> log.error(LOGGER_PREFIX + "[validateEmail] email user invalid", error))
                .then();
    }

    private Mono<Void> validatePhone(final User user) {
        return Mono.just(user)
                .flatMap(u -> {
                    if (!u.isValidPhone()) {
                        return Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "phone"));
                    }
                    return Mono.empty();
                })
                .doOnError(error -> log.error(LOGGER_PREFIX + "[validatePhone] phone user invalid", error))
                .then();
    }

    public Mono<Void> updateUser(final User user) {
        return this.userService.findByEmail(user.email())
                .switchIfEmpty(Mono.error(new UserUseCaseException(CodeException.USER_NOT_FOUND, null)))
                .flatMap(whitExistingUser -> {
                    if (!user.isValidPhone()) {
                        return Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "phone"));
                    }
                    final User updatedUser = whitExistingUser.toBuilder()
                            .address(user.address())
                            .phone(user.phone())
                            .build();
                    return this.userService.save(updatedUser);
                })
                .then();
    }
    
    public Mono<Void> delete(final String userName, final String email) {
        return this.userService.delete(userName, email);
    }
}
