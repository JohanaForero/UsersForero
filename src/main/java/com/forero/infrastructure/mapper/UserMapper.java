package com.forero.infrastructure.mapper;

import com.forero.domain.model.User;
import com.forero.infrastructure.adapter.entity.UserEntity;
import com.forero.infrastructure.dto.request.UserRequestDto;
import com.forero.infrastructure.dto.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toModel(UserRequestDto userRequestDto);

    UserResponseDto toDto(User user);

    default User toModel(final UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .address(userEntity.getAddress())
                .build();
    }

    UserEntity toEntity(User user);
}

