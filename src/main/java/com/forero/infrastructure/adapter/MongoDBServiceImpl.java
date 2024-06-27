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
                .flatMap(entity -> {
                    log.info(LOGGER_PREFIX + "[save] Saving entity to database: {}", entity);
                    return this.userDao.save(entity)
                            .doOnSuccess(savedEntity -> log.info(LOGGER_PREFIX + "[save] Entity saved to database: {}",
                                    savedEntity))
                            .onErrorResume(error -> {
                                log.error(LOGGER_PREFIX + "[createUser] Error occurred: {}", error.getMessage());
                                return Mono.error(new RepositoryException(CodeException.INTERNAL_SERVER_ERROR, null));
                            });
                })
                .map(this.userMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByEmail(final String email) {
        return Mono.just(email)
                .flatMap(e -> {
                    log.info(LOGGER_PREFIX + "[existsByEmail] Request: {}", e);
                    return this.userDao.existsByEmail(e)
                            .doOnNext(result ->
                                    log.info(LOGGER_PREFIX + "[existsByEmail] Response: {}", result));
                })
                .onErrorResume(error -> {
                    log.error(LOGGER_PREFIX + "[existsByEmail] Error occurred: {}", error.getMessage());
                    return Mono.error(new RepositoryException(CodeException.INTERNAL_SERVER_ERROR, null));
                });
    }

    @Override
    public Flux<User> getAllUsers() {
        return this.userDao.findAll()
                .doFirst(() -> log.info(LOGGER_PREFIX + "[getAllUsers] Request"))
                .doOnNext(userEntityResponse -> log.info(LOGGER_PREFIX + "[getAllUsers] Response {}",
                        userEntityResponse))
                .map(this.userMapper::toModel);
    }

    @Override
    public Mono<Void> delete(final String nameUser, final String email) {
        return Mono.defer(() -> {
            log.info(LOGGER_PREFIX + "[delete] Request {}", nameUser, email);
            return this.userDao.deleteByNameAndEmail(nameUser, email)
                    .doOnSuccess(voidResult -> log.info(LOGGER_PREFIX + "[delete] Response {}", nameUser, email))
                    .doOnError(error -> log.error(LOGGER_PREFIX + "[delete] Error occurred {}", nameUser, email,
                            error.getMessage()));
        });
    }

    @Override
    public Mono<User> findByEmail(final String email) {
        return Mono.defer(() -> {
            log.info(LOGGER_PREFIX + "[findByEmail] Request {}", email);
            return this.userDao.findByEmail(email)
                    .doOnSubscribe(subscription -> log.info(LOGGER_PREFIX + "[findByEmail] Request {}", email))
                    .doOnNext(userEntityResponse -> log.info(LOGGER_PREFIX + "[findByEmail] Response {}",
                            userEntityResponse))
                    .doOnError(error -> log.error(LOGGER_PREFIX + "[findByEmail] Error {}", email, error))
                    .map(this.userMapper::toModel);
        });
    }
}
