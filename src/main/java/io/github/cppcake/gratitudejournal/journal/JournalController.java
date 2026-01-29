package io.github.cppcake.gratitudejournal.journal;

import io.github.cppcake.gratitudejournal.journal.entry.JournalEntry;
import io.github.cppcake.gratitudejournal.journal.entry.JournalEntryDTO;
import io.github.cppcake.gratitudejournal.journal.entry.JournalEntryModelAssembler;
import io.github.cppcake.gratitudejournal.journal.id_date_pair.IdDatePairDTO;
import io.github.cppcake.gratitudejournal.journal.id_date_pair.IdDatePairDTOModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
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

/**
 * Rest-Controller exposing the Journal-API. Part of the presentation layer of
 * the Journal-API.
 * 
 * @author Afeef Neiroukh
 */
@RestController
public class JournalController {
    private final JournalService journalService;

    private final JournalEntryModelAssembler journalEntryModelAssembler;

    private final IdDatePairDTOModelAssembler idPairDTOModelAssembler;

    /**
     * Public constructor of the class.
     * 
     * @param journalService             JournalService object injected by Spring.
     * @param journalEntryModelAssembler JournalEntryModelAssembler object injected
     *                                   by Spring. Used to add HAL links to
     *                                   responses.
     * @param idPairDTOModelAssembler    IdPairDTOModelAssembler object injected by
     *                                   Spring. Used to add HAL links to responses.
     */
    public JournalController(JournalService journalService, JournalEntryModelAssembler journalEntryModelAssembler,
            IdDatePairDTOModelAssembler idPairDTOModelAssembler) {
        this.journalService = journalService;
        this.journalEntryModelAssembler = journalEntryModelAssembler;
        this.idPairDTOModelAssembler = idPairDTOModelAssembler;
    }

    /**
     * Retrieves a list containing an id-date pair for every journal entry belonging
     * to the user.
     * 
     * @param userName The name of the user to retrieve the journal entries from.
     * @return A {@code CollectionModel<EntityModel<IdDatePairDTO>>} object
     *         containing a list of id-date pairs of the user's journal entries,
     *         links to valid actions and HTTP-Code 200-OK.
     */
    @GetMapping("/journal/{userName}")
    public CollectionModel<EntityModel<IdDatePairDTO>> getEntries(@PathVariable String userName) {
        List<EntityModel<IdDatePairDTO>> entries = journalService.getEntries(userName).stream()
                .map(idPairDTOModelAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(entries, linkTo(methodOn(JournalController.class).getEntries(userName)).withSelfRel(),
                linkTo(methodOn(JournalController.class).addEntry(userName, null)).withRel("create"));
    }

    /**
     * Adds a journal entry to a user's journal.
     * 
     * @param userName The user name of the user to add the entry to.
     * @param newEntry The new entry to add.
     * @return An {@code EntityModel<JournalEntry>} object containing the added
     *         entry, links to valid actions and HTTP-Code 201-CREATED.
     */
    @PostMapping("/journal/{userName}")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<JournalEntry> addEntry(@PathVariable String userName, @RequestBody JournalEntry newEntry) {
        JournalEntry journalEntry = journalService.addEntry(userName, newEntry);
        return journalEntryModelAssembler.toModel(journalEntry);
    }

    /**
     * Retrieves a journal entry.
     * 
     * @param journalEntryId The id of the requested journal entry.
     * @return An {@code EntityModel<JournalEntry>} object containing the retrieved
     *         entry, links to valid actions and HTTP-Code 200-OK.
     */
    @GetMapping("/journal/entry/{journalEntryId}")
    public EntityModel<JournalEntry> getEntry(@PathVariable Long journalEntryId) {
        JournalEntry journalEntry = journalService.getEntry(journalEntryId);
        return journalEntryModelAssembler.toModel(journalEntry);
    }

    /**
     * Deletes an entry.
     * 
     * @param journalEntryId The id of the journal entry to delete.
     * @return Empty response and HTTP-Code 204-NO-CONTENT.
     */
    @DeleteMapping("/journal/entry/{journalEntryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEntry(@PathVariable Long journalEntryId) {
        journalService.deleteEntry(journalEntryId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the mutable fields of a
     * {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntry} object.
     * 
     * @param journalEntryId The id of the
     *                       {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntry}
     *                       object to update.
     * @param updatedEntry   The
     *                       {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntryDTO}
     *                       object containing the updated mutable variables.
     * @return An {@code EntityModel<JournalEntry>} object containing the updated
     *         entry, links to valid actions and HTTP-Code 200-OK.
     */
    @PutMapping("/journal/entry/{journalEntryId}")
    public EntityModel<JournalEntry> updateEntry(@PathVariable Long journalEntryId,
            @RequestBody JournalEntryDTO updatedEntry) {
        JournalEntry journalEntry = journalService.updateEntry(journalEntryId, updatedEntry);
        return journalEntryModelAssembler.toModel(journalEntry);
    }
}