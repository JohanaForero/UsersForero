package com.forero.infrastructure.entrypoint;

import com.forero.application.command.UserCommand;
import com.forero.application.query.UserQuery;
import com.forero.domain.model.User;
import com.forero.infrastructure.dto.request.UserRequestDto;
import com.forero.infrastructure.dto.response.UserResponseDto;
import com.forero.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
        return this.userQuery.execute()
                .map(this.userMapper::toDto)
                .doOnNext(userResponseDto -> log.info(LOGGER_PREFIX + "[all] Response {}", userResponseDto));
    }
}
