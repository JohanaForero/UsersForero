package com.forero.infrastructure.adapter;

import com.forero.application.service.UserService;
import com.forero.domain.model.User;
import com.forero.infrastructure.adapter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class H2RepositoryServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Mono<User> createUser(User user) {
        return userRepository.save(user)  // Save the user in the database
                .thenReturn(user);           // Return the saved user in a Mono
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return null;
    }
}
