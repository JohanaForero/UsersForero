package com.forero.infrastructure.dto.request;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserRequestDto(String id, String name, String email, String phone, String address) {
}

