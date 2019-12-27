package com.godaddy.uk.productsapi.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Indicates unexpected api error.
 * The message is constant and generic, to don't expose error details.
 */
public class UnexpectedException extends ApiException {

    private static final String TYPE = "https://uk.godaddy.com/errors/unexpected-error";
    private static final String TITLE = "about:blank";
    private static final int STATUS = HttpStatus.INTERNAL_SERVER_ERROR.value();
    private static final String MESSAGE = "Unexpected error occurred.";

    public UnexpectedException() {
        super(TYPE, TITLE, STATUS, MESSAGE);
    }

}