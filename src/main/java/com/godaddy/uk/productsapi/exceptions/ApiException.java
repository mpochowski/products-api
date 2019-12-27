package com.godaddy.uk.productsapi.exceptions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * <p>
 * Generic abstract exception class for defining platform exceptions.
 * Based on RFC7807: <a href="https://tools.ietf.org/html/rfc7807">specification</a>.
 * </p>
 * <p>
 * Requires each concrete exception to define:
 * 1. Type - unique sequence e.g. URL, that can be later used by FE application to provide more details, internationalised message, or even page describing error
 * 2. Title - short definition of the problem
 * 3. Status - html status code
 * 4. Message - message related to this instance of the problem
 * </p>
 * <p>
 * The class extends {@link RuntimeException} so that the errors does not need to be caught and
 * provides json annotations to ensure no extra information leak out during json parsing.
 * </p>
 */
@JsonAutoDetect(fieldVisibility = NONE, setterVisibility = NONE, getterVisibility = NONE, isGetterVisibility = NONE, creatorVisibility = NONE)
public abstract class ApiException extends RuntimeException {

    @JsonProperty("type")
    private final String type;
    @JsonProperty("title")
    private final String title;
    @JsonProperty("status")
    private final int status;
    @JsonProperty("message")
    private final String message;

    ApiException(final String type, final String title, final int status, final String message) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
