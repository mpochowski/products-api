package com.godaddy.uk.productsapi.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends ApiException {

    private static final String TYPE = "https://uk.godaddy.com/errors/invalid-input-error";
    private static final String TITLE = "Cannot proceed, because request input is invalid.";
    private static final int STATUS = HttpStatus.BAD_REQUEST.value();

    public InvalidInputException(final String message) {
        super(TYPE, TITLE, STATUS, message);
    }

}