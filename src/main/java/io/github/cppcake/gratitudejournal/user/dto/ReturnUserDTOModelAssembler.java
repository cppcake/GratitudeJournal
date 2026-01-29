package io.github.cppcake.gratitudejournal.user.dto;

import io.github.cppcake.gratitudejournal.user.UserController;

import io.github.cppcake.gratitudejournal.journal.JournalController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Spring Component to create an EntityModel for a {@link ReturnUserDTO} object.
 * Used to add HAL-Support to {@link ReturnUserDTO} responses.
 * 
 * @author Afeef Neiroukh
 */
@Component
public class ReturnUserDTOModelAssembler
        implements RepresentationModelAssembler<ReturnUserDTO, EntityModel<ReturnUserDTO>> {

    /**
     * Default constructor.
     */
    ReturnUserDTOModelAssembler() {
    }

    /**
     * Returns the {@link ReturnUserDTO} object and the following links in the
     * "_links"
     * property:
     * <ul>
     * <li>"self", points to the GET-Request of the user</li>
     * <li>"update", points to the PUT-Request of the user</li>
     * <li>"delete", points to the DELETE-Request of the user</li>
     * <li>"journal", points to the GET-Request of the user's journal</li>
     * </ul>
     * 
     * @param returnUserDTO The {@link ReturnUserDTO} object to wrap in an
     *                      EntityModel object and add links to.
     * @return An {@code EntityModel<ReturnUserDTO>} object containing the
     *         {@link ReturnUserDTO} object with links to possible operations on it.
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