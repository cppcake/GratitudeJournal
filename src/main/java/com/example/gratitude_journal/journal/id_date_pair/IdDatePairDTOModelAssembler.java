package com.example.gratitude_journal.journal.id_date_pair;

import com.example.gratitude_journal.journal.JournalController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class IdDatePairDTOModelAssembler
                implements RepresentationModelAssembler<IdDatePairDTO, EntityModel<IdDatePairDTO>> {

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