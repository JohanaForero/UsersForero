package com.forero.infrastructure.entrypoint;

import com.forero.application.command.UserCommand;
import com.forero.application.command.UserDeleteCommand;
import com.forero.application.command.UserPartialUpdateCommand;
import com.forero.application.query.UserQueryByEmail;
import com.forero.application.query.UsersQuery;
import com.forero.domain.model.User;
import com.forero.infrastructure.dto.request.UserPartialUpdateRequestDto;
import com.forero.infrastructure.dto.request.UserRequestDto;
import com.forero.infrastructure.dto.response.UserResponseDto;
import com.forero.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final UserQueryByEmail userQueryByEmail;
    private final UserDeleteCommand userDeleteCommand;
    private final UserPartialUpdateCommand userPartialUpdateCommand;

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
        return this.userQueryByEmail.execute(email)
                .map(this.userMapper::toDto)
                .doOnNext(userResponseDto -> log.info(LOGGER_PREFIX + "[all] Response {}", userResponseDto));
    }

    @PostMapping("/")
    public Mono<Void> updateUser(@RequestBody final UserPartialUpdateRequestDto userPartialUpdateDto) {
        return this.userPartialUpdateCommand.execute(this.userMapper.toModel(userPartialUpdateDto))
                .doFirst(() -> log.info(LOGGER_PREFIX + "[updateUser] request {}", userPartialUpdateDto))
                .doOnSuccess(voidFlow ->
                        log.info(LOGGER_PREFIX + "[updateUser] response void"));
    }

    @DeleteMapping
    public Mono<Void> deleteUser(@RequestParam(value = "name") final String userName,
                                 @RequestParam(value = "email") final String email) {
        return this.userDeleteCommand.execute(userName, email)
                .doFirst(() -> log.info(LOGGER_PREFIX + "[deleteUser] request {}, {}", userName, email))
                .doOnSuccess(voidFlow ->
                        log.info(LOGGER_PREFIX + "[deleteUser] response void"));
    }
}
