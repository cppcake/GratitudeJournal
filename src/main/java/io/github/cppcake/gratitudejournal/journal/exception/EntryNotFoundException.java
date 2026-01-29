package io.github.cppcake.gratitudejournal.journal.exception;

/**
 * Exception thrown when the requested journal entry is not found.
 * 
 * @author Afeef Neiroukh
 */
public class EntryNotFoundException extends RuntimeException {
    /**
     * Public constructor of the EntryNotFoundException class.
     * 
     * @param journalEntryId The id of the missing journal entry.
     */
    public EntryNotFoundException(Long journalEntryId) {
        super("No entry exists for journalEntryId " + journalEntryId.toString());
    }
}