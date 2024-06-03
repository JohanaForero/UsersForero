package com.forero.application.service;

import com.forero.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> createUser(User user);
    Mono<Boolean> existsByEmail(String email);
}
