package com.desafio.scheduling.communication.dao.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafio.scheduling.communication.dao.domain.CommunicationScheduling;

/**
 * Jpa repository for COMMUNICATION_SCHEDULING table
 * 
 * @author pauloapf
 *
 */
@Repository
public interface CommunicationSchedulingRepository extends JpaRepository<CommunicationScheduling, Long>{

}
