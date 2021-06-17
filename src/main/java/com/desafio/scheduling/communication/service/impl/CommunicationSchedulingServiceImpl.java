package com.desafio.scheduling.communication.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.desafio.scheduling.communication.api.response.ResponseCodeValues;
import com.desafio.scheduling.communication.business.CommunicationSchedulingBuilder;
import com.desafio.scheduling.communication.dao.CommunicationSchedulingDao;
import com.desafio.scheduling.communication.dao.domain.CommunicationScheduling;
import com.desafio.scheduling.communication.exception.BusinessException;
import com.desafio.scheduling.communication.exception.NotFoundIdException;
import com.desafio.scheduling.communication.model.SchedulingCreationRequest;
import com.desafio.scheduling.communication.model.SchedulingCreationResponse;
import com.desafio.scheduling.communication.model.SchedulingStatusResponse;
import com.desafio.scheduling.communication.service.CommunicationSchedulingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommunicationSchedulingServiceImpl implements CommunicationSchedulingService{

	private CommunicationSchedulingBuilder communicationSchedulingBuilder;
	
	private CommunicationSchedulingDao communicationSchedulingDao;
	
	public CommunicationSchedulingServiceImpl(
			@Autowired CommunicationSchedulingBuilder communicationSchedulingBuilder,
			@Autowired CommunicationSchedulingDao communicationSchedulingDao
			) {
		this.communicationSchedulingBuilder = communicationSchedulingBuilder;
		this.communicationSchedulingDao = communicationSchedulingDao;
	}
	
	public ResponseEntity<SchedulingCreationResponse> create(SchedulingCreationRequest request, String xCorrelationID) {

		CommunicationScheduling communicationScheduling = communicationSchedulingDao.create(
				communicationSchedulingBuilder.createCommunication(request));
		
        return new ResponseEntity<>(communicationSchedulingBuilder.buildSchedulingCreationResponse(
        		communicationScheduling), HttpStatus.CREATED);

	}
	
	@Override
	public ResponseEntity<Void> delete(Integer id, String xCorrelationID) {
		try {
			communicationSchedulingDao.deleteById(Long.valueOf(id));
		} catch (NotFoundIdException e) {
			log.info("Nenhum agendamento encontrado para o id {}", id);
			throw new BusinessException(ResponseCodeValues.ID_NOT_FOUND, HttpStatus.NOT_FOUND, "Nenhum agendamento encontrado");
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<SchedulingStatusResponse> get(Integer id, String xCorrelationID) {
		Optional<CommunicationScheduling> communicationScheduling = communicationSchedulingDao.getById(Long.valueOf(id));
		if(communicationScheduling.isPresent()) {
	        return new ResponseEntity<>(communicationSchedulingBuilder.buildSchedulingStatusResponse(
	        		communicationScheduling.get()), HttpStatus.OK);
		}else {
			throw new BusinessException(ResponseCodeValues.ID_NOT_FOUND, HttpStatus.NOT_FOUND, "Nenhum agendamento encontrado para o id informado");
		}
	}

}
