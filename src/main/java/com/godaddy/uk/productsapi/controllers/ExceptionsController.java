package com.godaddy.uk.productsapi.controllers;

import com.godaddy.uk.productsapi.exceptions.ApiException;
import com.godaddy.uk.productsapi.exceptions.ForbiddenException;
import com.godaddy.uk.productsapi.exceptions.InvalidInputException;
import com.godaddy.uk.productsapi.exceptions.UnexpectedException;
import com.godaddy.uk.productsapi.utilities.Loggable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.stream.Collectors.joining;

/**
 * Exception controller to wrap expected errors into {@link ApiException}.
 */
@RestControllerAdvice
public class ExceptionsController implements Loggable {

    /**
     * Handles any type of {@link ApiException}.
     *
     * @param apiError that occurred
     * @return ResponseEntity of type ApiError
     */
    @ExceptionHandler(ApiException.class)
    private ResponseEntity<ApiException> handleProblemDetailException(final ApiException apiError) {
        return ResponseEntity
                .status(apiError.getStatus())
                .body(apiError);
    }

    /**
     * Handles unexpected error (throwable) and converts it {@link ApiException}.
     *
     * @param throwable any unexpected exception
     * @return ResponseEntity of type ApiError
     */
    @ExceptionHandler(Throwable.class)
    private ResponseEntity<ApiException> handleUnexpectedException(final Throwable throwable) {
        // if access denied, return ForbiddenException
        if (throwable instanceof AccessDeniedException) {
            logger().warn("Access denied exception occurred.");
            return handleProblemDetailException(new ForbiddenException("Access has been denied."));
        }

        // if bean validation, return InvalidInputException along with error messages
        if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) throwable;
            String errorMessages = exception.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(joining(" "));
            return handleProblemDetailException(new InvalidInputException("Request failed validation due to following errors: " + errorMessages));
        }

        // any other case, log error and return generic exception
        logger().error("Unexpected server error occurred, details: ", throwable);
        return handleProblemDetailException(new UnexpectedException());
    }

}