package com.forero.domain.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record User(String id, String name, String email, String phone, String address) {
}
