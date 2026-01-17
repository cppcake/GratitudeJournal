package com.example.gratidude_journal.user;

import org.springframework.stereotype.Service;

import com.example.gratidude_journal.user.exception.NameInvalidException;
import com.example.gratidude_journal.user.exception.UserNameTakenException;
import com.example.gratidude_journal.user.exception.UserNotFoundException;

/*
    Service Layer for User-API
*/
@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    private void validateName(String name) {
        if (!User.validateName(name))
            throw new NameInvalidException(name);
    }

    private void validateName(String userName, UserUpdateDTO userUpdateDTO) {
        validateName(userName);
        validateName(userUpdateDTO.firstName());
        validateName(userUpdateDTO.lastName());
    }

    private void validateName(UserDTO user) {
        validateName(user.userName());
        validateName(user.firstName());
        validateName(user.lastName());
    }

    public User getUserByUserName(String userName) {
        validateName(userName);

        User user = repository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));

        return user;
    }

    public void deleteUserByUserName(String userName) {
        validateName(userName);

        repository.findByUserName(userName)
                .ifPresentOrElse(
                        foundUser -> repository.deleteById(foundUser.getUserId()),
                        () -> {
                            throw new UserNotFoundException(userName);
                        });
    }

    public User updateUser(String userName, UserUpdateDTO updatedUser) {
        validateName(userName, updatedUser);

        return repository.findByUserName(userName)
                .map(foundUser -> {
                    foundUser.setFirstName(updatedUser.firstName());
                    foundUser.setLastName(updatedUser.lastName());
                    return repository.save(foundUser);
                })
                .orElseThrow(() -> new UserNotFoundException(userName));
    }

    public User createUser(UserDTO userDTO) {
        validateName(userDTO);

        if (repository.findByUserName(userDTO.userName()).isPresent())
            throw new UserNameTakenException(userDTO.userName());

        User user = new User(userDTO.userName(), userDTO.firstName(), userDTO.lastName());
        return repository.save(user);
    }

    public User saveUser(String userName) {
        validateName(userName);

        return repository.save(getUserByUserName(userName));
    }
}