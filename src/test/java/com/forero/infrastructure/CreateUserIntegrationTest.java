package com.forero.infrastructure;

import com.forero.BaseIT;
import com.forero.infrastructure.adapter.entity.UserEntity;
import com.forero.infrastructure.dto.request.UserRequestDto;
import com.forero.infrastructure.dto.response.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

class CreateUserIntegrationTest extends BaseIT {
    private static final String BASE_PATH = "/users/register";

    public String findUserByEmail(final String email) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return this.reactiveMongoTemplate.findOne(query, UserEntity.class).block().getId();
    }

    @Test
    void testSaveUser_withRequestValid_shouldReturnUserResponseDtoAndStatusCreated() {
        final UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("john.doe@gmail.com")
                .name("John Doe")
                .phone("0123456789")
                .address("Cra 12#")
                .build();

        // When
        final WebTestClient.ResponseSpec response = this.webTestClient.post().uri(BASE_PATH)
                .body(BodyInserters.fromValue(userRequestDto))
                .exchange();

        final UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(this.findUserByEmail("john.doe@gmail.com"))
                .name("John Doe")
                .email("john.doe@gmail.com")
                .phone("0123456789")
                .address("Cra 12#")
                .build();

        response.expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .isEqualTo(userResponseDto);
    }
}
