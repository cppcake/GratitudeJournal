package com.example.gratidude_journal.journal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/*
    Presentation Layer for Journal-API
*/
@RestController
public class JournalController {
    @PostMapping("/journaling/{userName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addEntry(@PathVariable String userName, @RequestBody JournalEntry newEntry) {
        /*
         * repository.findByUserName(userName)
         * .map(foundUser -> {
         * foundUser.addJournalEntry(newEntry);
         * return repository.save(foundUser);
         * })
         * .orElseThrow(() -> new UserNotFoundException(userName));
         */
    }
}