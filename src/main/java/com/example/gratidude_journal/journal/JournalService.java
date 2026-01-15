package com.example.gratidude_journal.journal;

import com.example.gratidude_journal.journal.entry.JournalEntry;
import com.example.gratidude_journal.journal.entry.JournalEntryDTO;
import com.example.gratidude_journal.journal.entry.IdDatePairDTO;
import com.example.gratidude_journal.journal.entry.JournalEntryRepository;

import com.example.gratidude_journal.journal.exception.EntryNotFoundException;
import com.example.gratidude_journal.user.User;

import com.example.gratidude_journal.user.UserService;
import com.example.gratidude_journal.user.exception.UserNotFoundException;

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

    public void addEntry(String userName, JournalEntry newEntry) {
        User user = userService.getUserByUserName(userName);

        user.getJournal().addEntry(newEntry);
        userService.saveUser(userName);
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
                    foundEntry.setGratefullForToday(updatedEntry.gratefullForToday());
                    foundEntry.setGratefullForTodayDescription(updatedEntry.gratefullForTodayDescription());
                    foundEntry.setGratefullForInLife(updatedEntry.gratefullForInLife());
                    foundEntry.setGratefullForInLifeDescription(updatedEntry.gratefullForInLifeDescription());
                    return entryRepository.save(foundEntry);
                })
                .orElseThrow(() -> new EntryNotFoundException(journalEntryId));
    }
}