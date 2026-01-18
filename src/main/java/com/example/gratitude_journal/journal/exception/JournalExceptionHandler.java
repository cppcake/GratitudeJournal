package com.example.gratitude_journal.journal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JournalExceptionHandler {

    @ExceptionHandler(EntryAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String entryAlreadyExistsHandler(EntryAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(EntryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String entryDoesNotExistHandler(EntryNotFoundException ex) {
        return ex.getMessage();
    }
}