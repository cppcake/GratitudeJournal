package com.example.gratitude_journal.journal.exception;

public class EntryNotFoundException extends RuntimeException {
    public EntryNotFoundException(Long journalEntryId) {
        super("No entry exists for journalEntryId " + journalEntryId.toString());
    }
}