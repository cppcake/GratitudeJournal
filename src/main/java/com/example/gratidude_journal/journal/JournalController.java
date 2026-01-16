package com.example.gratidude_journal.journal;

import com.example.gratidude_journal.journal.entry.JournalEntry;
import com.example.gratidude_journal.journal.entry.JournalEntryDTO;
import com.example.gratidude_journal.journal.entry.IdDatePairDTO;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/*
    Presentation Layer for Journal-API
*/
@RestController
public class JournalController {
    private final JournalService journalService;

    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    @GetMapping("/journal/{userName}")
    public Collection<IdDatePairDTO> getEntries(@PathVariable String userName) {
        return journalService.getEntries(userName);
    }

    @PostMapping("/journal/{userName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEntry(@PathVariable String userName, @RequestBody JournalEntry newEntry) {
        journalService.addEntry(userName, newEntry);
    }

    @GetMapping("/journal/entry/{journalEntryId}")
    public JournalEntry getEntry(@PathVariable Long journalEntryId) {
        return journalService.getEntry(journalEntryId);
    }

    @PutMapping("/journal/entry/{journalEntryId}")
    public JournalEntry putEntry(@PathVariable Long journalEntryId, @RequestBody JournalEntryDTO updatedEntry) {
        return journalService.putEntry(journalEntryId, updatedEntry);
    }

    @DeleteMapping("/journal/entry/{journalEntryId}")
    public void deleteEntry(@PathVariable Long journalEntryId) {
        journalService.deleteEntry(journalEntryId);
    }
}