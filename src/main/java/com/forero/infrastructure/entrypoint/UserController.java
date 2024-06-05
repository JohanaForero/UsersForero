package com.forero.infrastructure.entrypoint;

import com.forero.application.command.UserCommand;
import com.forero.domain.model.User;
import com.forero.infrastructure.dto.request.UserRequestDto;
import com.forero.infrastructure.dto.response.UserResponseDto;
import com.forero.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private static final String LOGGER_PREFIX = String.format("[%s] ", UserController.class.getSimpleName());
    private UserCommand userCommand;
    private UserMapper userMapper;

    @PostMapping("/register")
    public Mono<UserResponseDto> createUser(@RequestBody final UserRequestDto userRequestDto) {
        log.info(LOGGER_PREFIX + "[createUser] Request {}", userRequestDto);
        final User user = this.userMapper.toModel(userRequestDto);
        return this.userCommand.execute(user)
                .map(this.userMapper::toDto)
                .doOnNext(userResponseDto -> log.info(LOGGER_PREFIX + "[createUser] Response {}", userResponseDto));
    }
}
