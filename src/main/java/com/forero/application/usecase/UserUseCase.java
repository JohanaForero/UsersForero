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
                .then(this.userService.save(user));
    }

    private Mono<Void> validateUserFields(final User user) {
        return Mono.when(
                this.validateName(user),
                this.validatePhone(user),
                this.validateEmail(user),
                this.existEmail(user.email())
                        .flatMap(existsEmail -> {
                            if (Boolean.TRUE.equals(existsEmail)) {
                                log.error(LOGGER_PREFIX + "[validateEmail] email already exists");
                                return Mono.error(
                                        new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "email"));
                            }
                            return Mono.empty();
                        })
        ).then();
    }

    private Mono<Void> validateName(final User user) {
        if (!user.validateUserName()) {
            log.error(LOGGER_PREFIX + "[validateName] name user invalid");
            return Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "name"));
        }
        return Mono.empty();
    }

    private Mono<Void> validateEmail(final User user) {
        if (!user.isValidEmail()) {
            log.error(LOGGER_PREFIX + "[validateEmail] email user invalid");
            return Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "email"));
        }
        return Mono.empty();
    }

    private Mono<Void> validatePhone(final User user) {
        if (!user.isValidPhone()) {
            log.error(LOGGER_PREFIX + "[validatePhone] phone user invalid");
            return Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "phone"));
        }
        return Mono.empty();
    }

    private Mono<Boolean> existEmail(final String email) {
        return this.userService.existsByEmail(email);
    }

    public Mono<Void> updateUser(final User user) {
        return this.userService.findByEmail(user.email())
                .switchIfEmpty(Mono.error(new UserUseCaseException(CodeException.USER_NOT_FOUND, null)))
                .flatMap(existingUser -> this.withUpdatedFields(existingUser, user.address(), user.phone())
                        .flatMap(this.userService::save))
                .then();
    }

    private Mono<User> withUpdatedFields(final User existingUser, final String address, final String phone) {
        if (existingUser.isValidPhone()) {
            return Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "phone"));
        }
        final User updatedUser = existingUser.toBuilder()
                .address(address)
                .phone(phone)
                .build();

        return Mono.just(updatedUser);
    }

    public Mono<Void> delete(final String userName, final String email) {
        return this.userService.delete(userName, email);
    }
}
