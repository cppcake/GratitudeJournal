package com.example.gratitude_journal.user;

import com.example.gratitude_journal.user.dto.NewUserDTO;
import com.example.gratitude_journal.user.dto.UpdateUserDTO;

import org.springframework.stereotype.Service;

import com.example.gratitude_journal.user.exception.NameInvalidException;
import com.example.gratitude_journal.user.exception.UserNameTakenException;
import com.example.gratitude_journal.user.exception.UserNotFoundException;

/**
 * Service providing operations on User entities and enforcing business logic.
 * Part of the service layer of the User-API.
 * 
 * @author Afeef Neiroukh
 */
@Service
public class UserService {
    private final UserRepository repository;

    /**
     * UserService constructor.
     * 
     * @param repository UserRepository to apply changes to. Is injected by the
     *                   Spring framework.
     */
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Helper method to validate a name against the
     * {@link com.example.gratitude_journal.user.User#validateName(String)} method.
     * 
     * @param name The name to validate
     * @throws NameInvalidException Provided name is invalid.
     */
    private void validateName(String name) {
        if (!User.validateName(name))
            throw new NameInvalidException(name);
    }

    /**
     * Convenient method to validate a name and the fields firstName and lastName of
     * a userUpdateDTO object against the
     * {@link com.example.gratitude_journal.user.User#validateName(String)} method.
     * 
     * @param userName      The user name to validate.
     * @param userUpdateDTO The UserUpdateDTO object to validate its name fields.
     * @throws NameInvalidException At least one of the provided names are invalid.
     */
    private void validateName(String userName, UpdateUserDTO userUpdateDTO) {
        validateName(userName);
        validateName(userUpdateDTO.firstName());
        validateName(userUpdateDTO.lastName());
    }

    /**
     * Convenient method to validate the name fields (userName, firstName and
     * lastName) of a NewUserDTO object against the
     * {@link com.example.gratitude_journal.user.User#validateName(String)} method.
     * 
     * @param newUser The NewUserDTO object to validate its name fields.
     * @throws NameInvalidException At least one of the provided names are invalid.
     */
    private void validateName(NewUserDTO newUser) {
        validateName(newUser.userName());
        validateName(newUser.firstName());
        validateName(newUser.lastName());
    }

    /**
     * Get a user by its userName.
     * 
     * @param userName The unique user name of the requested user.
     * @return Returns the requested User object matching the provided user name.
     * @throws NameInvalidException  Provided name is invalid.
     * @throws UserNotFoundException No user with the provided userName could be
     *                               found.
     */
    public User getUserByUserName(String userName) {
        validateName(userName);

        User user = repository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));

        return user;
    }

    /**
     * Delete a user by its userName.
     * 
     * @param userName The unique user name of the user to delete.
     * @throws NameInvalidException  Provided name is invalid.
     * @throws UserNotFoundException No user with the provided userName could be
     *                               found.
     * 
     */
    public void deleteUserByUserName(String userName) {
        validateName(userName);

        repository.findByUserName(userName)
                .ifPresentOrElse(
                        foundUser -> repository.deleteById(foundUser.getUserId()),
                        () -> {
                            throw new UserNotFoundException(userName);
                        });
    }

    /**
     * Update a user. The updated fields must be passed as an updateUserDTO object.
     * 
     * @param userName      The unique user name of the user to update.
     * @param updateUserDTO The UpdateUserDTO object including the mutable fields to
     *                      apply.
     * @return The updated User object.
     * 
     * @throws NameInvalidException  One of the provided names are invalid.
     * @throws UserNotFoundException No user with the provided userName could be
     *                               found.
     */
    public User updateUser(String userName, UpdateUserDTO updateUserDTO) {
        validateName(userName, updateUserDTO);

        return repository.findByUserName(userName)
                .map(foundUser -> {
                    foundUser.setFirstName(updateUserDTO.firstName());
                    foundUser.setLastName(updateUserDTO.lastName());
                    return repository.save(foundUser);
                })
                .orElseThrow(() -> new UserNotFoundException(userName));
    }

    /**
     * Create a new user. The fields to initialize the user with must be passed as a
     * NewUserDTO object.
     * 
     * @param newUserDTO NewUserDTO object containing the required fields to
     *                   initialize the created user with.
     * @return The created User object.
     * @throws NameInvalidException   At least one of the provided names are
     *                                invalid.
     * @throws UserNameTakenException A User object matching the provided user name
     *                                already exists.
     */
    public User createUser(NewUserDTO newUserDTO) {
        validateName(newUserDTO);

        if (repository.findByUserName(newUserDTO.userName()).isPresent())
            throw new UserNameTakenException(newUserDTO.userName());

        User user = new User(newUserDTO.userName(), newUserDTO.firstName(), newUserDTO.lastName());
        return repository.save(user);
    }

    /**
     * Save the User object matching the user name to the repository.
     * 
     * @param userName
     * @return
     */
    public User saveUser(String userName) {
        validateName(userName);

        return repository.save(getUserByUserName(userName));
    }
}