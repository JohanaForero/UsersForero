package com.forero.application.exception;

import com.forero.domain.exception.CodeException;

public class UserUseCaseException extends CoreException {
    public UserUseCaseException(final CodeException codeException, final Exception exception, final String... fields) {
        super(codeException, exception, fields);
    }
}
