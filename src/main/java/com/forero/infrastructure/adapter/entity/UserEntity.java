package com.forero.infrastructure.adapter.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "users")
public class UserEntity {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String address;
}
