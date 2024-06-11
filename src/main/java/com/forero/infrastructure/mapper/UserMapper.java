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

    User toModel(UserEntity userEntity);

    UserResponseDto toDto(User user);

    UserEntity toEntity(User user);
}

