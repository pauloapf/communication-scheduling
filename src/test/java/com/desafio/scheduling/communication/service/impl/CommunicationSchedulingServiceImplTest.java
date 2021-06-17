package com.desafio.scheduling.communication.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
import com.desafio.scheduling.communication.model.SendTypeEnum;
import com.desafio.scheduling.communication.service.CommunicationSchedulingService;
import com.desafio.scheduling.communication.util.Util;

@TestInstance(Lifecycle.PER_CLASS)
public class CommunicationSchedulingServiceImplTest {

	private static final String SEND_TYPE_1 = "1";

	private static final String PHONE_NUMBER = "phoneNumber";

	private static final String MESSAGE = "message";

	private static final String EMAIL = "email";
	
	private String validScheduleDate = null;
	
	private LocalDateTime scheduleDateTime = null;

	private CommunicationSchedulingService communicationSchedulingService;
	
	private LocalDate currentDate;
	
	private LocalDateTime currentLocalDateTime;
	
	@Mock
	private CommunicationSchedulingDao communicationSchedulingDao;

	private Util communicationSchedulingUtil;
	
	
	@BeforeAll
	public void setup() {
		MockitoAnnotations.openMocks(this);
		communicationSchedulingUtil = new Util("yyyy-MM-dd'T'HH");
		CommunicationSchedulingRules communicationSchedulingRules = new CommunicationSchedulingRules(communicationSchedulingUtil);
		CommunicationSchedulingBuilder communicationSchedulingBuilder = new CommunicationSchedulingBuilder(
				communicationSchedulingRules);
		communicationSchedulingService = new CommunicationSchedulingServiceImpl(
				communicationSchedulingBuilder,
				communicationSchedulingDao,
				communicationSchedulingRules);
		scheduleDateTime = LocalDate.now().plusDays(1).atTime(23, 59);
		validScheduleDate = communicationSchedulingUtil.localDateTimeToApi(scheduleDateTime);
		currentDate = LocalDate.now();
		currentLocalDateTime = LocalDateTime.now();
	}
	
	@BeforeEach
	public void beforeEach() {
		Mockito.reset(communicationSchedulingDao);
	}
	
	@DisplayName("Create a scheduling communication")
	@Test
	public void createCommunicationScheduling() {
		SchedulingCreationRequest request = commonSchedulingCreationRequest();
		Mockito.when(communicationSchedulingDao.create(
				argThat(new CommunicationSchedulingAllArgMatcher(request))))
		.thenReturn(commonCommunicationScheduling(request));
		
		ResponseEntity<SchedulingCreationResponse> resp = communicationSchedulingService.create(request, "xCorrelationID");
		
		assertEquals(HttpStatus.CREATED, resp.getStatusCode());
		assertEquals(request.getEmail(), resp.getBody().getEmail());
		assertEquals(request.getMessage(), resp.getBody().getMessage());
		assertEquals(request.getPhoneNumber(), resp.getBody().getPhoneNumber());
		assertEquals(request.getScheduleDate(), resp.getBody().getScheduleDate());
		assertEquals(request.getSendType().toString(), resp.getBody().getSendType());
		assertEquals(1, resp.getBody().getId());
		assertEquals(CommunicationScheduling.STATUS_AGENDADO, resp.getBody().getStatus().toString());
		assertEquals(CommunicationScheduling.STATUS_DESCRIPTION_AGENDADO, resp.getBody().getStatusDescription());
	}
	
	@DisplayName("Get a scheduling communication")
	@Test
	public void getCommunicationScheduling() {
		Long id = 1L;
		
		Mockito.when(communicationSchedulingDao.getById(id))
		.thenReturn(commonCommunicationScheduling());
		
		ResponseEntity<SchedulingStatusResponse> resp = communicationSchedulingService.get(id.intValue(), "xCorrelationID");
		
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals(EMAIL, resp.getBody().getEmail());
		assertEquals(MESSAGE, resp.getBody().getMessage());
		assertEquals(PHONE_NUMBER, resp.getBody().getPhoneNumber());
		assertEquals(validScheduleDate, resp.getBody().getScheduleDate());
		assertEquals(SEND_TYPE_1, resp.getBody().getSendType());
		assertEquals(1, resp.getBody().getId());
		assertEquals(CommunicationScheduling.STATUS_AGENDADO, resp.getBody().getStatus().toString());
		assertEquals(CommunicationScheduling.STATUS_DESCRIPTION_AGENDADO, resp.getBody().getStatusDescription());
	}
	
	@DisplayName("Try getting a scheduling communication")
	@Test
	public void getCommunicationSchedulingNotFound() {
		Long id = 1L;
		
		Mockito.when(communicationSchedulingDao.getById(id))
		.thenReturn(Optional.empty());
		
		BusinessException be = assertThrows(
				BusinessException.class,()->
				communicationSchedulingService.get(id.intValue(), "xCorrelationID"));
		
		assertEquals(HttpStatus.NOT_FOUND, be.getStatus());
		assertEquals(ResponseCodeValues.ID_NOT_FOUND, be.getInternalCode());
	}	
	
	@DisplayName("Delete a scheduling communication")
	@Test
	public void deleteCommunicationScheduling() throws NotFoundIdException {
		Long id = 1L;

		Mockito.when(communicationSchedulingDao.getById(id))
			.thenReturn(commonCommunicationScheduling());
		doNothing().when(communicationSchedulingDao).deleteById(id);

		ResponseEntity<Void> resp = communicationSchedulingService.delete(id.intValue(), "xCorrelationID");
		
		verify(communicationSchedulingDao, times(1)).deleteById(id);
		assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
	}
	
	@DisplayName("Try deleting a scheduling communication not exists")
	@Test
	public void deleteCommunicationSchedulingNotExists() throws NotFoundIdException {
		Long id = 1L;
		
		Mockito.when(communicationSchedulingDao.getById(id))
		.thenReturn(commonCommunicationScheduling());
		doThrow(NotFoundIdException.class).when(communicationSchedulingDao).deleteById(id);
		
		BusinessException be = assertThrows(BusinessException.class,()->communicationSchedulingService.delete(id.intValue(), "xCorrelationID"));
		assertEquals(ResponseCodeValues.ID_NOT_FOUND, be.getInternalCode());
		assertEquals(HttpStatus.NOT_FOUND, be.getStatus());
		
	}
	
	@DisplayName("Try deleting a scheduling communication but it has already sent")
	@Test
	public void deleteCommunicationSchedulingForbidden() throws NotFoundIdException {
		Long id = 1L;
		
		Mockito.when(communicationSchedulingDao.getById(id))
		.thenReturn(commonCommunicationScheduling(CommunicationScheduling.STATUS_ENVIADO));
		
		BusinessException be = assertThrows(BusinessException.class,()->communicationSchedulingService.delete(id.intValue(), "xCorrelationID"));
		assertEquals(ResponseCodeValues.DELETE_UNAVAILABLE_DUE_TO_DATE_OR_STATUS, be.getInternalCode());
		assertEquals(HttpStatus.FORBIDDEN, be.getStatus());
		
	}
	
	private CommunicationScheduling commonCommunicationScheduling(SchedulingCreationRequest req) {
		CommunicationScheduling resp = new CommunicationScheduling();
		resp.setEmail(req.getEmail());
		resp.setMessage(req.getMessage());
		resp.setPhoneNumber(req.getPhoneNumber());
		resp.setScheduleDate(scheduleDateTime);
		resp.setSendType(req.getSendType().toString());
		resp.setCreationDate(currentLocalDateTime);
		resp.setStatus(CommunicationScheduling.STATUS_AGENDADO);
		resp.setStatusDescription(CommunicationScheduling.STATUS_DESCRIPTION_AGENDADO);
		resp.setId(1L);
		return resp;
	}

	private Optional<CommunicationScheduling> commonCommunicationScheduling() {
		return commonCommunicationScheduling(CommunicationScheduling.STATUS_AGENDADO);
	}

	private Optional<CommunicationScheduling> commonCommunicationScheduling(String status) {
		CommunicationScheduling resp = new CommunicationScheduling();
		resp.setEmail(EMAIL);
		resp.setMessage(MESSAGE);
		resp.setPhoneNumber(PHONE_NUMBER);
		resp.setScheduleDate(scheduleDateTime);
		resp.setSendType(SEND_TYPE_1);
		resp.setCreationDate(currentLocalDateTime);
		resp.setStatus(status);
		resp.setStatusDescription(CommunicationScheduling.STATUS_DESCRIPTION_AGENDADO);
		resp.setId(1L);
		return Optional.of(resp);
	}

	private SchedulingCreationRequest commonSchedulingCreationRequest() {
		SchedulingCreationRequest request = new SchedulingCreationRequest();
		request.setEmail(EMAIL);
		request.setMessage(MESSAGE);
		request.setPhoneNumber(PHONE_NUMBER);
		request.setScheduleDate(validScheduleDate);
		request.setSendType(SendTypeEnum.EMAIL.toString());
		return request;
	}
	
	class CommunicationSchedulingAllArgMatcher implements ArgumentMatcher<CommunicationScheduling>{

		SchedulingCreationRequest req;
		
		public CommunicationSchedulingAllArgMatcher(SchedulingCreationRequest req) {
			this.req = req;
		}
		
		@Override
		public boolean matches(CommunicationScheduling arg) {
			return req.getEmail().equals(arg.getEmail()) &&
					req.getMessage().equals(arg.getMessage()) &&
					req.getPhoneNumber().equals(arg.getPhoneNumber()) &&
					req.getScheduleDate().equals(arg.getScheduleDate().toString().substring(0, 13)) &&
					req.getSendType().toString().equals(arg.getSendType()) &&
					currentDate.equals(arg.getCreationDate().toLocalDate()) &&
					CommunicationScheduling.STATUS_AGENDADO.equals(arg.getStatus()) &&
					CommunicationScheduling.STATUS_DESCRIPTION_AGENDADO.equals(arg.getStatusDescription());
		}
		
	}
	
}
