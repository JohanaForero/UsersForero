package com.forero.domain.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record User(String id, String name, String email, String phone, String address) {
    private static final String REGEX_CONTAINS_NUMBERS = ".*\\d.*";
    private static final String GMAIL_DOMAIN = "@gmail.com";
    private static final int PHONE_LENGTH = 10;

    public boolean validateUserName() {
        return this.name != null && !this.name.matches(REGEX_CONTAINS_NUMBERS);
    }

    public boolean isValidEmail() {
        return this.email != null && this.email.endsWith(GMAIL_DOMAIN);
    }

    public boolean isValidPhone() {
        return this.phone != null && this.phone.length() == PHONE_LENGTH;
    }
}
