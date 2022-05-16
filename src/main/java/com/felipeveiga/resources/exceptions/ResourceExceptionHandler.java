package com.felipeveiga.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.felipeveiga.services.exceptions.DataIntegrityViolationException;
import com.felipeveiga.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest http){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(status.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityViolationException e, HttpServletRequest http){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(status.value(), e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(status).body(err);
	}
	
}