package io.github.cppcake.gratitudejournal.user.dto;

/**
 * DTO returned to clients as JSON when they request details about a
 * User using the GET-User request. This gives more control about what the
 * clients receive. The journal of a user for example is not included in the
 * response, reducing the response size.
 * 
 * @param userId    The userId (primary key) of the user.
 * @param userName  The unique user name of the user.
 * @param firstName The first name of the user.
 * @param lastName  The last name of the user.
 * 
 * @author Afeef Neiroukh
 */
public record ReturnUserDTO(
        Long userId,
        String userName,
        String firstName,
        String lastName) {
}