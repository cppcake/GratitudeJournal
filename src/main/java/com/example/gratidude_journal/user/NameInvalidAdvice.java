package com.example.gratidude_journal.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class NameInvalidAdvice {

	@ExceptionHandler(NameInvalidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String nameInvalidHandler(NameInvalidException ex) {
		return ex.getMessage();
	}
}