package com.desafio.scheduling.communication.service.impl;

import java.time.LocalDate;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
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

import com.desafio.scheduling.communication.business.CommunicationSchedulingBuilder;
import com.desafio.scheduling.communication.business.CommunicationSchedulingRules;
import com.desafio.scheduling.communication.dao.CommunicationSchedulingDao;
import com.desafio.scheduling.communication.dao.domain.CommunicationScheduling;
import com.desafio.scheduling.communication.model.SchedulingCreationRequest;
import com.desafio.scheduling.communication.model.SchedulingCreationResponse;
import com.desafio.scheduling.communication.model.SchedulingCreationRequest.SendTypeEnum;
import com.desafio.scheduling.communication.service.CommunicationSchedulingService;
import com.desafio.scheduling.communication.util.Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

@TestInstance(Lifecycle.PER_CLASS)
public class CommunicationSchedulingServiceImplTest {

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
				communicationSchedulingDao);
		scheduleDateTime = LocalDate.now().plusDays(1).atTime(23, 59);
		validScheduleDate = communicationSchedulingUtil.localDateTimeToApi(scheduleDateTime);
		currentDate = LocalDate.now();
		currentLocalDateTime = LocalDateTime.now();
	}
	
	@DisplayName("Create a scheduling communication")
	@Test
	public void createCommunicationSchedulingUsingEmailSendType() {
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
	
	private SchedulingCreationRequest commonSchedulingCreationRequest() {
		SchedulingCreationRequest request = new SchedulingCreationRequest();
		request.setEmail(EMAIL);
		request.setMessage(MESSAGE);
		request.setPhoneNumber(PHONE_NUMBER);
		request.setScheduleDate(validScheduleDate);
		request.setSendType(SendTypeEnum._1);
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
