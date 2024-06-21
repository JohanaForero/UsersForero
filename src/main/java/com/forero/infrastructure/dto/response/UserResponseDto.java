package com.forero.infrastructure.dto.response;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserResponseDto(String id, String name, String email, String phone, String address) {
}