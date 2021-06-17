package com.desafio.scheduling.communication.business;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.desafio.scheduling.communication.dao.domain.CommunicationScheduling;
import com.desafio.scheduling.communication.model.SchedulingCreationRequest;
import com.desafio.scheduling.communication.model.SchedulingCreationResponse;
import com.desafio.scheduling.communication.model.SchedulingCreationResponse.StatusEnum;
import com.desafio.scheduling.communication.model.SchedulingStatusResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommunicationSchedulingBuilder {

	private CommunicationSchedulingRules communicationSchedulingRules;

	public CommunicationSchedulingBuilder(
			@Autowired CommunicationSchedulingRules communicationSchedulingRules
			) {
		this.communicationSchedulingRules =communicationSchedulingRules;
	}

	public CommunicationScheduling createCommunication(SchedulingCreationRequest request) {
		
		log.debug("Building CommunicationScheduling for creating");
		
		communicationSchedulingRules.validateRecipientForSendType(request.getPhoneNumber(), request.getEmail(), request.getSendType());
		
		CommunicationScheduling communicationScheduling = new CommunicationScheduling(
					LocalDateTime.now(), 
					communicationSchedulingRules.getValidScheduleDate(request.getScheduleDate(), LocalDate.now()), 
					request.getPhoneNumber(), 
					request.getEmail(), 
					request.getSendType().toString(), 
					request.getMessage(), 
					CommunicationScheduling.STATUS_AGENDADO, 
					CommunicationScheduling.STATUS_DESCRIPTION_AGENDADO);
		
		return communicationScheduling;
	}
	

	
	public SchedulingCreationResponse buildSchedulingCreationResponse(CommunicationScheduling communicationScheduling) {
		SchedulingCreationResponse response = new SchedulingCreationResponse();
		response.setEmail(communicationScheduling.getEmail());
		response.setId(communicationScheduling.getId().intValue());
		response.setMessage(communicationScheduling.getMessage());
		response.setPhoneNumber(communicationScheduling.getPhoneNumber());
		response.setScheduleDate(communicationSchedulingRules.getScheduleLocalDateTimeToString(communicationScheduling.getScheduleDate()));
		response.setSendType(communicationScheduling.getSendType());
		response.setStatus(SchedulingCreationResponse.StatusEnum.fromValue(communicationScheduling.getStatus()));
		response.setStatusDescription(communicationScheduling.getStatusDescription());
		return response;
	}


	public SchedulingStatusResponse buildSchedulingStatusResponse(CommunicationScheduling communicationScheduling) {
		SchedulingStatusResponse response = new SchedulingStatusResponse();
		response.setEmail(communicationScheduling.getEmail());
		response.setId(communicationScheduling.getId().intValue());
		response.setMessage(communicationScheduling.getMessage());
		response.setPhoneNumber(communicationScheduling.getPhoneNumber());
		response.setScheduleDate(communicationSchedulingRules.getScheduleLocalDateTimeToString(communicationScheduling.getScheduleDate()));
		response.setSendType(communicationScheduling.getSendType());
		response.setStatus(SchedulingStatusResponse.StatusEnum.fromValue(communicationScheduling.getStatus()));
		response.setStatusDescription(communicationScheduling.getStatusDescription());
		return response;
	}
	
}
