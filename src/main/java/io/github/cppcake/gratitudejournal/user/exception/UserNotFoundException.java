package io.github.cppcake.gratitudejournal.user.exception;

/**
 * Exception thrown when the requested user is not found.
 * 
 * @author Afeef Neiroukh
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Public constructor of the UserNotFoundException class.
     * 
     * @param userName The user name of the missing user to mention in the Exception
     *                 message.
     */
    public UserNotFoundException(String userName) {
        super("Could not find the user \"" + userName + "\"");
    }
}