package com.example.gratitude_journal.user;

import com.example.gratitude_journal.user.dto.NewUserDTO;
import com.example.gratitude_journal.user.dto.UpdateUserDTO;
import com.example.gratitude_journal.user.dto.ReturnUserDTO;
import com.example.gratitude_journal.user.dto.ReturnUserDTOModelAssembler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.hateoas.EntityModel;

/**
 * Rest-Controller exposing the User-API. Part of the presentation layer of the
 * User-API.
 * 
 * @author Afeef Neiroukh
 */
@RestController
public class UserController {
    /**
     * UserService object injected by Spring.
     */
    private final UserService userService;

    /**
     * ReturnUserDTOModelAssembler object injected by Spring. Used to add HAL links
     * to responses.
     */
    private final ReturnUserDTOModelAssembler returnUserDTOAssembler;

    /**
     * Constructor of the UserController class.
     * 
     * @param userService            UserService object injected by Spring.
     * @param returnUserDTOAssembler ReturnUserDTOModelAssembler object injected by
     *                               Spring. Used to add HAL links to reponses.
     */
    public UserController(UserService userService, ReturnUserDTOModelAssembler returnUserDTOAssembler) {
        this.userService = userService;
        this.returnUserDTOAssembler = returnUserDTOAssembler;
    }

    /**
     * Retrieves a user.
     * 
     * @param userName The user name of the requested User object
     * @return An {@code EntityModel<ReturnUserDTO>} object containing the retrieved
     *         User and links to valid actions (200 OK).
     */
    @GetMapping("/user/{userName}")
    public EntityModel<ReturnUserDTO> getUser(@PathVariable String userName) {
        User user = userService.getUserByUserName(userName);

        return returnUserDTOAssembler.toModel(user.toReturnUserDTO());
    }

    /**
     * Deletes a user
     * 
     * @param userName The user name of the User object to delete.
     * @return Empty response (204 No Content).
     */
    @DeleteMapping("/user/{userName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable String userName) {
        userService.deleteUserByUserName(userName);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the mutable fields of a User object.
     * 
     * @param userName      The user name of the User object to update.
     * @param updateUserDTO The updateUserDTO object containing the updated mutable
     *                      variables.
     * @return An {@code EntityModel<ReturnUserDTO>} object containing the updated
     *         User and links to valid actions (200 OK).
     */
    @PutMapping("/user/{userName}")
    public EntityModel<ReturnUserDTO> updateUser(@PathVariable String userName,
            @RequestBody UpdateUserDTO updateUserDTO) {
        User user = userService.updateUser(userName, updateUserDTO);

        return returnUserDTOAssembler.toModel(user.toReturnUserDTO());
    }

    /**
     * Creates a new User object.
     * 
     * @param newUserDTO The NewUserDTO object containing the variables to
     *                   initialize the new user with.
     * @return An {@code EntityModel<ReturnUserDTO>} object containing the created
     *         User and links to valid actions (201 CREATED).
     */
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<ReturnUserDTO> createUser(@RequestBody NewUserDTO newUserDTO) {
        User user = userService.createUser(newUserDTO);

        return returnUserDTOAssembler.toModel(user.toReturnUserDTO());
    }
}