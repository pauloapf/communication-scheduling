package com.desafio.scheduling.communication.dao.domain;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CommunicationSchedulingOld {

	
	
	public CommunicationSchedulingOld(Timestamp creationDate, Timestamp scheduleDate, String phoneNumber,
			String email, String sendType, String message, String status, String statusDescription) {
		super();
		this.id = id;
		this.creationDate = creationDate;
		this.scheduleDate = scheduleDate;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.sendType = sendType;
		this.message = message;
		this.status = status;
		this.statusDescription = statusDescription;
	}

	private Long id;
	
	private Timestamp creationDate;

	private Timestamp scheduleDate;

	private String phoneNumber;

	private String email;

	private String sendType;

	private String message;

	private String status;

	private String statusDescription;
	
}
