package com.example.gratidude_journal.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/*
    Presentation Layer for User-API
*/
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{userName}")
    public EntityModel<User> getUser(@PathVariable String userName) {
        User user = userService.getUserByUserName(userName);

        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUser(userName)).withSelfRel(),
                linkTo(methodOn(UserController.class).updateUser(userName, null)).withRel("update"),
                linkTo(methodOn(UserController.class).createUser(null)).withRel("create"));
    }

    @DeleteMapping("/user/{userName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String userName) {
        userService.deleteUserByUserName(userName);
    }

    @PutMapping("/user/{userName}")
    public EntityModel<User> updateUser(@PathVariable String userName, @RequestBody UserUpdateDTO updatedUser) {
        User user = userService.updateUser(userName, updatedUser);

        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).createUser(null)).withRel("create"),
                linkTo(methodOn(UserController.class).updateUser(userName, null)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUser(userName)).withRel("get"));
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<User> createUser(@RequestBody UserDTO userDTO) {
        User user = userService.createUser(userDTO);

        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).createUser(null)).withSelfRel(),
                linkTo(methodOn(UserController.class).updateUser(user.getUserName(), null)).withRel("update"),
                linkTo(methodOn(UserController.class).getUser(user.getUserName())).withRel("get"));
    }
}