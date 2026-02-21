package com.pgmanagement.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

   
	private static final long serialVersionUID = 1L;

	public BusinessException(String message) {
        super(message);
    }
}
