package com.forero.domain.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record ErrorObjectDto(String code, String message) {
}
