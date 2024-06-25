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
    private final UserService userService;

    public Flux<User> getUsers() {
        return this.userService.getAllUsers();
    }

    public Mono<User> getUser(final String email) {
        return this.userService.findByEmail(email);
    }

    public Mono<User> createUser(final User user) {
        return this.validateUser(user)
                .flatMap(userValid -> this.validateUniqueEmail(user.email())
                        .then(this.userService.save(userValid)));
    }

    private Mono<User> validateUser(final User user) {
        return Mono.fromCallable(() -> {
            user.validateUserName();
            user.validEmail();
            user.validPhone();
            return user;
        });
    }

    public Mono<Void> validateUniqueEmail(final String email) {
        return this.userService.existsByEmail(email)
                .flatMap(existEmail -> {
                    if (Boolean.FALSE.equals(existEmail)) {
                        return Mono.empty();
                    } else {
                        return Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, null, "emailExists"));
                    }
                });
    }

    public Mono<Void> updateUser(final User userWithValueNews) {
        return this.userService.findByEmail(userWithValueNews.email())
                .map(userToUpdate -> userToUpdate.withUpdatedFields(userWithValueNews))
                .flatMap(userNew -> this.userService.save(userNew).then());
    }

    public Mono<Void> delete(final String userName, final String email) {
        return this.userService.delete(userName, email);
    }
}
