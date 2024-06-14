package com.forero.infrastructure;

import com.forero.BaseIT;
import com.forero.infrastructure.dto.request.UserRequestDto;
import com.forero.infrastructure.dto.response.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

class CreateUserIntegrationTest extends BaseIT {
    private static final String BASE_PATH = "/users";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCreateUser_withRequestValid_shouldReturnUserResponseDtoAndStatusCreated() {
        //Given
        final UserRequestDto userRequestDto = UserRequestDto.builder()
                .name("name")
                .email("test@gmail.com")
                .phone("1234567892")
                .address("Bella vista")
                .build();
        final UserResponseDto userResponseDto = UserResponseDto.builder()
                .id("")
                .name("name")
                .email("test@gmail.com")
                .phone("1234567892")
                .address("Bella vista")
                .build();

        //When
        this.webTestClient.post().uri(BASE_PATH)
                .body(userRequestDto, UserRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .isEqualTo(userResponseDto);

        //Then

    }
}
