package com.desafio.scheduling.communication.api;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.desafio.scheduling.communication.model.SchedulingCreationRequest;
import com.desafio.scheduling.communication.model.SchedulingCreationResponse;
import com.desafio.scheduling.communication.model.SchedulingStatusResponse;
import com.desafio.scheduling.communication.service.CommunicationSchedulingService;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommunicationDelegateService implements CommunicationApiDelegate{

	@Autowired
	private CommunicationSchedulingService communicationSchedulingService;
	
	public ResponseEntity<Void> communicationIdDelete(Integer id,
			String xCorrelationID) {
		xCorrelationID = defineXCorrelationIdAndMDC(xCorrelationID);
        log.info("API Request = {} with xCorrelationID {}", id, xCorrelationID);
        ResponseEntity<Void> response = communicationSchedulingService.delete(id,xCorrelationID);
        log.info("API Response = {}", new Gson().toJson(response));
        return response;
	}

	public ResponseEntity<SchedulingStatusResponse> communicationIdGet(Integer id,
			String xCorrelationID) {
		xCorrelationID = defineXCorrelationIdAndMDC(xCorrelationID);
        log.info("API Request = {} with xCorrelationID {}", id, xCorrelationID);
        ResponseEntity<SchedulingStatusResponse> response = communicationSchedulingService.get(id,xCorrelationID);
        log.info("API Response = {}", new Gson().toJson(response));
        return response;
	}

	public ResponseEntity<SchedulingCreationResponse> communicationPost(SchedulingCreationRequest body,
			String xCorrelationID) {
		xCorrelationID = defineXCorrelationIdAndMDC(xCorrelationID);
        log.info("API Request = {} with xCorrelationID {}", new Gson().toJson(body), xCorrelationID);
        ResponseEntity<SchedulingCreationResponse> response = communicationSchedulingService.create(body,xCorrelationID);
        log.info("API Response = {}", new Gson().toJson(response));
        return response;
	}

	private String defineXCorrelationIdAndMDC(String xCorrelationID) {
		MDC.clear();
		if(!StringUtils.hasText(xCorrelationID)) {
			xCorrelationID = UUID.randomUUID().toString();
		}
		MDC.put("TID", xCorrelationID);
		return xCorrelationID;
	}

}
