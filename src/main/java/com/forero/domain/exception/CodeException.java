package com.forero.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeException {
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    USER_NOT_FOUND("User not found"),
    DB_INTERNAL("Unavailable DB service"),
    INVALID_PARAMETERS("Invalid %s Parameters");
    private final String messageFormat;
}
