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

/*
    Presentation Layer for User-API
*/
@RestController
public class UserController {
    private final UserService userService;

    private final ReturnUserDTOModelAssembler returnUserDTOAssembler;

    public UserController(UserService userService, ReturnUserDTOModelAssembler returnUserDTOAssembler) {
        this.userService = userService;
        this.returnUserDTOAssembler = returnUserDTOAssembler;
    }

    @GetMapping("/user/{userName}")
    public EntityModel<ReturnUserDTO> getUser(@PathVariable String userName) {
        User user = userService.getUserByUserName(userName);

        return returnUserDTOAssembler.toModel(user.toReturnUserDTO());
    }

    @DeleteMapping("/user/{userName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable String userName) {
        userService.deleteUserByUserName(userName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/user/{userName}")
    public EntityModel<ReturnUserDTO> updateUser(@PathVariable String userName,
            @RequestBody UpdateUserDTO updateUserDTO) {
        User user = userService.updateUser(userName, updateUserDTO);

        return returnUserDTOAssembler.toModel(user.toReturnUserDTO());
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<ReturnUserDTO> createUser(@RequestBody NewUserDTO newUserDTO) {
        User user = userService.createUser(newUserDTO);

        return returnUserDTOAssembler.toModel(user.toReturnUserDTO());
    }
}