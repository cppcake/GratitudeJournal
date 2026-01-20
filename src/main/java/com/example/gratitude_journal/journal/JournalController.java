package com.example.gratitude_journal.journal;

import com.example.gratitude_journal.journal.entry.JournalEntry;
import com.example.gratitude_journal.journal.entry.JournalEntryDTO;
import com.example.gratitude_journal.journal.entry.JournalEntryModelAssembler;
import com.example.gratitude_journal.journal.id_date_pair.IdDatePairDTO;

import java.util.Collection;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final JournalEntryModelAssembler journalEntryAssembler;

    public JournalController(JournalService journalService, JournalEntryModelAssembler journalEntryAssembler) {
        this.journalService = journalService;
        this.journalEntryAssembler = journalEntryAssembler;
    }

    @GetMapping("/journal/{userName}")
    public Collection<IdDatePairDTO> getEntries(@PathVariable String userName) {
        return journalService.getEntries(userName);
    }

    @PostMapping("/journal/{userName}")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<JournalEntry> addEntry(@PathVariable String userName, @RequestBody JournalEntry newEntry) {
        JournalEntry journalEntry = journalService.addEntry(userName, newEntry);
        return journalEntryAssembler.toModel(journalEntry);
    }

    @GetMapping("/journal/entry/{journalEntryId}")
    public EntityModel<JournalEntry> getEntry(@PathVariable Long journalEntryId) {
        JournalEntry journalEntry = journalService.getEntry(journalEntryId);
        return journalEntryAssembler.toModel(journalEntry);
    }

    @DeleteMapping("/journal/entry/{journalEntryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEntry(@PathVariable Long journalEntryId) {
        journalService.deleteEntry(journalEntryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/journal/entry/{journalEntryId}")
    public EntityModel<JournalEntry> updateEntry(@PathVariable Long journalEntryId,
            @RequestBody JournalEntryDTO updatedEntry) {
        JournalEntry journalEntry = journalService.putEntry(journalEntryId, updatedEntry);
        return journalEntryAssembler.toModel(journalEntry);
    }
}