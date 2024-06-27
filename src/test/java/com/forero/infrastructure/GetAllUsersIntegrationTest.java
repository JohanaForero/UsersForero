package com.forero.infrastructure;

import com.forero.BaseIT;
import com.forero.infrastructure.adapter.entity.UserEntity;
import com.forero.infrastructure.dto.response.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GetAllUsersIntegrationTest extends BaseIT {
    private static final String BASE_PATH = "/users/all";

    @Test
    void test_getAllUsers_withRequestNullValid_shouldReturnAnStatusOKAndAllUserResponseDto() {
        //Given
        final UserEntity user1 = new UserEntity(null, "andres", "andres@gmail.com", "3147822930", "Cra 12#");
        final UserEntity user2 = new UserEntity(null, "genesis", "genesis@gmail.com", "3147822940", "Cra 14#");
        this.reactiveMongoTemplate.insert(user1).block();
        this.reactiveMongoTemplate.insert(user2).block();

        // When
        final WebTestClient.ResponseSpec response = this.webTestClient.post().uri(BASE_PATH)
                .exchange();

        // Then
        final List<UserResponseDto> responseBody = response
                .expectStatus().isOk()
                .expectBodyList(UserResponseDto.class)
                .returnResult()
                .getResponseBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        final List<String> names = responseBody.stream().map(UserResponseDto::name).toList();
        assertEquals(List.of("andres", "genesis"), names);

        final List<String> emails = responseBody.stream().map(UserResponseDto::email).toList();
        assertEquals(List.of("andres@gmail.com", "genesis@gmail.com"), emails);

        final List<String> phoneNumbers = responseBody.stream().map(UserResponseDto::phone).toList();
        assertEquals(List.of("3147822930", "3147822940"), phoneNumbers);

        final List<String> addresses = responseBody.stream().map(UserResponseDto::address).toList();
        assertEquals(List.of("Cra 12#", "Cra 14#"), addresses);
    }
}
