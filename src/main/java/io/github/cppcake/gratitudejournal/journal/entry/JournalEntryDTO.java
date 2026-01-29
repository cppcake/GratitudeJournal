package io.github.cppcake.gratitudejournal.journal.entry;

/**
 * DTO object that clients send when creating or updating a journal entry. This
 * way they do not have to fill the automatically assigned fields journalEntryId
 * and date.
 * 
 * @param wellBeing                    The value of well-being reported by the
 *                                     user.
 * @param gratefulForToday             What the user is grateful for today.
 * @param gratefulForTodayDescription  Description of what the user is grateful
 *                                     for today.
 * @param gratefulForInLife            What the user is grateful for in life.
 * @param gratefulForInLifeDescription Description of what the user is grateful
 *                                     for in life.
 * 
 * @author Afeef Neiroukh
 */
public record JournalEntryDTO(JournalEntry.WellBeing wellBeing,
                String gratefulForToday,
                String gratefulForTodayDescription,
                String gratefulForInLife,
                String gratefulForInLifeDescription) {

        /**
         * Compare the common fields of a
         * {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntry} object to a
         * {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntryDTO} object.
         * 
         * @param entryDTO The
         *                 {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntryDTO}
         *                 object.
         * @param entry    The
         *                 {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntry}
         *                 object.
         * @return true if the common fields on the objects are equal, false otherwise.
         */
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