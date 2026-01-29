package io.github.cppcake.gratitudejournal.journal.entry;

import io.github.cppcake.gratitudejournal.journal.JournalController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Spring Component to create an EntityModel for a
 * {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntry} object. Used
 * to add HAL-Support to JournalEntry responses.
 * 
 * @author Afeef Neiroukh
 */
@Component
public class JournalEntryModelAssembler
                implements RepresentationModelAssembler<JournalEntry, EntityModel<JournalEntry>> {

        /**
         * Default constructor.
         */
        JournalEntryModelAssembler() {

        }

        /**
         * Returns the {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntry}
         * object and the following links in the "_links" property:
         * <ul>
         * <li>"self", points to the GET-Request of the journal entry</li>
         * <li>"update", points to the PUT-Request of the journal entry</li>
         * <li>"delete", points to the DELETE-Request of the journal entry</li>
         * </ul>
         * 
         * @param journalEntry The
         *                     {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntry}
         *                     object to wrap in an EntityModel object and add links to.
         * @return An {@code EntityModel<JournalEntry>} object containing the
         *         {@link io.github.cppcake.gratitudejournal.journal.entry.JournalEntry}
         *         object with links to possible operations on it.
         */
        @Override
        public EntityModel<JournalEntry> toModel(JournalEntry journalEntry) {
                return EntityModel.of(journalEntry,
                                linkTo(methodOn(JournalController.class).getEntry(journalEntry.getJournalEntryId()))
                                                .withSelfRel(),
                                linkTo(methodOn(JournalController.class).updateEntry(journalEntry.getJournalEntryId(),
                                                null))
                                                .withRel("update"),
                                linkTo(methodOn(JournalController.class).deleteEntry(journalEntry.getJournalEntryId()))
                                                .withRel("delete"));
        }
}