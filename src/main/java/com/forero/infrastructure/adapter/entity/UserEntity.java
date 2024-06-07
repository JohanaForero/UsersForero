package com.forero.infrastructure.adapter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@AllArgsConstructor
@Data
public class UserEntity {
    @Id
    private String id;

    private String name;

    private String email;

    private String phone;

    private String address;
}
