package com.forero.infrastructure.adapter;

import com.forero.application.exception.RepositoryException;
import com.forero.application.exception.UserUseCaseException;
import com.forero.application.service.UserService;
import com.forero.domain.exception.CodeException;
import com.forero.domain.model.User;
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
    public Mono<User> save(final User user) {
        return Mono.just(user)
                .doFirst(() -> log.info(LOGGER_PREFIX + "[createUser] Request {}", user))
                .map(this.userMapper::toEntity)
                .flatMap(this.userRepository::save)
                .doOnSuccess(userEntityResult -> log.info(LOGGER_PREFIX + "[createUser] Response {}", userEntityResult))
                .map(this.userMapper::toModel)
                .onErrorResume(error -> {
                    log.error(LOGGER_PREFIX + "[createUser] Error occurred: {}", error.getMessage());
                    return Mono.error(new RepositoryException(CodeException.INTERNAL_SERVER_ERROR, null));
                });
    }

    @Override
    public Mono<Boolean> existsByEmail(final String email) {
        log.info(LOGGER_PREFIX, "[existsByEmail] Request {}", email);
        return this.userRepository.existsByEmail(email)
                .doOnSuccess(isEmail -> log.info(LOGGER_PREFIX, "[existsByEmail] Response {}", isEmail))
                .onErrorResume(error -> {
                    log.error(LOGGER_PREFIX, "[existsByEmail] Error occurred: {}", error.getMessage());
                    return Mono.error(new RepositoryException(CodeException.INTERNAL_SERVER_ERROR, null));
                });
    }

    @Override
    public Flux<User> getAllUsers() {
        return this.userRepository.findAll()
                .doFirst(() -> log.info(LOGGER_PREFIX + "[getAllUsers] List {}"))
                .map(this.userMapper::toModel)
                .doOnSubscribe(subscription -> log.info(LOGGER_PREFIX + "[getAllUsers] Subscription started"))
                .doOnNext(user -> log.info(LOGGER_PREFIX + "[getAllUsers] Retrieved user: {}", user))
                .doOnError(error -> log.error(LOGGER_PREFIX + "[getAllUsers] Error retrieving users: {}",
                        error.getMessage()))
                .doOnComplete(() -> log.info(LOGGER_PREFIX + "[getAllUsers] Completed listing users"));

    }

    @Override
    public Mono<Void> delete(final String nameUser, final String email) {
        return this.userRepository.deleteByNameAndEmail(nameUser, email)
                .doFirst(() -> log.info(LOGGER_PREFIX + "[deleteUser] request {}, {}", nameUser, email))
                .doOnSuccess(voidFlow -> log.info(LOGGER_PREFIX + "[deleteUser] response void"));
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
