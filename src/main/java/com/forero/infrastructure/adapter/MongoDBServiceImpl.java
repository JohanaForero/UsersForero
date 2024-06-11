package com.forero.infrastructure.adapter;

import com.forero.application.exception.RepositoryException;
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
                .doOnSuccess(userEntityResult -> log.info(LOGGER_PREFIX, "[createUser] Response {}", userEntityResult))
                .map(this.userMapper::toModel)
                .onErrorResume(error -> {
                    log.error(LOGGER_PREFIX, "[existsByEmail] Error occurred: {}", LOGGER_PREFIX, error.getMessage());
                    return Mono.error(new RepositoryException(CodeException.INTERNAL_SERVER_ERROR, null));
                });
    }

    @Override
    public Mono<Boolean> existsByEmail(final String email) {
        log.info(LOGGER_PREFIX, "[existsByEmail] Request {}", email);
        return this.userRepository.existsByEmail(email)
                .doOnSuccess(isEmail -> log.info(LOGGER_PREFIX, "[existsByEmail] Response {}", isEmail))
                .onErrorResume(error -> {
                    log.error(LOGGER_PREFIX, "[existsByEmail] Error occurred: {}", LOGGER_PREFIX, error.getMessage());
                    return Mono.error(new RepositoryException(CodeException.INTERNAL_SERVER_ERROR, null));
                });

    }

    @Override
    public Flux<User> getAllUsers() {
        log.info(LOGGER_PREFIX, "[getAllUsers] List all users ");
        return this.userRepository.findAll()
                .flatMap(user -> Mono.just(this.userMapper.toModel(user)))
                .doOnNext(user -> log.info("{} [getAllUsers] Retrieved user: {}", LOGGER_PREFIX, user))
                .doOnError(error -> log.error("{} [getAllUsers] Error retrieving users: {}", LOGGER_PREFIX, error.getMessage()));
    }

    @Override
    public Mono<User> findByEmail(final String email) {
        log.info(LOGGER_PREFIX, "[findByEmail] Request {}", email);
        return this.userRepository.findByEmail(email)
                .map(this.userMapper::toModel)
                .doOnNext(user -> log.info(LOGGER_PREFIX, "[findByEmail] Response: {}", email))
                .doOnError(error -> log.error("[findUserByEmail] Error finding user by email: {}", email, error))
                .switchIfEmpty(Mono.error(new UserUseCaseException(CodeException.USER_NOT_FOUND, null, email)));
    }
}
