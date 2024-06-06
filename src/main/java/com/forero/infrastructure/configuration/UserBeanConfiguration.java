package com.forero.infrastructure.configuration;

import com.forero.application.command.UserCommand;
import com.forero.application.service.UserService;
import com.forero.application.usecase.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@RequiredArgsConstructor
@EnableR2dbcRepositories(basePackages = "com.forero.infrastructure.adapter.repository")
public class UserBeanConfiguration {
    @Bean
    public UserUseCase userUseCase(final UserService userService) {
        return new UserUseCase(userService);
    }

    @Bean
    public UserCommand userCommand(final UserUseCase userUseCase) {
        return new UserCommand(userUseCase);
    }
}
