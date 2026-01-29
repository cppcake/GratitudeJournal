package io.github.cppcake.gratitudejournal.journal;

import io.github.cppcake.gratitudejournal.journal.entry.JournalEntry;
import io.github.cppcake.gratitudejournal.journal.exception.*;

import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * JPA-Entity to represent a journal in the GratitudeJournal Service. Part of
 * the persistence layer of the Journal-API.
 * 
 * Every journal is uniquely identified by its primary key journalId, which is
 * assigned automatically. Furthermore a journal includes a Set of
 * {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntry} objects.
 * 
 * @author Afeef Neiroukh
 */
@Entity
public class Journal {
    /**
     * The private primary key of the journal.
     */
    @Column(name = "journal_id")
    private @Id @GeneratedValue Long journalId;

    /**
     * The private set of
     * {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntry}
     * objects.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JournalEntry> journalEntries = new HashSet<JournalEntry>();

    /**
     * Public empty constructor.
     */
    public Journal() {
    }

    /**
     * Adds a new entry to {@link #journalEntries}.
     * 
     * @param newEntry The new
     *                 {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntry}
     *                 object to add. The entry must be from today.
     * @throws EntryAlreadyExistsException An entry already exists for today.
     * @throws RuntimeException            The date of the entry is not today.
     * @throws IllegalArgumentException    The passed object is null.
     */
    public void addEntry(JournalEntry newEntry) {
        if (newEntry == null)
            throw new IllegalArgumentException("Entry is null.");

        LocalDate today = LocalDate.now();
        if (!today.equals(newEntry.getDate()))
            throw new RuntimeException("Entry is not from today.");

        if (this.hasEntryForToday())
            throw new EntryAlreadyExistsException(newEntry.getDate());

        newEntry.setJournal(this);
        journalEntries.add(newEntry);
    }

    /**
     * Private helper method to check if an entry already exists for today.
     * 
     * @return True if the journal includes an entry from today, false otherwise.
     */
    private boolean hasEntryForToday() {
        LocalDate today = LocalDate.now();
        // While this is inefficient, it is also simple and safe. It could however be
        // done in constant time in the future.
        return journalEntries.stream().anyMatch(entry -> today.equals(entry.getDate()));
    }

    /**
     * Getter for this object's journalEntries set.
     * 
     * @return The set of journal entries.
     */
    public Set<JournalEntry> getJournalEntries() {
        return journalEntries;
    }

    /**
     * Getter for this object's primary key.
     * 
     * @return The journalId of this journal.
     */
    public Long getJournalId() {
        return journalId;
    }

    /**
     * Compares two journals based on their primary keys.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Journal))
            return false;
        Journal journal = (Journal) o;
        return Objects.equals(this.journalId, journal.journalId);
    }

    /**
     * Generates the hash code of a journal based on its primary key.
     * 
     * @return The hash code of this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.journalId);
    }

    /**
     * Provides a basic String representation of this object.
     * 
     * @return String representation of this object.
     */
    @Override
    public String toString() {
        return "Journal with id " + journalId.toString();
    }
}