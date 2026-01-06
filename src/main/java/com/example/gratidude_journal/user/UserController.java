package com.example.gratidude_journal.user;

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
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/user/{userName}")
    public EntityModel<User> getUserByUserName(@PathVariable String userName) {
        if (!User.validateName(userName))
            throw new NameInvalidException(userName);

        User user = repository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));

        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserByUserName(userName)).withSelfRel(),
                linkTo(methodOn(UserController.class).deleteUserByUserName(userName)).withRel("delete"),
                linkTo(methodOn(UserController.class).updateUser(null)).withRel("update"),
                linkTo(methodOn(UserController.class).createUser(null)).withRel("create"));
    }

    @DeleteMapping("/user/{userName}")
    public ResponseEntity<RepresentationModel<?>> deleteUserByUserName(@PathVariable String userName) {
        if (!User.validateName(userName))
            throw new NameInvalidException(userName);

        repository.findByUserName(userName)
                .ifPresentOrElse(
                        foundUser -> repository.deleteById(foundUser.getUserId()),
                        () -> {
                            throw new UserNotFoundException(userName);
                        });

        RepresentationModel<?> model = new RepresentationModel<>();
        model.add(linkTo(methodOn(UserController.class).deleteUserByUserName(userName)).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).createUser(null)).withRel("create"));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<User> createUser(@RequestBody User newUser) {
        if (repository.findByUserName(newUser.getUserName()).isPresent())
            throw new UserNameTakenException(newUser.getUserName());

        User user = repository.save(newUser);

        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).createUser(null)).withSelfRel(),
                linkTo(methodOn(UserController.class).deleteUserByUserName(user.getUserName())).withRel("delete"),
                linkTo(methodOn(UserController.class).updateUser(null)).withRel("update"),
                linkTo(methodOn(UserController.class).getUserByUserName(user.getUserName())).withRel("get"));
    }

    @PutMapping("/user")
    public EntityModel<User> updateUser(@RequestBody User updatedUser) {
        User user = repository.findByUserName(updatedUser.getUserName())
                .map(foundUser -> {
                    foundUser.setFirstName(updatedUser.getFirstName());
                    foundUser.setLastName(updatedUser.getLastName());
                    return repository.save(foundUser);
                })
                .orElseThrow(() -> new UserNotFoundException(updatedUser.getUserName()));

        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).createUser(null)).withRel("create"),
                linkTo(methodOn(UserController.class).deleteUserByUserName(user.getUserName())).withRel("delete"),
                linkTo(methodOn(UserController.class).updateUser(null)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUserByUserName(user.getUserName())).withRel("get"));
    }
}