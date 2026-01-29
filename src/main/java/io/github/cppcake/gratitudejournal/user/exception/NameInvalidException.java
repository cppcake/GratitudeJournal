package io.github.cppcake.gratitudejournal.user.exception;

/**
 * Exception thrown when an invalid name is provided.
 * 
 * @author Afeef Neiroukh
 */
public class NameInvalidException extends RuntimeException {
    /**
     * Public constructor of the NameInvalidException class.
     * 
     * @param name The invalid name to mention in the Exception message.
     */
    public NameInvalidException(String name) {
        super("The name \"" + name + "\" does not follow the naming rules.");
    }
}