package com.example.gratitude_journal.journal.id_date_pair;

import com.example.gratitude_journal.journal.JournalController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Spring Component to create an EntityModel for a
 * {@link com.example.gratitude_journal.journal.id_date_pair.IdDatePairDTO}
 * object. Used to add HAL-Support to IdDatePairDTO responses.
 * 
 * @author Afeef Neiroukh
 */
@Component
public class IdDatePairDTOModelAssembler
                implements RepresentationModelAssembler<IdDatePairDTO, EntityModel<IdDatePairDTO>> {

        /**
         * Returns the
         * {@link com.example.gratitude_journal.journal.id_date_pair.IdDatePairDTO}
         * object and the following links in the "_links" property:
         * <ul>
         * <li>"self", points to the GET-Request of the journal entry matching the
         * id</li>
         * <li>"update", points to the PUT-Request of the journal entry matching the
         * id</li>
         * <li>"delete", points to the DELETE-Request of the journal entry matching the
         * id</li>
         * </ul>
         * 
         * @param idDatePair The
         *                   {@link com.example.gratitude_journal.journal.id_date_pair.IdDatePairDTO}
         *                   object to wrap in an EntityModel object and add links to.
         * @return An {@code EntityModel<JournalEntry>} object containing the
         *         {@link com.example.gratitude_journal.journal.id_date_pair.IdDatePairDTO}
         *         object with links to possible operations on it.
         */
        @Override
        public EntityModel<IdDatePairDTO> toModel(IdDatePairDTO idDatePair) {
                return EntityModel.of(idDatePair,
                                linkTo(methodOn(JournalController.class).getEntry(idDatePair.id())).withSelfRel(),
                                linkTo(methodOn(JournalController.class).deleteEntry(idDatePair.id()))
                                                .withRel("delete"),
                                linkTo(methodOn(JournalController.class).updateEntry(idDatePair.id(), null))
                                                .withRel("update"));
        }
}