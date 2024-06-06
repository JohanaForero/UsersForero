package com.forero.infrastructure.adapter.repository;

import com.forero.infrastructure.adapter.entity.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserEntity, String> {
    Mono<Boolean> existsByEmail(String email);
}
