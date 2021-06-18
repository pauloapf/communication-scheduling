package com.desafio.scheduling.communication.dao.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.desafio.scheduling.communication.dao.CommunicationSchedulingDao;
import com.desafio.scheduling.communication.dao.domain.CommunicationScheduling;
import com.desafio.scheduling.communication.exception.NotFoundIdException;

import lombok.extern.slf4j.Slf4j;

/**
 * Class responsible for CRUD operation in MySQL
 * 
 * @author pauloapf
 *
 */
@Slf4j
@Component
public class CommunicationSchedulingDaoImpl implements CommunicationSchedulingDao{

	private CommunicationSchedulingRepository communicationSchedulingRepository;

	public CommunicationSchedulingDaoImpl(
			@Autowired CommunicationSchedulingRepository communicationSchedulingRepository) {
		this.communicationSchedulingRepository = communicationSchedulingRepository;
	}
	
	@Override
	public CommunicationScheduling create(CommunicationScheduling communicationScheduling) {
		log.debug("Realizando o insert na tabela COMMUNICATION_SCHEDULING");
		return communicationSchedulingRepository.save(communicationScheduling);
	}

	@Override
	public Optional<CommunicationScheduling> getById(Long id) {
		log.debug("Realizando a consulta na tabela COMMUNICATION_SCHEDULING");
		return communicationSchedulingRepository.findById(id);
	}

	@Override
	public void deleteById(Long id) throws NotFoundIdException {
		try {
			log.debug("Realizando o delete na tabela COMMUNICATION_SCHEDULING");
			communicationSchedulingRepository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new NotFoundIdException();
		}
	}

}
