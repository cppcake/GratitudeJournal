package io.github.cppcake.gratitudejournal.journal.exception;

import java.time.LocalDate;

/**
 * Exception thrown when the journal entry already exists for a certain date.
 * 
 * @author Afeef Neiroukh
 */
public class EntryAlreadyExistsException extends RuntimeException {
    /**
     * Public constructor of the EntryAlreadyExistsException class.
     * 
     * @param date The date of conflict.
     */
    public EntryAlreadyExistsException(LocalDate date) {
        super("An entry already exists for " + date.toString());
    }
}