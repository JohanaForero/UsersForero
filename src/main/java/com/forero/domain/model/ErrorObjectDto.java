package com.forero.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorObjectDto {
    private String code;
    private String message;

    public void message(final String message) {
        this.message = message;
    }

    public void code(final String code) {
        this.code = code;
    }
}
