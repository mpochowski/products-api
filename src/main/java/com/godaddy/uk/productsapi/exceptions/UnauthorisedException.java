package com.godaddy.uk.productsapi.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorisedException extends ApiException {

    private static final String TYPE = "https://uk.godaddy.com/errors/unauthorised-error";
    private static final String TITLE = "Cannot proceed, because user is not authorised.";
    private static final int STATUS = HttpStatus.UNAUTHORIZED.value();

    public UnauthorisedException(final String message) {
        super(TYPE, TITLE, STATUS, message);
    }

}