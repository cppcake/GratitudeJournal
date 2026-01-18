package com.example.gratitude_journal.journal.entry;

public record JournalEntryDTO(JournalEntry.WellBeing wellBeing,
                String gratefulForToday,
                String gratefulForTodayDescription,
                String gratefulForInLife,
                String gratefulForInLifeDescription) {

        public static boolean compareToEntry(JournalEntryDTO entryDTO, JournalEntry entry) {
                if (entryDTO == null && entry == null)
                        return true;
                if (entryDTO == null || entry == null)
                        return false;
                return entryDTO.wellBeing.equals(entry.getWellBeing())
                                && entryDTO.gratefulForToday().equals(entry.getGratefulForToday())
                                && entryDTO.gratefulForTodayDescription()
                                                .equals(entry.getGratefulForTodayDescription())
                                && entryDTO.gratefulForInLife().equals(entry.getGratefulForInLife())
                                && entryDTO.gratefulForInLifeDescription()
                                                .equals(entry.getGratefulForInLifeDescription());
        }
}