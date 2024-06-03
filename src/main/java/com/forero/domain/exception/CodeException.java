package com.forero.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeException {
    INTERNAL_SERVER_ERROR("Internal server error"),
    CUSTOMER_NOT_FOUND("Customer not found");

    private final String messageFormat;
}
