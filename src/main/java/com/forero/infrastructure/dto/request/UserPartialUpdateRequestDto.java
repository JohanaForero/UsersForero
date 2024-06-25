package com.forero.infrastructure.dto.request;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserPartialUpdateRequestDto(
        String email,
        String phone,
        String address
) {
}
