package com.example.gratidude_journal.journal;

import com.example.gratidude_journal.user.User;

import com.example.gratidude_journal.user.UserService;

import java.util.Collection;

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

        user.addJournalEntry(newEntry);
        userService.saveUser(userName);
    }

    public Collection<IdDatePairDTO> getEntries(String userName) {
        User user = userService.getUserByUserName(userName);

        return entryRepository.getEntriesByJournalId(user.getJournal().getJournalId());
    }
}