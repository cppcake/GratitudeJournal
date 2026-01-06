package com.example.gratidude_journal.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class UserNameTakenAdvice {

	@ExceptionHandler(UserNameTakenException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	String userNameTakenHandler(UserNameTakenException ex) {
		return ex.getMessage();
	}
}