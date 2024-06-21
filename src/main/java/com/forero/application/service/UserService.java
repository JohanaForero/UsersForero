package com.forero.application.service;

import com.forero.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> save(User user);

    Mono<Boolean> existsByEmail(String email);

    Flux<User> getAllUsers();

    Mono<Void> delete(final String userName, final String email);

    Mono<User> findByEmail(String email);
}
