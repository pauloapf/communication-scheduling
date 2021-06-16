package com.desafio.scheduling.communication.api.response;


import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.desafio.scheduling.communication.exception.BusinessException;
import com.desafio.scheduling.communication.model.ResponseError;

@Component
public class ApiErrorResponseBuilder {

    protected ResponseEntity<ResponseError> buildResponseError(
    		HttpStatus status,
    		String internalCode ,
    		String message,
    		String requestURI,
    		Exception e) {
    	
        ResponseError error = new ResponseError();
        error.setTimestamp(System.currentTimeMillis());
        error.setStatus(Integer.parseInt(status.toString()));
        error.setError(status.getReasonPhrase());
        error.setErrors(new ArrayList<>());
        error.setException(e.getClass().getSimpleName());
        error.setPath(requestURI);
        error.setInternalCode(internalCode);

        return new ResponseEntity<>(error, status);
    }
	
	
}
