package com.forero.infrastructure.adapter;

import com.forero.application.exception.RepositoryException;
import com.forero.application.service.UserService;
import com.forero.domain.exception.CodeException;
import com.forero.domain.model.User;
import com.forero.infrastructure.adapter.dao.UserDao;
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
    private final UserDao userDao;
    private final UserMapper userMapper;

    @Override
    public Mono<User> save(final User user) {
        return Mono.just(user)
                .map(this.userMapper::toEntity)
                .doFirst(() -> log.info(LOGGER_PREFIX + "[createUser] Request {}", user))
                .flatMap(this.userDao::save)
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
        return this.userDao.existsByEmail(email)
                .doOnSuccess(isEmail -> log.info(LOGGER_PREFIX, "[existsByEmail] Response {}", isEmail))
                .onErrorResume(error -> {
                    log.error(LOGGER_PREFIX, "[existsByEmail] Error occurred: {}", error.getMessage());
                    return Mono.error(new RepositoryException(CodeException.INTERNAL_SERVER_ERROR, null));
                });
    }

    @Override
    public Flux<User> getAllUsers() {
        return this.userDao.findAll()
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
        return this.userDao.deleteByNameAndEmail(nameUser, email)
                .doFirst(() -> log.info(LOGGER_PREFIX + "[deleteUser] request {}, {}", nameUser, email))
                .doOnSuccess(voidFlow -> log.info(LOGGER_PREFIX + "[deleteUser] response void"));
    }

    @Override
    public Mono<User> findByEmail(final String email) {
        return this.userDao.findByEmail(email)
                .doFirst(() -> log.info(LOGGER_PREFIX, "[findByEmail] Request {}", email))
                .doOnSuccess(voidFlow -> log.info(LOGGER_PREFIX + "[deleteUser] response void"))
                .map(this.userMapper::toModel)
                .doOnError(error -> log.error("[findUserByEmail] Error finding user by email: {}", email, error))
                .switchIfEmpty(Mono.error(new RepositoryException(CodeException.USER_NOT_FOUND, null, email)));
    }
}
