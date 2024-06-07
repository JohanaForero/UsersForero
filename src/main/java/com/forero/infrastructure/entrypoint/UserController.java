package com.forero.infrastructure.entrypoint;

import com.forero.application.command.UserCommand;
import com.forero.application.exception.UserUseCaseException;
import com.forero.application.query.UserQuery;
import com.forero.application.query.UsersQuery;
import com.forero.domain.exception.CodeException;
import com.forero.domain.model.User;
import com.forero.infrastructure.dto.request.UserRequestDto;
import com.forero.infrastructure.dto.response.UserResponseDto;
import com.forero.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private static final String LOGGER_PREFIX = String.format("[%s] ", UserController.class.getSimpleName());
    private final UserCommand userCommand;
    private final UserMapper userMapper;
    private final UsersQuery usersQuery;
    private final UserQuery userQuery;

    @PostMapping("/register")
    public Mono<UserResponseDto> createUser(@RequestBody final UserRequestDto userRequestDto) {
        log.info(LOGGER_PREFIX + "[createUser] Request {}", userRequestDto);
        final User user = this.userMapper.toModel(userRequestDto);
        return this.userCommand.execute(user)
                .map(this.userMapper::toDto)
                .doOnNext(userResponseDto -> log.info(LOGGER_PREFIX + "[createUser] Response {}", userResponseDto));
    }

    @GetMapping("/all")
    public Flux<UserResponseDto> getAllUsers() {
        log.info(LOGGER_PREFIX + "[all] Users list request ");
        return this.usersQuery.execute()
                .map(this.userMapper::toDto)
                .doOnNext(userResponseDto -> log.info(LOGGER_PREFIX + "[all] Response {}", userResponseDto));
    }

    @GetMapping("/{email}")
    public Mono<UserResponseDto> getUser(@PathVariable final String email) {
        log.info(LOGGER_PREFIX + "[getUser] Request {}", email);
        return this.userQuery.execute(email)
                .map(this.userMapper::toDto)
                .doOnNext(userResponseDto -> log.info(LOGGER_PREFIX + "[all] Response {}", userResponseDto))
                .doOnError(error -> log.error(LOGGER_PREFIX + "[findUserById] Error finding user by ID", error))
                .switchIfEmpty(Mono.error(new UserUseCaseException(CodeException.USER_NOT_FOUND, null, email)));
    }
}
