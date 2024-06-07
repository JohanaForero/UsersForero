package com.forero.infrastructure.configuration;

import com.forero.application.command.UserCommand;
import com.forero.application.query.UserQuery;
import com.forero.application.query.UsersQuery;
import com.forero.application.service.UserService;
import com.forero.application.usecase.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
    public UsersQuery usersQuery(final UserUseCase userUseCase) {
        return new UsersQuery(userUseCase);
    }

    @Bean
    public UserQuery userQuery(final UserUseCase userUseCase) {
        return new UserQuery(userUseCase);
    }
}
