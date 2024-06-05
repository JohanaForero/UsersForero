package com.forero.application.exception;

import com.forero.domain.exception.CodeException;

public class RepositoryException extends CoreException {
    protected RepositoryException(final CodeException codeException, final Exception exception, final String... fields) {
        super(codeException, exception, fields);
    }
}
