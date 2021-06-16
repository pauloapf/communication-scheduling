package com.desafio.scheduling.communication.api.response;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.desafio.scheduling.communication.exception.BusinessException;
import com.desafio.scheduling.communication.model.ResponseError;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

	private ApiErrorResponseBuilder apiErrorResponseBuilder;

	public ApiExceptionHandler(
			@Autowired ApiErrorResponseBuilder apiErrorResponseBuilder
			) {
		this.apiErrorResponseBuilder = apiErrorResponseBuilder;
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ResponseError> businessException(BusinessException e, HttpServletRequest request) {
		log.error(e.getMessage(), e);
		return apiErrorResponseBuilder.buildResponseError(e.getStatus(), e.getInternalCode(), e.getMessage(), request.getRequestURI(), e);
	}
	
	@ExceptionHandler({InvalidFormatException.class, HttpMessageNotReadableException.class, MethodArgumentNotValidException.class, ServletRequestBindingException.class, MethodArgumentTypeMismatchException.class,
			MissingServletRequestParameterException.class, HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class })
	public ResponseEntity<ResponseError> springExceptions(Exception e, HttpServletRequest request) throws Exception {
		log.error(e.getMessage(), e);
		throw e;
	}

	//TODO
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity springExceptions(ConstraintViolationException e, HttpServletRequest request)
			throws Exception {
		Map <String, String> responseError = new HashMap<>();
		for (ConstraintViolation violation : e.getConstraintViolations()) {
			responseError.put("timestamp", String.valueOf(System.currentTimeMillis()));
			responseError.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
			responseError.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());		
			responseError.put("exception", e.getClass().getName());
			responseError.put("message", violation.getPropertyPath().toString() + "=" + violation.getInvalidValue() + " " + violation.getMessage() );
			responseError.put("path", request.getRequestURI().toString());
		}		
		return new ResponseEntity<Map>(responseError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseError> businessException(Exception e, HttpServletRequest request) {
		log.error(e.getMessage(), e);
		return apiErrorResponseBuilder.buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR, "", e.getMessage(), request.getRequestURI(), e);
	}
	
}
