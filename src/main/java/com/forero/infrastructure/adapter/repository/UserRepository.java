package com.forero.infrastructure.adapter.repository;

import com.forero.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends JpaRepository<User, Long> {
    Mono<Boolean> existsByEmail(String email);
}
