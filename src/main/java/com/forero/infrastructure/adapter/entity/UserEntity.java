package com.forero.infrastructure.adapter.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;

    private String name;

    private String email;

    private String phone;

    private String address;
}
