package com.example.gratidude_journal.journal;

import com.example.gratidude_journal.journal.exception.*;

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

/*
    Part of the Persistence Layer for Journal-API
*/
@Entity
public class Journal {
    @Column(name = "journal_id")
    private @Id @GeneratedValue Long journalId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JournalEntry> journalEntries = new HashSet<JournalEntry>();

    public Journal() {
    }

    public void addEntry(JournalEntry newEntry) {
        if (newEntry == null)
            throw new IllegalArgumentException("Entry is null.");

        LocalDate today = LocalDate.now();
        if (!today.equals(newEntry.getDate()))
            throw new RuntimeException("Entry is not from today.");

        if (this.hasEntryForToday())
            throw new EntryAlreadyExistsException(newEntry.getDate());

        newEntry.setJournal(this);
        System.out.println(newEntry.toString());
        journalEntries.add(newEntry);
    }

    public boolean hasEntryForToday() {
        // This is inefficient but also super simple. Could be done in constant time in
        // the future.
        LocalDate today = LocalDate.now();
        return journalEntries.stream().anyMatch(entry -> today.equals(entry.getDate()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Journal))
            return false;
        Journal journal = (Journal) o;
        return Objects.equals(this.journalId, journal.journalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.journalId);
    }

    @Override
    public String toString() {
        return "Journal with id " + journalId.toString();
    }
}