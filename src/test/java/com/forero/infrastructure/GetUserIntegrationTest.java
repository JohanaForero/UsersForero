package com.forero.infrastructure;

import com.forero.BaseIT;
import com.forero.infrastructure.adapter.entity.UserEntity;
import com.forero.infrastructure.dto.response.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

class GetUserIntegrationTest extends BaseIT {
    private static final String BASE_PATH = "/users/email";
    private static final String EMAIL = "email";

    @Test
    void test_getUser_withRequestNullValid_shouldReturnAnStatusOKAndAllUserResponseDto() {
        //Given
        final String email = "andres@gmail.com";
        final UserEntity user1 = new UserEntity(null, "andres", "andres@gmail.com", "3147822930", "Cra 12#");
        this.reactiveMongoTemplate.save(user1).block();

        final UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(this.findUserByEmail("andres@gmail.com"))
                .name("andres")
                .email("andres@gmail.com")
                .phone("3147822930")
                .address("Cra 12#")
                .build();

        // When
        final WebTestClient.ResponseSpec response = this.webTestClient.post().uri(BASE_PATH)
                .body(BodyInserters.fromValue(email))
                .exchange();

        // Then
        response
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .isEqualTo(userResponseDto);
    }
}
