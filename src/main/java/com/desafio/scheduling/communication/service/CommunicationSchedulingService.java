package com.desafio.scheduling.communication.service;

import org.springframework.http.ResponseEntity;

import com.desafio.scheduling.communication.model.SchedulingCreationRequest;
import com.desafio.scheduling.communication.model.SchedulingCreationResponse;
import com.desafio.scheduling.communication.model.SchedulingStatusResponse;

public interface CommunicationSchedulingService {

	public ResponseEntity<SchedulingCreationResponse> create(SchedulingCreationRequest request, String xCorrelationID);

	public ResponseEntity<Void> delete(Integer id, String xCorrelationID);

	public ResponseEntity<SchedulingStatusResponse> get(Integer id, String xCorrelationID);

}
