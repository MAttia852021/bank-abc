package com.abcbank.accountservices.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {
	@ResponseBody
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String entityNotFoundHandler(EntityNotFoundException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(InsufficientFundsException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String insufficientFundsHandler(InsufficientFundsException ex) {
		return ex.getMessage();
	}

}
