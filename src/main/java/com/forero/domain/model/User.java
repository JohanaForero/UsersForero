package com.forero.domain.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record User(Long id, String name, String email, String phone, String address) {}
