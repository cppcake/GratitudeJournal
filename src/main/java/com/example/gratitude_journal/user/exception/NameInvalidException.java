package com.example.gratitude_journal.user.exception;

public class NameInvalidException extends RuntimeException {

    public NameInvalidException(String name) {
        super("The name \"" + name + "\" does not follow the naming rules.");
    }
}