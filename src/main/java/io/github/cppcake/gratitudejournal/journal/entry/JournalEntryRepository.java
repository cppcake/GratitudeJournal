package io.github.cppcake.gratitudejournal.journal.entry;

import io.github.cppcake.gratitudejournal.journal.id_date_pair.IdDatePairDTO;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * JpaRepository for the JournalEntry Entity. Part of the persistence layer of
 * the Journal-API.
 * 
 * @author Afeef Neiroukh
 */
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    /**
     * Retrieves every journal entry that is assigned to a certain journal.
     * 
     * @param journalId The id of the journal.
     * @return A collection containing an
     *         {@link io.github.cppcake.gratitudejournal.journal.id_date_pair.IdDatePairDTO}
     *         object for every journal entry assigned to the journal matching the
     *         provided id. Every
     *         {@link io.github.cppcake.gratitudejournal.journal.id_date_pair.IdDatePairDTO}
     *         object contains the id and creation date of the journal entry. The
     *         results are ordered by date in descending order.
     */
    @Query("SELECT entry.journalEntryId, entry.date FROM JournalEntry entry WHERE entry.journal.journalId = ?1 ORDER BY entry.date DESC")
    Collection<IdDatePairDTO> getEntriesByJournalId(Long journalId);
}