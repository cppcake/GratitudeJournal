package com.example.gratitude_journal.user.dto;

/**
 * DTO sent by clients as JSON when creating a new User using the
 * POST-User request. This way they do not need to fill unnecessary User fields
 * such as userId and journal and at the same time the response is easily
 * deserializable.
 * 
 * @param userName  The user name of the user to create. Is validated
 *                  according to
 *                  {@link com.example.gratitude_journal.user.User#validateName(String)}.
 * @param firstName The first name of the user to create. Is validated
 *                  according to
 *                  {@link com.example.gratitude_journal.user.User#validateName(String)}.
 * @param lastName  The last name of the user to create. Is validated according
 *                  to
 *                  {@link com.example.gratitude_journal.user.User#validateName(String)}.
 * 
 * @author Afeef Neiroukh
 */
public record NewUserDTO(
        String userName,
        String firstName,
        String lastName) {
}