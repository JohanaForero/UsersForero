package com.forero.infrastructure.configuration;

import com.forero.application.command.UserCommand;
import com.forero.application.service.UserService;
import com.forero.application.usecase.UserUseCase;
import com.forero.infrastructure.mapper.UserMapper;
import com.forero.infrastructure.mapper.UserMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
@RequiredArgsConstructor
public class UserBeanConfiguration {
    @Bean
    public UserUseCase userUseCase(final UserService userService) {
        return new UserUseCase(userService);
    }

    @Bean
    public UserCommand userCommand(final UserUseCase userUseCase) {
        return new UserCommand(userUseCase);
    }

    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();
    }

}
