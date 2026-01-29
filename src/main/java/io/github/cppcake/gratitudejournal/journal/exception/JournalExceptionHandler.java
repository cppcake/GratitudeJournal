package io.github.cppcake.gratitudejournal.journal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler for Spring to provide
 * {@link io.github.cppcake.gratitudejournal.journal.JournalController} with proper
 * HTTP-Responses in case of an exception.
 * 
 * @author Afeef Neiroukh
 */
@RestControllerAdvice
public class JournalExceptionHandler {
    /**
     * Default constructor.
     */
    JournalExceptionHandler() {
    }

    /**
     * Handler for the {@link EntryAlreadyExistsException} Exception.
     * 
     * @param ex The {@link EntryAlreadyExistsException} object
     * @return The exception message and HTTP-Code 409 Conflict.
     */
    @ExceptionHandler(EntryAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String entryAlreadyExistsHandler(EntryAlreadyExistsException ex) {
        return ex.getMessage();
    }

    /**
     * Handler for the {@link EntryNotFoundException} Exception.
     * 
     * @param ex The {@link EntryNotFoundException} object
     * @return The exception message and HTTP-Code 404 Not Found.
     */
    @ExceptionHandler(EntryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String entryDoesNotExistHandler(EntryNotFoundException ex) {
        return ex.getMessage();
    }
}