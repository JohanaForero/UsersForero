package com.forero.domain.model;

import com.forero.domain.exception.CodeException;
import com.forero.domain.exception.UserDomainException;
import lombok.Builder;

@Builder(toBuilder = true)
public record User(String id, String name, String email, String phone, String address) {
    private static final String REGEX_CONTAINS_NUMBERS = ".*\\d.*";
    private static final String GMAIL_DOMAIN = "@gmail.com";
    private static final int PHONE_LENGTH = 10;

    public void validateUserName() {
        if (this.name != null && this.name.matches(REGEX_CONTAINS_NUMBERS)) {
            throw new UserDomainException(CodeException.INVALID_PARAMETERS, null, "name");
        }
    }

    public void validEmail() {
        if (this.email != null && !this.email.endsWith(GMAIL_DOMAIN)) {
            throw new UserDomainException(CodeException.INVALID_PARAMETERS, null, "email");
        }
    }

    public void validPhone() {
        if (this.phone != null && this.phone.length() != PHONE_LENGTH) {
            throw new UserDomainException(CodeException.INVALID_PARAMETERS, null, "phone");
        }
    }

    public User withUpdatedFields(final User userWithValueNews) {
        return this.toBuilder()
                .name(userWithValueNews.name())
                .phone(userWithValueNews.phone())
                .address(userWithValueNews.address())
                .build();
    }
}
