package com.godaddy.uk.productsapi.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    private static final String TYPE = "https://uk.godaddy.com/errors/not-found-error";
    private static final String TITLE = "Cannot proceed, because required resource was not found.";
    private static final int STATUS = HttpStatus.NOT_FOUND.value();

    public NotFoundException(final String message) {
        super(TYPE, TITLE, STATUS, message);
    }

}