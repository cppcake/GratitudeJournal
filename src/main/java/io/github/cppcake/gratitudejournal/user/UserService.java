package io.github.cppcake.gratitudejournal.user;

import io.github.cppcake.gratitudejournal.user.dto.SimpleUserDTO;

import org.springframework.stereotype.Service;

import io.github.cppcake.gratitudejournal.user.exception.NameInvalidException;
import io.github.cppcake.gratitudejournal.user.exception.UserNameTakenException;
import io.github.cppcake.gratitudejournal.user.exception.UserNotFoundException;

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
     * {@link io.github.cppcake.gratitudejournal.user.User#validateName(String)} method.
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
     * a simpleUserDTO object against the
     * {@link io.github.cppcake.gratitudejournal.user.User#validateName(String)} method.
     * 
     * @param userName      The user name to validate.
     * @param simpleUserDTO The SimpleUserDTO object to validate its name fields.
     * @throws NameInvalidException At least one of the provided names are invalid.
     */
    private void validateName(String userName, SimpleUserDTO simpleUserDTO) {
        validateName(userName);
        validateName(simpleUserDTO.firstName());
        validateName(simpleUserDTO.lastName());
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
     * Update a user. The updated fields must be passed as an simpleUserDTO object.
     * 
     * @param userName      The unique user name of the user to update.
     * @param simpleUserDTO The SimpleUserDTO object including the mutable fields to
     *                      apply.
     * @return The updated User object.
     * 
     * @throws NameInvalidException  One of the provided names are invalid.
     * @throws UserNotFoundException No user with the provided userName could be
     *                               found.
     */
    public User updateUser(String userName, SimpleUserDTO simpleUserDTO) {
        validateName(userName, simpleUserDTO);

        return repository.findByUserName(userName)
                .map(foundUser -> {
                    foundUser.setFirstName(simpleUserDTO.firstName());
                    foundUser.setLastName(simpleUserDTO.lastName());
                    return repository.save(foundUser);
                })
                .orElseThrow(() -> new UserNotFoundException(userName));
    }

    /**
     * Create a new user. The fields to initialize the user with must be passed as a
     * SimpleUserDTO object.
     * 
     * @param userName      User name of the User to create. Must be unique.
     * @param simpleUserDTO SimpleUserDTO object containing the required fields to
     *                      initialize the created user with.
     * @return The created User object.
     * @throws NameInvalidException   At least one of the provided names are
     *                                invalid.
     * @throws UserNameTakenException A User object matching the provided user name
     *                                already exists.
     */
    public User createUser(String userName, SimpleUserDTO simpleUserDTO) {
        validateName(userName, simpleUserDTO);

        if (repository.findByUserName(userName).isPresent())
            throw new UserNameTakenException(userName);

        User user = new User(userName, simpleUserDTO.firstName(), simpleUserDTO.lastName());
        return repository.save(user);
    }

    /**
     * Save the User object matching the user name to the repository.
     * 
     * @param userName The unique user name of the user to save to the repository.
     * @return The saved User object.
     */
    public User saveUser(String userName) {
        validateName(userName);

        return repository.save(getUserByUserName(userName));
    }
}