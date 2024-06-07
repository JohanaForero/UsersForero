package com.forero.infrastructure.adapter;

import com.forero.application.exception.UserUseCaseException;
import com.forero.application.service.UserService;
import com.forero.domain.exception.CodeException;
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
    private static final String LOGGER_PREFIX = String.format("[%s] ", MongoDBServiceImpl.class.getSimpleName());
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<User> createUser(final User user) {
        final UserEntity userEntity = this.userMapper.toEntity(user);
        log.info(LOGGER_PREFIX + "[createUser] Request {}", user);
        return this.userRepository.save(userEntity)
                .map(this.userMapper::toModel)
                .doOnNext(savedUser -> log.info(LOGGER_PREFIX, "[createUser] Response {}", savedUser))
                .doOnError(error -> {
                    log.error(LOGGER_PREFIX + "[createUser] Error creating user", error);
                    throw new UserUseCaseException(CodeException.INVALID_PARAMETERS, null);
                })
                .doOnSuccess(success -> log.info(LOGGER_PREFIX, "[createUser] Response {}", success))
                .onErrorResume(UserUseCaseException.class, Mono::error)
                .onErrorResume(error -> Mono.error(new UserUseCaseException(CodeException.INVALID_PARAMETERS, null)));
    }

    @Override
    public Mono<Boolean> existsByEmail(final String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public Flux<User> getAllUsers() {
        log.info(LOGGER_PREFIX, "[getAllUsers] List all users ");
        return this.userRepository.findAll()
                .map(this.userMapper::toModel)
                .doOnNext(user -> log.info(LOGGER_PREFIX, "[getAllUsers] Retrieved user: {}", user));
    }

    @Override
    public Mono<User> findByEmail(final String email) {
        return this.userRepository.findByEmail(email)
                .map(this.userMapper::toModel)
                .doOnNext(user -> log.info("[findUserByEmail] User found: {}", user))
                .switchIfEmpty(Mono.error(new UserUseCaseException(CodeException.USER_NOT_FOUND, null, email)));
    }
}
