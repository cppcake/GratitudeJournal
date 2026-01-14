package com.example.gratidude_journal.journal;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/*
    Persistence Layer for Journal-API
*/
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    @Query("SELECT  entry.journalEntryId, entry.date FROM JournalEntry entry WHERE entry.journal.journalId = ?1 ORDER BY entry.date DESC")
    Collection<IdDatePairDTO> getEntriesByJournalId(Long journalId);
}