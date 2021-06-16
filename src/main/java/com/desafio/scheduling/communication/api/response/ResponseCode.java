package com.desafio.scheduling.communication.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
public class ResponseCode {

	private String extCode;

	private String internalCode;

	private Integer status;

	private String message;

	public ResponseCode(String extCode) {
		super();
		this.extCode = extCode;
	}

}
