package com.forero.infrastructure;

import com.forero.BaseIT;
import com.forero.infrastructure.adapter.entity.UserEntity;
import com.forero.infrastructure.dto.request.UserPartialUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

class UpdateUserIntegrationTest extends BaseIT {
    private static final String BASE_PATH = "/users/userNew";

    @Test
    void test_updateUserFieldsByEmail_withRequestValid_shouldReturnStatusCodeOK() {
        //Given
        final UserEntity userEntity = new UserEntity(null, "john.doe@gmail.com",
                "John", "1000500009", "Cra 22L");
        this.reactiveMongoTemplate.save(userEntity).block();
        final UserPartialUpdateRequestDto userPartialUpdateRequestDto = UserPartialUpdateRequestDto.builder()
                .email("john.doe@gmail.com")
                .phone("0123456789")
                .address("Cra 12#")
                .build();

        // When
        final WebTestClient.ResponseSpec response = this.webTestClient.patch().uri(BASE_PATH)
                .body(BodyInserters.fromValue(userPartialUpdateRequestDto))
                .exchange();
        //Then
        response.expectStatus().isOk();
    }
}
