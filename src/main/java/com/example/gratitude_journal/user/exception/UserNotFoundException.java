package com.example.gratitude_journal.user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userName) {
        super("Could not find the user \"" + userName + "\"");
    }
}