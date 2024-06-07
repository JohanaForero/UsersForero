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

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    UserResponseDto toDto(User user);

    default User toModel(final UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        return new User(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getPhone(),
                userEntity.getAddress()
        );
    }

    default UserEntity toEntity(final User user) {
        if (user == null) {
            return null;
        }

        return new UserEntity(
                user.id(),
                user.name(),
                user.email(),
                user.phone(),
                user.address());
    }
}

