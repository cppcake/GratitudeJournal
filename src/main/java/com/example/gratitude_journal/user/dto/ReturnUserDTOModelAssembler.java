package com.example.gratitude_journal.user.dto;

import com.example.gratitude_journal.user.UserController;

import com.example.gratitude_journal.journal.JournalController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Spring Component to create an EntityModel for a ReturnUserDTO object. Used to
 * add HAL-Support to ReturnUserDTO responses.
 * 
 * @author Afeef Neiroukh
 */
@Component
public class ReturnUserDTOModelAssembler
        implements RepresentationModelAssembler<ReturnUserDTO, EntityModel<ReturnUserDTO>> {

    /**
     * Return the ReturnUserDTO object and the following links in the "_links" property:
     * <ul>
     * <li>"self", points to the GET-Request of the user</li>
     * <li>"update", points to the PUT-Request of the user</li>
     * <li>"delete", points to the DELETE-Request of the user</li>
     * <li>"journal", points to the GET-Request of the user's journal</li>
     * </ul>
     * 
     * @param returnUserDTO The ReturnUserDTO object to wrap around an EntityModel
     *                      object and add links to.
     * @return An {@code EntityModel<ReturnUserDTO>} object containing
     *         the ReturnUserDTO object with links to possible operations on it.
     */
    @Override
    public EntityModel<ReturnUserDTO> toModel(ReturnUserDTO returnUserDTO) {
        return EntityModel.of(returnUserDTO,
                linkTo(methodOn(UserController.class).getUser(returnUserDTO.userName())).withSelfRel(),
                linkTo(methodOn(UserController.class).updateUser(returnUserDTO.userName(), null)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(returnUserDTO.userName())).withRel("delete"),
                linkTo(methodOn(JournalController.class).getEntries(returnUserDTO.userName())).withRel("journal"));
    }
}