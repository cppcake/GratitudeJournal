package com.example.gratitude_journal.user.dto;

import com.example.gratitude_journal.user.UserController;

import com.example.gratitude_journal.journal.JournalController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ReturnUserDTOModelAssembler
        implements RepresentationModelAssembler<ReturnUserDTO, EntityModel<ReturnUserDTO>> {

    @Override
    public EntityModel<ReturnUserDTO> toModel(ReturnUserDTO returnUserDTO) {
        return EntityModel.of(returnUserDTO,
                linkTo(methodOn(UserController.class).getUser(returnUserDTO.userName())).withSelfRel(),
                linkTo(methodOn(UserController.class).updateUser(returnUserDTO.userName(), null)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(returnUserDTO.userName())).withRel("delete"),
                linkTo(methodOn(JournalController.class).getEntries(returnUserDTO.userName())).withRel("journal"));
    }
}