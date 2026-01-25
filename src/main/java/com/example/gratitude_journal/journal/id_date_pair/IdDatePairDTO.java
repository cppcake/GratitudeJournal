package com.example.gratitude_journal.journal.id_date_pair;

import java.time.LocalDate;

/**
 * Record to provide a an id-date pair. Used to provide an overview of all
 * journal entries.
 * 
 * @param id   The id to save.
 * @param date The date to save.
 * 
 * @author Afeef Neiroukh
 */
public record IdDatePairDTO(Long id, LocalDate date) {
}