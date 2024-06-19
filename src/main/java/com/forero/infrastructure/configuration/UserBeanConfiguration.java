package com.forero.infrastructure.configuration;

import com.forero.application.command.CreateUserCommand;
import com.forero.application.command.DeleteUserCommand;
import com.forero.application.command.UpdateUserCommand;
import com.forero.application.query.GetUserQuery;
import com.forero.application.query.GetUsersQuery;
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
    public CreateUserCommand userCommand(final UserUseCase userUseCase) {
        return new CreateUserCommand(userUseCase);
    }

    @Bean
    public DeleteUserCommand userDeleteCommand(final UserUseCase userUseCase) {
        return new DeleteUserCommand(userUseCase);
    }

    @Bean
    public UpdateUserCommand userPartialUpdateCommand(final UserUseCase userUseCase) {
        return new UpdateUserCommand(userUseCase);
    }

    @Bean
    public GetUsersQuery usersQuery(final UserUseCase userUseCase) {
        return new GetUsersQuery(userUseCase);
    }

    @Bean
    public GetUserQuery userQueryByEmail(final UserUseCase userUseCase) {
        return new GetUserQuery(userUseCase);
    }
}
