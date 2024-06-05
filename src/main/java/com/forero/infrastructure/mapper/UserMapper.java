package com.forero.infrastructure.mapper;

import com.forero.domain.model.User;
import com.forero.infrastructure.adapter.entity.UserEntity;
import com.forero.infrastructure.dto.request.UserRequestDto;
import com.forero.infrastructure.dto.response.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User toModel(UserRequestDto userRequestDto);

    UserResponseDto toDto(User user);

    User toModel(UserEntity userEntity);

    UserEntity toEntity(User user);
}
