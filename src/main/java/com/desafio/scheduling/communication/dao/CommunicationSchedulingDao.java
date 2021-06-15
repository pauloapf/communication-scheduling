package com.desafio.scheduling.communication.dao;

import java.util.Optional;

import com.desafio.scheduling.communication.dao.domain.CommunicationScheduling;
import com.desafio.scheduling.communication.exception.NotFoundIdException;
import com.desafio.scheduling.communication.exception.DaoGenericException;

/**
 * Interface responsible for CRUD operations for Communication Scheduling Service
 *  
 * @author pauloapf
 *
 */
public interface CommunicationSchedulingDao {

	/**
	 * Creates communication scheduling entity
	 * @param communicationScheduling Communication scheduling
	 * @return communicationScheduling update with its id
	 */
	public CommunicationScheduling create(CommunicationScheduling communicationScheduling);	

	/**
	 * Recovers CommunicationScheduling by id
	 * @param id CommunicationScheduling id
	 * @return Optional CommunicationScheduling
	 */
	public Optional<CommunicationScheduling> getById(Long id);	

	/**
	 * Removes CommunicationScheduling by id
	 * @param id CommunicationScheduling id
	 * @throws NotFoundIdException No id is found
	 */
	public void deleteById(Long id) throws NotFoundIdException;	
	
}
