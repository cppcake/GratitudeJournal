package com.example.gratitude_journal.journal.exception;

import java.time.LocalDate;

public class EntryAlreadyExistsException extends RuntimeException {
    public EntryAlreadyExistsException(LocalDate date) {
        super("An entry already exists for " + date.toString());
    }
}