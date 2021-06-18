//package com.desafio.scheduling.communication.dao.impl;
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.DataAccessException;
//import org.springframework.test.context.TestPropertySource;
//
//import com.desafio.scheduling.communication.dao.CommunicationSchedulingDao;
//import com.desafio.scheduling.communication.dao.domain.CommunicationScheduling;
//import com.desafio.scheduling.communication.exception.NotFoundIdException;
//
//@SpringBootTest(classes = SpringBootH2Application.class)
//@ImportAutoConfiguration(classes = { })
//@TestPropertySource(properties = { 
//		"spring.profiles.active=dev"})
//public class CommunicationSchedulingDaoImplTest {
//
//	@Autowired
//	CommunicationSchedulingRepository notificationMessageRepository;
//
//	@Autowired
//	CommunicationSchedulingDao communicationSchedulingDao;
//	
//	@BeforeEach
//	public void clearDatabase() {
//		notificationMessageRepository.deleteAll();
//	}
//	
//	@DisplayName("Test create and get methods with success")
//	@Test()
//	public void testeCreate() throws DataAccessException, Exception {
//		
//		CommunicationScheduling communicationSchedulingId1 = communicationSchedulingBuild();
//		CommunicationScheduling communicationSchedulingId2 = communicationSchedulingBuild();
//		
//		communicationSchedulingId1 = communicationSchedulingDao.create(communicationSchedulingId1);
//		communicationSchedulingId2 = communicationSchedulingDao.create(communicationSchedulingId2);
//		
//		Optional<CommunicationScheduling> communicationSchedulingFind = communicationSchedulingDao.getById(communicationSchedulingId1.getId());
//
//		assertEquals(communicationSchedulingId1.toString(), communicationSchedulingFind.get().toString());
//		assertEquals(communicationSchedulingId1.getId()+1, communicationSchedulingId2.getId());
//		
//	}
//	
//	@DisplayName("Test delete is success")
//	@Test()
//	public void testeDelete() throws DataAccessException, Exception {
//		
//		CommunicationScheduling communicationSchedulingId1 = communicationSchedulingBuild();
//		
//		communicationSchedulingId1 = communicationSchedulingDao.create(communicationSchedulingId1);
//		
//		communicationSchedulingDao.deleteById(communicationSchedulingId1.getId());
//		
//		Optional<CommunicationScheduling> communicationSchedulingFind = communicationSchedulingDao.getById(communicationSchedulingId1.getId());
//
//		assertFalse(communicationSchedulingFind.isPresent());
//		
//	}
//	
//	@DisplayName("Test delete when id not found")
//	@Test()
//	public void testeDeleteNotFound() throws DataAccessException, Exception {
//		assertThrows(NotFoundIdException.class, () -> communicationSchedulingDao.deleteById(1L));
//	}	
//	
//	private CommunicationScheduling communicationSchedulingBuild() {
//		return new CommunicationScheduling(
//				LocalDateTime.now(),
//				LocalDateTime.now(), 
//				"11900000000", 
//				"XX@YY.com.br",
//				"1",
//				"message",
//				"1",
//				null);
//	}
//
//}
