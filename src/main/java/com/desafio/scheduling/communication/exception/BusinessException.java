package com.desafio.scheduling.communication.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -6494914344711611593L;

	private String internalCode;

	private HttpStatus status;

	public BusinessException(String internalCode, HttpStatus status, String message, Throwable cause) {
		super(message, cause);
		this.internalCode = internalCode;
	}

	public BusinessException(String internalCode, HttpStatus status, String message) {
		super(message);
		this.internalCode = internalCode;
	}

}

