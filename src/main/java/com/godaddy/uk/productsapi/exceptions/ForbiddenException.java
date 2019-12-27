package com.godaddy.uk.productsapi.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiException {

    private static final String TYPE = "https://uk.godaddy.com/errors/forbidden-error";
    private static final String TITLE = "Cannot proceed, because the operation has been forbidden.";
    private static final int STATUS = HttpStatus.FORBIDDEN.value();

    public ForbiddenException(final String message) {
        super(TYPE, TITLE, STATUS, message);
    }

}