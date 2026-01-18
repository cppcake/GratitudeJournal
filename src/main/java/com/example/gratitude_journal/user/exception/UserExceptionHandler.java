package com.example.gratitude_journal.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(NameInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String nameInvalidHandler(NameInvalidException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserNameTakenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String userNameTakenHandler(UserNameTakenException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(UserNotFoundException ex) {
        return ex.getMessage();
    }
}