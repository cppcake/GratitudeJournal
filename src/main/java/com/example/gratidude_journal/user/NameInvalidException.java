package com.example.gratidude_journal.user;

class NameInvalidException extends RuntimeException {

    NameInvalidException(String name) {
        super("The name \"" + name + "\" does not follow the naming rules.");
    }
}