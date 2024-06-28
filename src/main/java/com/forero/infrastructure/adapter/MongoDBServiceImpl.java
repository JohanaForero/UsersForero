package com.forero.infrastructure.adapter;

import com.forero.application.exception.RepositoryException;
import com.forero.application.service.UserService;
import com.forero.domain.exception.CodeException;
import com.forero.domain.model.User;
import com.forero.infrastructure.adapter.dao.UserDao;
import com.forero.infrastructure.adapter.entity.UserEntity;
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
                .flatMap(this::saveEntity)
                .map(this.userMapper::toModel);
    }

    private Mono<UserEntity> saveEntity(final UserEntity entity) {
        return this.userDao.save(entity)
                .doFirst(() -> log.info(LOGGER_PREFIX + "[saveEntity] Request {}", entity))
                .doOnSuccess(savedEntity -> log.info(LOGGER_PREFIX + "[saveEntity] Response: {}", savedEntity))
                .onErrorResume(this::handlerError);
    }

    private Mono<UserEntity> handlerError(final Throwable error) {
        log.error(LOGGER_PREFIX + "[handlerError] Error occurred: {}", error.getMessage());
        return Mono.error(new RepositoryException(CodeException.INTERNAL_SERVER_ERROR, null));
    }

    @Override
    public Mono<Boolean> existsByEmail(final String email) {
        return Mono.just(email)
                .flatMap(this::checkEmailExistence)
                .onErrorResume(this::handleError);
    }

    private Mono<Boolean> checkEmailExistence(final String email) {
        return this.userDao.existsByEmail(email)
                .doFirst(() -> log.info(LOGGER_PREFIX + "[checkEmailExistence] Request {}", email))
                .doOnNext(result -> log.info(LOGGER_PREFIX + "[checkEmailExistence] Response: {}", result));
    }

    private Mono<Boolean> handleError(final Throwable error) {
        log.error(LOGGER_PREFIX + "[handleError] Error occurred: {}", error.getMessage());
        return Mono.error(new RepositoryException(CodeException.INTERNAL_SERVER_ERROR, null));
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
        return this.userDao.deleteByNameAndEmail(nameUser, email)
                .doFirst(() -> log.info(LOGGER_PREFIX + "[delete] Request{}", nameUser, email))
                .doOnSuccess(ignored -> log.info(LOGGER_PREFIX + "[delete] response status ok"))
                .doOnError(error -> log.error(LOGGER_PREFIX + "[delete] Error {}", email, error));
    }

    @Override
    public Mono<User> findByEmail(final String email) {
        return this.userDao.findByEmail(email)
                .doFirst(() -> log.info(LOGGER_PREFIX + "[findByEmail] Request{}", email))
                .doOnNext(userEntityResponse -> log.info(LOGGER_PREFIX + "[findByEmail] Response {}",
                        userEntityResponse))
                .doOnError(error -> log.error(LOGGER_PREFIX + "[findByEmail] Error {}", email, error))
                .map(this.userMapper::toModel);
    }
}
