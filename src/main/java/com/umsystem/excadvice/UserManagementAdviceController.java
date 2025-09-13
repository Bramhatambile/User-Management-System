package com.umsystem.excadvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserManagementAdviceController {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleArgumentException(IllegalArgumentException exp){
		return new ResponseEntity<String>
		("Problem ::"+exp.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleAllExcaption(Exception e){
		return new ResponseEntity<String>
		("Problem::"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
