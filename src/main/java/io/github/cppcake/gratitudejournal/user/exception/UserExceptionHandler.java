package io.github.cppcake.gratitudejournal.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler for Spring to provide
 * {@link io.github.cppcake.gratitudejournal.user.UserController} with proper
 * HTTP-Responses in case of an exception.
 * 
 * @author Afeef Neiroukh
 */
@RestControllerAdvice
public class UserExceptionHandler {
    /**
     * Default constructor.
     */
    UserExceptionHandler() {
    }

    /**
     * Handler for the {@link NameInvalidException} Exception.
     * 
     * @param ex The {@link NameInvalidException} object
     * @return The exception message and HTTP-Code 400 Bad Request.
     */
    @ExceptionHandler(NameInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String nameInvalidHandler(NameInvalidException ex) {
        return ex.getMessage();
    }

    /**
     * Handler for the {@link UserNameTakenException} Exception
     * 
     * @param ex The {@link UserNameTakenException} object.
     * @return The exception message and HTTP-Code 409 Conflict.
     */
    @ExceptionHandler(UserNameTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String userNameTakenHandler(UserNameTakenException ex) {
        return ex.getMessage();
    }

    /**
     * Handler for the {@link UserNotFoundException} Exception
     * 
     * @param ex The {@link UserNotFoundException} object.
     * @return The exception message and HTTP-Code 404 Not Found.
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(UserNotFoundException ex) {
        return ex.getMessage();
    }
}