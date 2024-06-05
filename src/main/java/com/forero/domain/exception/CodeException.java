package com.forero.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeException {
    INTERNAL_SERVER_ERROR("Internal server error"),
    USER_NOT_FOUND("User not found"),
    INVALID_PARAMETERS("Invalid Parameters");
    private final String messageFormat;
}
