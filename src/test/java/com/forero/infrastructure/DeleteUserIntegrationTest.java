package com.forero.infrastructure;

import com.forero.BaseIT;
import com.forero.domain.model.ErrorObjectDto;
import com.forero.infrastructure.adapter.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

class DeleteUserIntegrationTest extends BaseIT {
    private static final String BASE_PATH = "/users/userDelete";
    private static final String REQUEST_PARAM_EMAIL = "email";
    private static final String REQUEST_PARAM_NAME = "name";

    @Test
    void test_deleteUser_withRequestValid_shouldReturnAnStatusOK() {
        final String name = "John";
        final String email = "johndoe@gmail.com";
        final UserEntity userToDelete = new UserEntity(null, "john.doe@gmail.com",
                "John", "1000500009", "Cra 22L");
        this.reactiveMongoTemplate.insert(userToDelete).block();

        final WebTestClient.ResponseSpec response = this.webTestClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .queryParam(REQUEST_PARAM_NAME, name)
                        .queryParam(REQUEST_PARAM_EMAIL, email)
                        .build())
                .exchange();

        response.expectStatus().isOk();
    }

    @Test
    void test_deleteUser_withRequestInvalid_shouldReturnCodeExceptionINVALID_PARAMETERSAndStatusBadRequest() {
        final ErrorObjectDto errorResponseExpected = ErrorObjectDto.builder()
                .code("INVALID_PARAMETERS")
                .message(String.format("Invalid %s parameters", REQUEST_PARAM_EMAIL))
                .build();

        final WebTestClient.ResponseSpec response = this.webTestClient.delete()
                .uri(BASE_PATH)
                .exchange();

        response.expectStatus()
                .isBadRequest()
                .expectBody(ErrorObjectDto.class)
                .isEqualTo(errorResponseExpected);
    }

}
