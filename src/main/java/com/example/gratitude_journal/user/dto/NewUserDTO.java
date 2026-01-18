package com.example.gratitude_journal.user.dto;

/*
    Part of the Presentation Layer for User-API
*/
public record NewUserDTO(
        String userName,
        String firstName,
        String lastName) {
}