package com.forero.infrastructure.configuration;

import com.forero.application.command.UserCommand;
import com.forero.application.command.UserDeleteCommand;
import com.forero.application.command.UserPartialUpdateCommand;
import com.forero.application.query.UserQueryByEmail;
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
    public UserDeleteCommand userDeleteCommand(final UserUseCase userUseCase) {
        return new UserDeleteCommand(userUseCase);
    }

    @Bean
    public UserPartialUpdateCommand userPartialUpdateCommand(final UserUseCase userUseCase) {
        return new UserPartialUpdateCommand(userUseCase);
    }

    @Bean
    public UsersQuery usersQuery(final UserUseCase userUseCase) {
        return new UsersQuery(userUseCase);
    }

    @Bean
    public UserQueryByEmail userQueryByEmail(final UserUseCase userUseCase) {
        return new UserQueryByEmail(userUseCase);
    }
}
