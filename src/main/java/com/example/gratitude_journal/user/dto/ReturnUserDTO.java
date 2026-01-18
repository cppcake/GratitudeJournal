package com.example.gratitude_journal.user.dto;

/*
    Part of the Presentation Layer for User-API
*/
public record ReturnUserDTO(
        Long userId,
        String userName,
        String firstName,
        String lastName) {
}