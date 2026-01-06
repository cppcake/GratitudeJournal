package com.example.gratidude_journal.user;

class UserNameTakenException extends RuntimeException {

    UserNameTakenException(String userName) {
        super("The userName \"" + userName + "\" is already taken.");
    }
}