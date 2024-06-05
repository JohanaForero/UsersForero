package com.forero.infrastructure.adapter.repository;

import com.forero.infrastructure.adapter.entity.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<UserEntity, Long> {
    Mono<Boolean> existsByEmail(String email);
}
