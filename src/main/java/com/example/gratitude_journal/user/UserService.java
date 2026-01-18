package com.example.gratitude_journal.user;

import com.example.gratitude_journal.user.dto.NewUserDTO;
import com.example.gratitude_journal.user.dto.UpdateUserDTO;

import org.springframework.stereotype.Service;

import com.example.gratitude_journal.user.exception.NameInvalidException;
import com.example.gratitude_journal.user.exception.UserNameTakenException;
import com.example.gratitude_journal.user.exception.UserNotFoundException;

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

    private void validateName(String userName, UpdateUserDTO userUpdateDTO) {
        validateName(userName);
        validateName(userUpdateDTO.firstName());
        validateName(userUpdateDTO.lastName());
    }

    private void validateName(NewUserDTO user) {
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

    public User createUser(NewUserDTO newUserDTO) {
        validateName(newUserDTO);

        if (repository.findByUserName(newUserDTO.userName()).isPresent())
            throw new UserNameTakenException(newUserDTO.userName());

        User user = new User(newUserDTO.userName(), newUserDTO.firstName(), newUserDTO.lastName());
        return repository.save(user);
    }

    public User saveUser(String userName) {
        validateName(userName);

        return repository.save(getUserByUserName(userName));
    }
}