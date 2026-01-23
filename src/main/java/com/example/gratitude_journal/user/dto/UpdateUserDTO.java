package com.example.gratitude_journal.user.dto;

/**
 * DTO sent by clients as JSON when updating an existing User using the
 * PUT-User request. This way they do not have to unnecessarily fill the
 * immutable User fields such as userId, userName and journal and at the same
 * time the response is easily deserializable.
 * 
 * @param firstName The new first name to apply on the user. Is validated
 *                  according to
 *                  {@link com.example.gratitude_journal.user.User#validateName(String)}.
 * @param lastName  The new last name to apply on the user. Is validated
 *                  according to
 *                  {@link com.example.gratitude_journal.user.User#validateName(String)}.
 * 
 * @author Afeef Neiroukh
 */
public record UpdateUserDTO(
                String firstName,
                String lastName) {
}