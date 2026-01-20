package com.example.gratitude_journal.journal;

import com.example.gratitude_journal.journal.entry.JournalEntry;
import com.example.gratitude_journal.journal.entry.JournalEntryDTO;
import com.example.gratitude_journal.journal.id_date_pair.IdDatePairDTO;
import com.example.gratitude_journal.journal.entry.JournalEntryRepository;

import com.example.gratitude_journal.journal.exception.EntryNotFoundException;
import com.example.gratitude_journal.user.User;

import com.example.gratitude_journal.user.UserService;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

/*
    Service Layer for Journal-API
*/
@Service
public class JournalService {
    private final JournalEntryRepository entryRepository;
    private final UserService userService;

    public JournalService(JournalEntryRepository entryRepository, UserService userService) {
        this.entryRepository = entryRepository;
        this.userService = userService;
    }

    public JournalEntry addEntry(String userName, JournalEntry newEntry) {
        User user = userService.getUserByUserName(userName);

        user.getJournal().addEntry(newEntry);
        userService.saveUser(userName);

        return newEntry;
    }

    public Collection<IdDatePairDTO> getEntries(String userName) {
        User user = userService.getUserByUserName(userName);

        return entryRepository.getEntriesByJournalId(user.getJournal().getJournalId());
    }

    public JournalEntry getEntry(Long journalEntryId) {
        Optional<JournalEntry> entry = entryRepository.findById(journalEntryId);

        if (!entry.isPresent())
            throw new EntryNotFoundException(journalEntryId);

        return entry.get();
    }

    public JournalEntry putEntry(Long journalEntryId, JournalEntryDTO updatedEntry) {
        return entryRepository.findById(journalEntryId)
                .map(foundEntry -> {
                    foundEntry.setWellBeing(updatedEntry.wellBeing());
                    foundEntry.setGratefulForToday(updatedEntry.gratefulForToday());
                    foundEntry.setGratefulForTodayDescription(updatedEntry.gratefulForTodayDescription());
                    foundEntry.setGratefulForInLife(updatedEntry.gratefulForInLife());
                    foundEntry.setGratefulForInLifeDescription(updatedEntry.gratefulForInLifeDescription());
                    return entryRepository.save(foundEntry);
                })
                .orElseThrow(() -> new EntryNotFoundException(journalEntryId));
    }

    public void deleteEntry(Long journalEntryId) {
        entryRepository.findById(journalEntryId)
                .ifPresentOrElse(entry -> entryRepository.delete(entry),
                        () -> {
                            throw new EntryNotFoundException(journalEntryId);
                        });
    }
}