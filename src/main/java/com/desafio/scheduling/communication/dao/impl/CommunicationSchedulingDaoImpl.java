package com.desafio.scheduling.communication.dao.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.desafio.scheduling.communication.dao.CommunicationSchedulingDao;
import com.desafio.scheduling.communication.dao.domain.CommunicationScheduling;
import com.desafio.scheduling.communication.exception.NotFoundIdException;

/**
 * Class responsible for CRUD operation in MySQL
 * 
 * @author pauloapf
 *
 */
@Component
public class CommunicationSchedulingDaoImpl implements CommunicationSchedulingDao{

	private CommunicationSchedulingRepository communicationSchedulingRepository;

	public CommunicationSchedulingDaoImpl(
			@Autowired CommunicationSchedulingRepository communicationSchedulingRepository) {
		this.communicationSchedulingRepository = communicationSchedulingRepository;
	}
	
	@Override
	public CommunicationScheduling create(CommunicationScheduling communicationScheduling) {
		return communicationSchedulingRepository.save(communicationScheduling);
	}

	@Override
	public Optional<CommunicationScheduling> getById(Long id) {
		return communicationSchedulingRepository.findById(id);
	}

	@Override
	public void deleteById(Long id) throws NotFoundIdException {
		try {
			communicationSchedulingRepository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new NotFoundIdException();
		}
	}

}
