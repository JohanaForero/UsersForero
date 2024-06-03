package com.forero.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@RequiredArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private boolean processed;
    private Instant createdAt;
}
