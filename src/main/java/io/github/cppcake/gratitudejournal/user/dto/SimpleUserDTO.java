package io.github.cppcake.gratitudejournal.user.dto;

/**
 * DTO sent by clients as JSON when creating or updating a User using the
 * POST- or PUT-User request. This way they do not have to unnecessarily fill
 * automatically generated / immutable User fields such as userId and
 * journal and at the same time the response is easily deserializable.
 * 
 * @param firstName The first name to apply on the user. Is validated according to
 *                  {@link io.github.cppcake.gratitudejournal.user.User#validateName(String)}.
 * @param lastName  The last name to apply on the user. Is validated according to
 *                  {@link io.github.cppcake.gratitudejournal.user.User#validateName(String)}.
 * 
 * @author Afeef Neiroukh
 */
public record SimpleUserDTO(
                String firstName,
                String lastName) {
}