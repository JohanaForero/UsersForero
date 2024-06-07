package com.forero.infrastructure.configuration;

import com.forero.application.command.UserCommand;
import com.forero.application.service.UserService;
import com.forero.application.usecase.UserUseCase;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

@Configuration
@RequiredArgsConstructor
public class UserBeanConfiguration {
    private final MongoDBProperties mongoDBProperties;

    @Bean
    public UserUseCase userUseCase(final UserService userService) {
        return new UserUseCase(userService);
    }

    @Bean
    public UserCommand userCommand(final UserUseCase userUseCase) {
        return new UserCommand(userUseCase);
    }

    @Bean
    public ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory() {
        return new SimpleReactiveMongoDatabaseFactory(MongoClients.create(this.mongoDBProperties.getUri()),
                this.mongoDBProperties.getDatabase());
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(this.reactiveMongoDatabaseFactory());
    }
}
