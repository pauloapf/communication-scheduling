package com.desafio.scheduling.communication.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.desafio.scheduling.communication.api.response.ResponseCodeValues;
import com.desafio.scheduling.communication.business.CommunicationSchedulingBuilder;
import com.desafio.scheduling.communication.business.CommunicationSchedulingRules;
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

	private CommunicationSchedulingRules communicationSchedulingRules;
	
	private CommunicationSchedulingBuilder communicationSchedulingBuilder;
	
	private CommunicationSchedulingDao communicationSchedulingDao;
	
	public CommunicationSchedulingServiceImpl(
			@Autowired CommunicationSchedulingBuilder communicationSchedulingBuilder,
			@Autowired CommunicationSchedulingDao communicationSchedulingDao,
			@Autowired CommunicationSchedulingRules communicationSchedulingRules
			) {
		this.communicationSchedulingBuilder = communicationSchedulingBuilder;
		this.communicationSchedulingDao = communicationSchedulingDao;
		this.communicationSchedulingRules = communicationSchedulingRules;
	}
	
	public ResponseEntity<SchedulingCreationResponse> create(SchedulingCreationRequest request, String xCorrelationID) {

		if(log.isDebugEnabled()) {
			log.debug("Executando serviço de CREATE para request {}", request);
		}
		CommunicationScheduling communicationScheduling = communicationSchedulingDao.create(
				communicationSchedulingBuilder.createCommunication(request));
		
		SchedulingCreationResponse response = communicationSchedulingBuilder.buildSchedulingCreationResponse(
        		communicationScheduling);
		
		log.debug("Executado serviço de CREATE");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@Override
	public ResponseEntity<Void> delete(Integer id, String xCorrelationID) {
		try {
			log.debug("Executando serviço de DELETE para id {}", id);
			Optional<CommunicationScheduling> communicationScheduling = communicationSchedulingDao.getById(Long.valueOf(id));
			if(communicationScheduling.isPresent()) {
				if(communicationSchedulingRules.isCommunicationAvailableToBeDeleted(
						communicationScheduling.get().getScheduleDate(),
						communicationScheduling.get().getStatus())){
					communicationSchedulingDao.deleteById(Long.valueOf(id));
				}else {
					throw new BusinessException(ResponseCodeValues.DELETE_UNAVAILABLE_DUE_TO_DATE_OR_STATUS, HttpStatus.FORBIDDEN, "Data de agendamento é menor ou igual a data de hoje ou status diferente de 1 - agendado");
				}
			}else {
				throw new BusinessException(ResponseCodeValues.ID_NOT_FOUND, HttpStatus.NOT_FOUND, "Nenhum agendamento encontrado");
			}
		} catch (NotFoundIdException e) {
			log.info("Nenhum agendamento encontrado para o id {}", id);
			throw new BusinessException(ResponseCodeValues.ID_NOT_FOUND, HttpStatus.NOT_FOUND, "Nenhum agendamento encontrado");
		}
		log.debug("Executado serviço de DELETE");
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<SchedulingStatusResponse> get(Integer id, String xCorrelationID) {
		log.debug("Executando serviço de GET para id {}", id);
		SchedulingStatusResponse response = null;
		
		Optional<CommunicationScheduling> communicationScheduling = communicationSchedulingDao.getById(Long.valueOf(id));
		
		if(communicationScheduling.isPresent()) {
			response = communicationSchedulingBuilder.buildSchedulingStatusResponse(
	        		communicationScheduling.get());
		}else {
			throw new BusinessException(ResponseCodeValues.ID_NOT_FOUND, HttpStatus.NOT_FOUND, "Nenhum agendamento encontrado para o id informado");
		}
		
		log.debug("Executado serviço de GET");
        return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
