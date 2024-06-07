package com.forero.infrastructure.adapter;

import com.forero.application.service.UserService;
import com.forero.domain.model.User;
import com.forero.infrastructure.adapter.entity.UserEntity;
import com.forero.infrastructure.adapter.repository.UserRepository;
import com.forero.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoDBServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<User> createUser(final User user) {
        final UserEntity userEntity = this.userMapper.toEntity(user);
        return this.userRepository.save(userEntity)
                .map(this.userMapper::toModel)
                .onErrorResume(error -> Mono.error(new RuntimeException("Error creating user", error)));
    }

    @Override
    public Mono<Boolean> existsByEmail(final String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public Flux<User> getAllUsers() {
        return this.userRepository.findAll()
                .map(this.userMapper::toModel);
    }
}
